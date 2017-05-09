package co.com.soaint.foundation.framework.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class BusinessDelegateHandlerProxy implements InvocationHandler {
    private static Logger LOGGER = LogManager.getLogger(BusinessDelegateHandlerProxy.class.getName());
    private Object target;

    private BusinessDelegateHandlerProxy(Object target) {
        this.target = target;
    }

    public static BusinessDelegateHandlerProxy newInstance(Object target) {
        return new BusinessDelegateHandlerProxy(target);
    }

    @Override
    public Object invoke(Object target, Method method, Object[] params) throws Throwable {
        LOGGER.info("------------- proxy method: {}", method.getName());
        return method.invoke(this.target, params);
    }
}
