package com.labs.springbatchsamples.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext appContext;

	public SpringContextUtil() {}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		appContext = applicationContext;
	}

	public static Object getBean(String beanName) {
		return appContext.getBean(beanName);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName, Class<T> type) {
		return (T) appContext.getBean(beanName);
	}

	public static <T> T getBean(Class<T> type) {
		return appContext.getBean(type);
	}

}