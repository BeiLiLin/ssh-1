package cn.itcast.framework.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import cn.itcast.framework.util.Dom4JUtil;

public class CenterFilter implements Filter {
	//��ȡ�����ļ���Ϣ��key����Ӧaction�е�name value��Action����
	private Map<String, Action> actions = new HashMap<String, Action>();
	private FilterConfig filterConfig;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		initCfg();//��ʼ�������ļ�
		this.filterConfig = filterConfig;
		
	}
	//��ʼ�������ļ�
	private void initCfg() {
		//��ȡXML�����ļ����������ļ��е���Ϣ��װ��������.�ٷŵ�actions��
		
		Document document = Dom4JUtil.getDocument();
		Element root = document.getRootElement();
		//�õ����е�actionԪ�أ�����Action���󣬷�װ��Ϣ
		List<Element> actionElements = root.elements("action");
		if(actionElements!=null&&actionElements.size()>0){
			for(Element actionElement:actionElements){
				//��װaction��Ϣ��ʼ
				Action action = new Action();
				action.setName(actionElement.attributeValue("name"));
				action.setClassName(actionElement.attributeValue("class"));
				String methodXmlAttrValue = actionElement.attributeValue("method");
				if(methodXmlAttrValue!=null)
					action.setMethod(methodXmlAttrValue);
				//��װaction��Ϣ����
				
				
				//�õ�ÿ��actionԪ���е�resultԪ�أ�����Result���󣬷�װ��Ϣ
				//��װ���ڵ�ǰaction�Ľ��
				List<Element> resultElements = actionElement.elements("result");
				if(resultElements!=null&&resultElements.size()>0){
					for(Element resultElement:resultElements){
						Result result = new Result();
						result.setName(resultElement.attributeValue("name"));
						result.setTargetUri(resultElement.getText().trim());
						String typeXmlValue = resultElement.attributeValue("type");
						if(typeXmlValue!=null){
							result.setType(ResultType.valueOf(typeXmlValue));
						}
						action.getResults().add(result);
					}
				}
				//��װ���ڵ�ǰaction�Ľ��
				
				
				
				//��Action���󶼷ŵ�Map��
				actions.put(action.getName(), action);
			}
		}
	}
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest)req;
			HttpServletResponse response = (HttpServletResponse)resp;
			//�����Ŀ���������
			
			//ȡһ�����ò���
			String aciontPostFixs [] = {"action","","do"};//������ĵ�ַ��action\do\�ս�β�Ļ������������ˡ�Ĭ��ֵ
			String aciontPostFix = filterConfig.getInitParameter("aciontPostFix");
			if(aciontPostFix!=null){
				aciontPostFixs = aciontPostFix.split("\\,");
			}
			
			//�����û������URI
			String uri = request.getRequestURI();//   /strutsDay01MyFramework/addCustomer.action
			
			//��ȡ��׺���������Ƿ���Ҫ���ǵĿ�ܽ��д���
			//�к�׺��
			String extendFileName = uri.substring(uri.lastIndexOf(".")+1);// /strutsDay01MyFramework/addCustomer.action   action
																			// /strutsDay01MyFramework/addCustomer.do   do
																			// /strutsDay01MyFramework/addCustomer   ""
			boolean needProcess = false;
			for(String s:aciontPostFixs){
				if(extendFileName.equals(s)){
					needProcess = true;
					break;
				}
			}
			
			if(needProcess){
				//��Ҫ��ܴ���
				//����uri�еĶ�������
				String requestActionName = uri.substring(uri.lastIndexOf("/")+1, uri.lastIndexOf("."));
				System.out.println("�������������ǣ�"+requestActionName);
				//����actions�ж�Ӧ��Action����
				if(actions.containsKey(requestActionName)){
					Action action = actions.get(requestActionName);
					//�õ������Ƶ��ֽ���
					Class clazz = Class.forName(action.getClassName());
					//��װ���ݵ�JavaBean��,����BeanUtils���
					Object bean = clazz.newInstance();
					BeanUtils.populate(bean, request.getParameterMap());
					//ʵ��������������ָ���ķ�������
					Method m = clazz.getMethod(action.getMethod(), null);
					//���ݷ����ķ���ֵ���������
					String resultValue = (String)m.invoke(bean, null);
					
					List<Result> results = action.getResults();
					if(results!=null&&results.size()>0){
						for(Result result:results){
							
							if(resultValue.equals(result.getName())){
								//���ݽ���е�type������ת�������ض���
								//�ض����Ŀ����ǽ���е�targetUri
								if("dispatcher".equals(result.getType().toString())){
									//ת��
									request.getRequestDispatcher(result.getTargetUri()).forward(request, response);
								}
								if("redirect".equals(result.getType().toString())){
									//�ض���
									response.sendRedirect(request.getContextPath()+result.getTargetUri());
								}
							}
						}
					}
				}else{
					throw new RuntimeException("The action "+requestActionName+" is not founded in your config files!");
				}
				
			}else{
				chain.doFilter(request, response);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}

	public void destroy() {

	}

}
