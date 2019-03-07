package io.four.remoting;

import io.four.protocol.four.Request;
import io.four.protocol.four.Response;

/**
 *  process the request and do some filter ops
 */
public interface Processor {

    Response process(Request request);
}
