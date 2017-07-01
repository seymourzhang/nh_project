package com.nh.ifarm.util.service.impl;

import java.lang.reflect.Method;

import com.nh.ifarm.util.service.ModuleService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.nh.ifarm.device.yintong.entity.YingtongControl;
/**
 * 模块服务类
 * Created by Raymon on 7/7/2016.
 */
@Service
public class ModuleServiceImpl implements ModuleService{
	@Autowired
	private BeanFactory beanFactory ;  
	
	public void setBeanFactory(BeanFactory factory) throws BeansException {  
		this.beanFactory = factory;  
	}  
	public <T> T getBean(String beanName) {  
		if (null != beanFactory){
			return (T) beanFactory.getBean(beanName); 
		}  
		return null;  
	}  
	/**
	 * 
	 * @TODO 任何bean b =controlMain.service("ssTestss","getStr",objs);  
	 * @param str 类名 类名第一个字母小写
	 * @param mstr 方法名
	 * @param objs Object[] objs = new Object[]{123, "str", new Date()};
	 * @return
	 * @Date 2016-6-28
	 * @author loyeWen
	 *
	 */
	public <T> T service(String str,String mstr,Object[] objs) {
		try {
			Object obj = this.getBean(str);
			Class clazz = obj.getClass();
			Method[] ms = clazz.getMethods();
			for(int i = 0 ; i < ms.length ; i ++){
				if(ms[i].getName().equalsIgnoreCase(mstr)){
					return (T)ms[i].invoke(obj, objs);
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return  null;
		}
	}

//	public void addYTC(YingtongControl ytc){
//		System.out.println(ytc.getCid());
//	}
	
	
}
