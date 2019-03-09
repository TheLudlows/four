package io.four.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author TheLudlows
 */
class CglibProxy {

    protected static  <T> T newProxy(Class<T> interfaceType, ProxyInvoke proxyInvoke) {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(interfaceType);
        enhancer.setCallback(new CglibInterceptor(proxyInvoke));
        enhancer.setClassLoader(interfaceType.getClassLoader());

        return interfaceType.cast(enhancer.create());
    }

    private static class CglibInterceptor implements MethodInterceptor {

        final ProxyInvoke proxyInvoke;

        CglibInterceptor(ProxyInvoke proxyInvoke) {
            this.proxyInvoke = proxyInvoke;
        }

        @Override
        public Object intercept(Object object, Method method, Object[] objects,
                                MethodProxy methodProxy) throws Throwable {
           return proxyInvoke.invoke(object.getClass().getCanonicalName(), objects,1);
        }
    }
}
