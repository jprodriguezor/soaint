package co.com.soaint.foundation.framework.proxy;

import java.lang.reflect.Proxy;

public class ProxyBuilder {

    private static ProxyBuilder instance = new ProxyBuilder();

    private ProxyBuilder() {
    }

    public static ProxyBuilder getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T newProxy(Object target, Class<T> type) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[]
                {type}, BusinessDelegateHandlerProxy.newInstance(target));
    }

}
