package io.four.proxy;

import javassist.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author TheLudlows
 */
class JavassistProxy {

    @SuppressWarnings("unchecked")
    protected static <T> T newFailFastProxy(Class interfaceClass ) throws Exception {
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
            CtMethod m = CtNewMethod.make(methodBuilder.toString(), defaultImplCtClass);
            defaultImplCtClass.addMethod(m);
        }
        Class<?> invokerClass = defaultImplCtClass.toClass();
        return (T) invokerClass.getConstructor().newInstance();
    }

    @SuppressWarnings("unchecked")
    protected static <T> T newProxy(Class interfaceClass, ProxyInvoke proxyInvoke) throws Exception {
        CtClass defaultImplCtClass = checkAndCreate(interfaceClass);

        List<String> methodList = createMethods(interfaceClass);
        for (String methodStr : methodList) {
            defaultImplCtClass.addMethod(CtMethod.make(methodStr, defaultImplCtClass));
        }
        Class<?> invokerClass = defaultImplCtClass.toClass();
        Object object = invokerClass.getConstructor().newInstance();
        invokerClass.getField("proxyInvoke").set(object, proxyInvoke);
        return (T) object;
    }

    private static CtClass checkAndCreate(Class clazz) throws NotFoundException, CannotCompileException {
        Stream.of(clazz)
                .flatMap((a) -> Stream.of(a.getMethods()))
                .forEach((b) -> {
                 /*   if (!CompletableFuture.class.equals(b.getReturnType())) {
                        throw new RuntimeException("method return-type must be CompletableFuture, " + b);
                    }*/
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
                    .append("( ");

            Class<?>[] parameterTypes = m.getParameterTypes();
            Class<?> returnType = m.getReturnType();

            int paramLength = 0;
            for (Class<?> type : parameterTypes) {
                sb.append(type.getCanonicalName() + " arg" + (paramLength++));
                if (paramLength != parameterTypes.length) {
                    sb.append(", ");
                }
            }
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
                    .append(" \r\n String serviceName = \"" + interfaceClass.getCanonicalName() + "\";")
                    .append(" \r\n Object[] params = new Object[" + paramLength + "];")
                    .append(" \r\n String methodName =\"" + m.getName() + "\";");

            for (int i = 0; i < paramLength; i++) {
                sb.append("\r\n params[" + i + "] = ($w)$" + (i + 1) + ";");
            }
            sb.append("\r\n Object response = " +
                    "proxyInvoke.invoke(serviceName, params);")
                    .append(" \r\n return ");
            if (!returnType.equals(void.class)) {
                sb.append(buildReturn(returnType, "response"));
            }
            sb.append(";\r\n}");
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

