package io.four.protocol.four;

/**
 * ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *     2   │   1    │    1   │     4     │              8
 * ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ┤
 * │       │        │        │           |
 * │   Agg    M Type  S Type   Body Size         MessageId              │
 * │       │        │        │           |
 * └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * <p>
 * Agg: colorful agg Might be 0 or 1
 * M Type: Message Type request/response/heat beat
 * S Type: Serialize Type
 * MessageId: 消息Id
 */


public class ProtocolConstant {

    public final static short AGG = 0x2F;
    public final static byte FASTJSON = 0x1;
    public final static byte KRYO = 0x2;
    public final static byte REQUEST = 0x1;
    public final static byte REPONSE = 0x2;
    public final static byte HEARTBEAT = 0x4;

}
