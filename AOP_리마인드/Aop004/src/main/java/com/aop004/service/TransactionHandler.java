package com.aop004.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionHandler implements InvocationHandler {

    private final PlatformTransactionManager txManager;
    private final Object target;
    private final String prefix;

    public TransactionHandler(PlatformTransactionManager txManager, Object target, String prefix) {
        this.txManager = txManager;
        this.target = target;
        this.prefix = prefix;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().startsWith(prefix)) {
            TransactionStatus status = this.txManager.getTransaction(new DefaultTransactionDefinition());
            try {
                Object ret = method.invoke(target, args);
                this.txManager.commit(status);
                return ret;
            } catch (InvocationTargetException e) {
                this.txManager.rollback(status);
                throw e.getTargetException();
            }
        } else {
            return method.invoke(target, args);
        }
    }
}