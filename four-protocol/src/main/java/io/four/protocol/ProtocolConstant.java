package io.four.protocol;

/**
 * @author: TheLudlows
 * @since 0.1
 */
public interface ProtocolConstant {
    short AGG = 0x2F;
    byte FASTJSN = 0x1;
    byte KRYO = 0x2;
    byte REQUEST = 0x1;
    byte REPONSE = 0x2;
    byte HEARTBEAT = 0x4;
}
