package io.four.exception;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class TransportException extends RpcException {

    public TransportException(String message) {
        super(message);
    }

    public TransportException(String message, Throwable cause) {
        super(message, cause);
    }
}
