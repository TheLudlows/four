package io.four.invoker;

import com.esotericsoftware.minlog.Log;
import javassist.*;

import java.lang.reflect.Method;
import java.util.UUID;

import static io.four.invoker.SourceCodeUtils.*;

/**
 * 250% of reflection invoke
 *
 * @param <T>
 */
public class JavassistInvoker<T> implements Invoker {
    private Object service;
    private Method method;
    private Class clazz;
    private Invoker<T> invoker;
    private int paramLength;

    @Override
    public T invoke(Object... params) {
        if (paramLength != params.length) {
            throw new RuntimeException("error param");
        }
        return invoker.invoke(params);
    }

    public JavassistInvoker(Object service, Method method, Class clazz) {
        this.service = service;
        this.method = method;
        this.clazz = clazz;
        paramLength = method.getParameterCount();
        this.invoker = generateRealInvoker();
    }

    private Invoker<T> generateRealInvoker() {
        final String invokerClassName = "io.four.invoke.generate.Invoker_"
                + UUID.randomUUID().toString().replace("-", "");
        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass invokerCtClass = pool.makeClass(invokerClassName);
            invokerCtClass.setInterfaces(new CtClass[]{pool.getCtClass(Invoker.class.getName())});

            // 添加私有成员service
            CtField serviceField = new CtField(pool.get(service.getClass().getName()), "service", invokerCtClass);
            serviceField.setModifiers(Modifier.PRIVATE | Modifier.FINAL);
            invokerCtClass.addField(serviceField);

            // 添加有参的构造函数
            CtConstructor constructor = new CtConstructor(new CtClass[]{pool.get(service.getClass().getName())},
                    invokerCtClass);
            constructor.setBody("{$0.service = $1;}");
            invokerCtClass.addConstructor(constructor);

            Class[] parameterTypes = method.getParameterTypes();

            {// 添加通用方法
                StringBuilder methodBuilder = new StringBuilder();
                StringBuilder resultBuilder = new StringBuilder();

                methodBuilder.append("public Object invoke(Object[] params) {\r\n");

                methodBuilder.append("  return ");

                resultBuilder.append("service.");
                resultBuilder.append(method.getName());
                resultBuilder.append("(");

                for (int i = 0; i < parameterTypes.length; i++) {
                    Class<?> paramType = parameterTypes[i];

                    resultBuilder.append("((");
                    resultBuilder.append(forceCast(paramType));

                    resultBuilder.append(")params[");
                    resultBuilder.append(i);
                    resultBuilder.append("])");

                    resultBuilder.append(unbox(paramType));
                }

                resultBuilder.append(")");

                String resultStr = box(method.getReturnType(), resultBuilder.toString());

                methodBuilder.append(resultStr);
                methodBuilder.append(";\r\n}");
                CtMethod m = CtNewMethod.make(methodBuilder.toString(), invokerCtClass);
                invokerCtClass.addMethod(m);
            }
            Class<?> invokerClass = invokerCtClass.toClass();
            // 通过反射创建有参的实例
            @SuppressWarnings("unchecked")
            Invoker<T> invoker = (Invoker<T>) invokerClass.getConstructor(service.getClass()).newInstance(service);
            return invoker;
        } catch (Exception e) {
            Log.warn("Generate the" + clazz.getName() + "invoker failed", e);
            System.exit(-1);
        }
        return null;
    }

}
