package io.four.server.invoke;

/**
 * Invoke is used to call the service
 * @author TheLudlows
 */
public interface Invoke<T> {

    T invoke(Object... params);

}
