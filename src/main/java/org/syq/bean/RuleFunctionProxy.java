package org.syq.bean;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by cage on 2020-07-12
 */
public class RuleFunctionProxy implements InvocationHandler {

    private final Object target;

    public RuleFunctionProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
