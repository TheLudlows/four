package io.four;

import io.four.invoker.Invoker;
import io.four.invoker.InvokerFactory;
import io.four.protocol.four.MessageUtil;
import io.four.protocol.four.Request;
import io.four.protocol.four.Response;
import io.four.remoting.Processor;

import java.util.concurrent.CompletableFuture;

import static io.four.protocol.four.ResponseEnum.*;

public class DefaultProcessor implements Processor {
    @Override
    public Response process(Request request) {
        Invoker<CompletableFuture<?>> invoker = InvokerFactory.getInvoker(request.getServiceName(), request.getMethodIndex());
        Response response = MessageUtil.getResponse();
        if (invoker == null) {
            return response.setStatus(NO_IMPL)
                    .setRequestId(request.getRequestId())
                    .setServiceResult("no impl");
        }
        CompletableFuture<?> future = invoker.invoke(request.getArgs());
        future.whenComplete((result, throwable) -> {
            response.setRequestId(request.getRequestId());
            if (result != null) {
                response.setServiceResult(result)
                        .setStatus(SUCCESS);
            } else if (throwable != null) {
                response.setStatus(IMPL_ERROR)
                        .setServiceResult(throwable.getMessage());
            }
        });
        return response;
    }
}
