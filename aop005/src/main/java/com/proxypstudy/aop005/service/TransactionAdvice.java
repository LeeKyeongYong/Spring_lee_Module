package com.proxypstudy.aop005.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {
    private PlatformTransactionManager txManager;

    public void setPlatformTransactionManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.txManager.getTransaction(new DefaultTransactionDefinition());
        try {
            Object ret = invocation.proceed();
            txManager.commit(status);
            return ret;
        } catch (DataAccessException e) {
            txManager.rollback(status);
            throw e;
        }
    }
}