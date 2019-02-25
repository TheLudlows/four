package io.four;

/**
 * Invoker is used to call the service
 * @author TheLudlows
 */
public interface Invoker<T> {

    T invoke(Object... params);

}
