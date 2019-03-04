package com.crawler.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextRegister implements ApplicationContextAware {
	
	private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public <T> T getServiceImpl(Class<T> clazs, String name) {
        return applicationContext.getBeansOfType(clazs).get(name);
    }

    public Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public <T> T getBean(Class<T> clazs) {
        return applicationContext.getBean(clazs);
    }

}
