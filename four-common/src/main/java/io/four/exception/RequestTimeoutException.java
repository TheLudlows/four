package io.four.exception;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class RequestTimeoutException extends RpcException {
    public RequestTimeoutException(String message) {
        super(message);
    }

    public RequestTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
