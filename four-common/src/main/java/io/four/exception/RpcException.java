package io.four.exception;


/**
 * @author: TheLudlows
 * @since 0.1
 */
public class RpcException extends Exception {
    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
}
