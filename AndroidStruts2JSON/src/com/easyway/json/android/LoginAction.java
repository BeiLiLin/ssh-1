package com.easyway.json.android;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

/**
 * ��android����ʱ�����ǲ���Ҫ�õ�������SQLite���ݿ��ṩ���ݣ������ʱ���Ǵ������ϻ�ȡ���ݣ�
 * ��ôAndroid��ô�ӷ������˻�ȡ�����أ��кܶ��֣�����������
 *һ������HttpЭ���ȡ���ݷ�����
 *��������SAOPЭ���ȡ���ݷ�����
 *��ô���ǵ���ƪ������Ҫ�ǽ�����ʹ��HttpЭ���ȡ�����������ݣ�
 *�������ǲ�ȡ�ķ������˼���Ϊjava�����ΪStruts2,���߿�����Servlet,�ֻ��߿�ֱ�Ӵ�JSPҳ���л�ȡ���ݡ�
 *��ô�����������Ǳ㿪ʼ��һ·�̣�
 *���ȣ���д�������˷���,��������õ�MVC�����Struts2��Ŀ�ĺܵ���������Ϊ���Ժ�������������ҵ��Ŀ��
 *�����䱸Ϊ��android+SSH����Ȼ��ƪ�����ޣ��������ֱ����Strtus2���ѡ�
 *�������ˣ��½�WebProject ,ѡ��Java ee 5.0.
 *Ϊ�˸���Ŀ���Struts2��֧�֣����Ǳ��뵼��Struts2��һЩ��⣬���¼��ɣ���Щjar���ǲ��صģ��������Ǻ�����չ������Ҫʹ�õ��ģ�����Ū��ȥ����
 *xwork-core-2.2.1.1.jar  struts2-core-2.2.1.1.jar commons-logging-1.0.4.jar  freemarker-2.3.16.jar
 *ognl-3.0.jar  javassist-3.7.ga.jar commons-ileupload.jar commons-io.jar json-lib-2.1-jdk15.jar  
 *����JSON��ʽ����Ҫʹ�õ� struts2-json-plugin-2.2.1.1.jar   
 * ����struts2��json���.
 * 
 * 
 * ���ýӿ�ע��ķ�ʽע��HttpServletRequest,HttpServletResponse����
 * 
 * @author longgangbai
 *
 */
public class LoginAction extends ActionSupport implements ServletRequestAware,
		ServletResponseAware {
	/** * */
	private static final long serialVersionUID = 1L;

	HttpServletRequest request;

	HttpServletResponse response;
	
	private String userName;
	private String password;
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
    
	/**
	 * ģ���û���¼��ҵ��
	 */
	public void login() {
		try {
              //��������ýӿ�ע��ķ�ʽ�Ļ�ȡHttpServletRequest��HttpServletResponse�ķ�ʽ
			  // HttpServletRequest request =ServletActionContext.getRequest();
			  // HttpServletResponse response=ServletActionContext.getResponse();
			
			   this.response.setContentType("text/json;charset=utf-8");
			   this.response.setCharacterEncoding("UTF-8");
			   //JSONObject json=new JSONObject(); 
			    Map<String,String> json=new HashMap<String,String>();
				if ("admin".equals(userName)&&"123456".equals(password)) {
					 json.put("message", "��ӭ����Ա��½");
				} else if ((!"admin".equals(userName))&&"123456".equals(password)) {
					json.put("message", "��ӭ"+userName+"��½��");
				} else {
					json.put("message", "�Ƿ���½��Ϣ��");
				}
			  byte[] jsonBytes = json.toString().getBytes("utf-8");
			  response.setContentLength(jsonBytes.length);
			  response.getOutputStream().write(jsonBytes);
			  response.getOutputStream().flush();
			  response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}