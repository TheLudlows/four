package io.four.protocol;


import java.io.Serializable;

/**
 * @author: TheLudlows
 * @since 0.1
 */
public class RequestBody implements Serializable {

    private static final long serialVersionUID = 5686121262356677607L;

    private long requestId;

    private String serviceName;

    private Object[] args;

    private long timestamp;


}
