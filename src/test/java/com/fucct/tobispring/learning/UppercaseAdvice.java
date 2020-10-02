package com.fucct.tobispring.learning;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class UppercaseAdvice implements MethodInterceptor {
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final String ret = (String)invocation.proceed();
        return ret.toUpperCase();
    }
}
