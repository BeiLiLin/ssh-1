package cn.itcast.sh.action;

import java.util.List;

import cn.itcast.sh.dao.EntityDao;
import cn.itcast.sh.dao.EntityDaoFactory;
import cn.itcast.sh.domain.User;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class UserAction extends ActionSupport implements ModelDriven<User>{
	private User model = new User();

	public User getModel() {
		// TODO Auto-generated method stub
		return this.model;
	}
	
	public String aa() throws Exception{
		EntityDao entityDao = EntityDaoFactory.getInstance(EntityDao.class);
		List<User> userList = entityDao.find(User.class);
		/**
		 * push�������԰�һ��������뵽ջ��
		 * add(obj)��obj���뵽ջ��
		 * add(0,obj)��obj���뵽ջ��
		 * peek��ȡջ��Ԫ��
		 * pop�Ƴ�ջ��Ԫ��
		 */
		//ActionContext.getContext().getValueStack().push(userList);
		//ActionContext.getContext().getValueStack().getRoot().add(0,userList);
		ActionContext.getContext().put("userList", userList);
		System.out.println(userList.size());
		//ActionContext.getContext().getValueStack().pop();
		return "userList";
	}
}
