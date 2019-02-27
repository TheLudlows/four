
package io.four.proxy;

import net.bytebuddy.ByteBuddy;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.INJECTION;
import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;


public enum Proxies {
    // BYTE BUDDY
    BYTE_BUDDY(new ProxyDelegate() {

        @Override
        public <T> T newProxy(Class<T> interfaceType, Object handler) {
            Class<? extends T> cls = new ByteBuddy()
                    .subclass(interfaceType)
                    .method(isDeclaredBy(interfaceType))
                    .intercept(to(handler, "handler").filter(not(isDeclaredBy(Object.class))))
                    .make()
                    .load(interfaceType.getClassLoader(), INJECTION)
                    .getLoaded();

            return Reflects.newInstance(cls);
        }
    }),
    // CGLIB
    CG_LIB(new ProxyDelegate() {

        @Override
        public <T> T newProxy(Class<T> interfaceType, Object handler) {

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(interfaceType);
            enhancer.setCallback((MethodInterceptor) handler);
            enhancer.setClassLoader(interfaceType.getClassLoader());

            return interfaceType.cast(enhancer.create());
        }
    }),
    //JDK
    JDK_PROXY(new ProxyDelegate() {

        @Override
        public <T> T newProxy(Class<T> interfaceType, Object handler) {

            Object object = Proxy.newProxyInstance(
                    interfaceType.getClassLoader(), new Class<?>[] { interfaceType }, (InvocationHandler) handler);

            return interfaceType.cast(object);
        }
    });

    private final ProxyDelegate delegate;

    private Proxies(ProxyDelegate delegate) {
        this.delegate = delegate;
    }

    public static Proxies getDefault() {
        return BYTE_BUDDY;
    }

    private <T> T newProxy(Class<T> interfaceType, Object handler) {
        return delegate.newProxy(interfaceType, handler);
    }

    interface ProxyDelegate {
        /**
         * new a object
         * @param interfaceType
         * @param handler
         * @param <T>
         * @return
         */
        <T> T newProxy(Class<T> interfaceType, Object handler);
    }
}
