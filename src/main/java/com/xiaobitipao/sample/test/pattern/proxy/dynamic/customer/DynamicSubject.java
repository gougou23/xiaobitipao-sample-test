package com.xiaobitipao.sample.test.pattern.proxy.dynamic.customer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicSubject implements InvocationHandler {

    private Object proxyObj;

    public DynamicSubject(Object proxyObj) {
        this.proxyObj = proxyObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println("before ...");
        method.invoke(proxyObj, args);
        System.out.println("after  ...");

        return null;
    }
}
