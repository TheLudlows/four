package io.four.proxy;

import io.four.Invoker;
import javassist.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * @author TheLudlows
 */
public class JavassistInvoke implements Invoker {

    private static final ProxyInvoke proxyInvoke = new ProxyInvoke();

    @Override
    public Object invoke(Object... params) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newFailFastProxy(Class interfaceClass) throws Exception {

        CtClass defaultImplCtClass = checkAndCreate(interfaceClass);

        for (Method method : interfaceClass.getMethods()) {
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
            System.out.println(methodBuilder.toString());
            CtMethod m = CtNewMethod.make(methodBuilder.toString(), defaultImplCtClass);
            defaultImplCtClass.addMethod(m);
        }
        byte[] bytes = defaultImplCtClass.toBytecode();
        Class<?> invokerClass = SingleClassLoader.loadClass(Thread.currentThread().getContextClassLoader(), bytes);
        return (T) invokerClass.getConstructor().newInstance();
    }

    @SuppressWarnings("unchecked")
    public static <T> T newProxy(Class interfaceClass) throws Exception {
        CtClass defaultImplCtClass = checkAndCreate(interfaceClass);

        List<String> methodList = createMethods(interfaceClass);
        for (String methodStr : methodList) {
            defaultImplCtClass.addMethod(CtNewMethod.make(methodStr, defaultImplCtClass));
        }
        byte[] bytes = defaultImplCtClass.toBytecode();
        Class<?> invokerClass = SingleClassLoader.loadClass(Thread.currentThread().getContextClassLoader(), bytes);
        Object object = invokerClass.getConstructor().newInstance();
        invokerClass.getField("proxyInvoke").set(object, proxyInvoke);
        return (T) object;
    }

    private static CtClass checkAndCreate(Class clazz) throws NotFoundException, CannotCompileException {
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

        // create impl
        ClassPool pool = ClassPool.getDefault();
        CtClass defaultImplCtClass = pool.makeClass(remoteClassName);

        defaultImplCtClass.addInterface(pool.getCtClass(clazz.getName()));
        // Add constructor
        defaultImplCtClass.addConstructor(CtNewConstructor.defaultConstructor(defaultImplCtClass));
        defaultImplCtClass.addField(CtField.make("public " + ProxyInvoke.class.getCanonicalName() + " proxyInvoke = null;", defaultImplCtClass));
        return defaultImplCtClass;
    }

    private static List<String> createMethods(Class<?> interfaceClass) {
        Method[] methodAry = interfaceClass.getMethods();
        StringBuilder sb = new StringBuilder();
        List<String> resultList = new ArrayList();
        for (Method m : methodAry) {
            if (m.isDefault()) {
                continue;
            }
            sb.append("public ")
                    .append(m.getReturnType().getCanonicalName())
                    .append(" ")
                    .append(m.getName())
                    .append("(");

            Class<?>[] parameterTypes = m.getParameterTypes();
            Class<?> returnType = m.getReturnType();

            int c = 0;
            for (Class<?> type : parameterTypes) {
                sb.append(type.getCanonicalName() + " arg" + (c++) + " ,");
            }
            sb.deleteCharAt(sb.length() - 1);

            sb.append(")");
            Class<?>[] exceptions = m.getExceptionTypes();
            if (exceptions.length > 0) {
                sb.append(" throws ");
                for (Class<?> exception : exceptions) {
                    sb.append(exception.getCanonicalName() + " ,");
                }
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("{")
                    .append(" String serviceName = \"" + interfaceClass.getCanonicalName() + "\";")
                    .append(" Object[] params = new Object[" + c + "];");
            for (int i = 0; i < c; i++) {
                sb.append("params[" + i + "] = arg" + i + ";");
            }
            sb.append(" Object response = " +
                    "proxyInvoke.invoke(serviceName, params);")
                    .append(" return ");
            if (returnType.equals(void.class)) {
                sb.append(" return;");
            } else {
                sb.append(buildReturn(returnType, "response"))
                        .append(";");
            }
            sb.append("}");
            System.out.println(sb);
            resultList.add(sb.toString());
            sb.delete(0, sb.length());
        }
        return resultList;
    }

    private static String buildReturn(Class<?> cl, String name) {
        if (cl.isPrimitive()) {
            if (Boolean.TYPE == cl)
                return name + "==null?false:((Boolean)" + name + ").booleanValue()";
            if (Byte.TYPE == cl)
                return name + "==null?(byte)0:((Byte)" + name + ").byteValue()";
            if (Character.TYPE == cl)
                return name + "==null?(char)0:((Character)" + name + ").charValue()";
            if (Double.TYPE == cl)
                return name + "==null?(double)0:((Double)" + name + ").doubleValue()";
            if (Float.TYPE == cl)
                return name + "==null?(float)0:((Float)" + name + ").floatValue()";
            if (Integer.TYPE == cl)
                return name + "==null?(int)0:((Integer)" + name + ").intValue()";
            if (Long.TYPE == cl)
                return name + "==null?(long)0:((Long)" + name + ").longValue()";
            if (Short.TYPE == cl)
                return name + "==null?(short)0:((Short)" + name + ").shortValue()";
            throw new RuntimeException(name + " is unknown primitive type.");
        }
        return "(" + getName(cl) + ")" + name;
    }


    private static String getName(Class<?> c) {
        if (c.isArray()) {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append("[]");
                c = c.getComponentType();
            }
            while (c.isArray());

            return c.getName() + sb.toString();
        }
        return c.getName();
    }
}

