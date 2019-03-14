package io.four.remoting.netty;

import io.four.protocol.four.Request;
import io.netty.channel.EventLoop;
import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscAtomicArrayQueue;

import java.nio.channels.Channel;

public class Sender {

    private Channel channel;
    private MpscAtomicArrayQueue queue = new MpscAtomicArrayQueue(1024);
    EventLoop eventLoop;

    public void send(Request request) {
    }
}
