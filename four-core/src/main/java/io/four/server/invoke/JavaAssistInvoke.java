package io.four.server.invoke;

import io.four.Invoker;
import io.four.client.proxy.SingleClassLoader;
import javassist.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class JavaAssistInvoke implements Invoker {
    @Override
    public Object invoke(Object... params) {
        return null;
    }

    public <T> T newInstance(Class clazz) throws Exception {
        Stream.of(clazz)
                .flatMap((a) -> Stream.of(a.getMethods()))
                .forEach((b) -> {
                    if (!CompletableFuture.class.equals(b.getReturnType())) {
                        throw new RuntimeException("method return-type must be CompletableFuture, " + b);
                    }
                    if (b.isDefault()) {
                        throw new RuntimeException("method access can't be default, " + b);
                    }
                });

        final String remoteClassName = clazz.getName() + "_DefaultImpl_"
                + UUID.randomUUID().toString().replace("-", "");
        // 创建类
        ClassPool pool = ClassPool.getDefault();
        CtClass defaultImplCtClass = pool.makeClass(remoteClassName);

        CtClass[] interfaces = {pool.getCtClass(clazz.getName())};
        defaultImplCtClass.setInterfaces(interfaces);

        // Add constructor
        CtConstructor constructor = new CtConstructor(null, defaultImplCtClass);
        constructor.setModifiers(Modifier.PUBLIC);
        constructor.setBody("{}");
        defaultImplCtClass.addConstructor(constructor);

        for (Method method : clazz.getMethods()) {

            if (method.isDefault()) {
                continue;
            }

            StringBuilder methodBuilder = new StringBuilder();

            methodBuilder.append("public ");
            methodBuilder.append(method.getReturnType().getName());
            methodBuilder.append(" ");
            methodBuilder.append(method.getName());
            methodBuilder.append("(");

            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];

                methodBuilder.append(parameterType.getName());
                methodBuilder.append(" param");
                methodBuilder.append(i);

                if (i != parameterTypes.length - 1) {
                    methodBuilder.append(", ");
                }
            }

            methodBuilder.append("){\r\n  throw new UnsupportedOperationException();\r\n}");

            CtMethod m = CtNewMethod.make(methodBuilder.toString(), defaultImplCtClass);
            defaultImplCtClass.addMethod(m);
        }
        byte[] bytes = defaultImplCtClass.toBytecode();
        Class<?> invokerClass = SingleClassLoader.loadClass(Thread.currentThread().getContextClassLoader(), bytes);
        return (T) invokerClass.getConstructor().newInstance();
    }
}

