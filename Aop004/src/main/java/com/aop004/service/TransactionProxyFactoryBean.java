package com.aop004.service;

import java.lang.reflect.Proxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

public class TransactionProxyFactoryBean implements FactoryBean<Object> {

    private Object target;
    private PlatformTransactionManager txManager;
    private String prefix;
    private Class<?> serviceInterface;

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setPlatformTransactionManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionHandler txHandler = new TransactionHandler(txManager, target, prefix);
        return Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { serviceInterface },
                txHandler);
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}