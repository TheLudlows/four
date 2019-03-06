package io.four;

import java.util.concurrent.CompletableFuture;

public class InvokeFuture extends CompletableFuture {

   long requestId;
   long time;

   public long getRequestId() {
      return requestId;
   }

   public InvokeFuture setRequestId(long requestId) {
      this.requestId = requestId;
      return this;
   }

   public long getTime() {
      return time;
   }

   public InvokeFuture setTime(long time) {
      this.time = time;
      return this;
   }
}
