package io.four.protocol.four;

/**
 * ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─┐
 *     2   │   1    │    1   │     8     │       4
 * ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ──  ┤
 * │       │        │        │           |
 * │   Agg    M Type  S Type    MessageId    Body Size        │
 * │       │        │        │           |
 * └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ -┘
 * <p>
 * Agg: colorful agg Might be 0 or 1
 * M Type: Message Type request/response/heat beat
 * S Type: Serialize Type
 * MessageId: 消息Id
 */


public interface ProtocolConstant {

    short AGG = 0x2F;
    byte FASTJSON = 0x1;
    byte KRYO = 0x2;
    byte REQUEST = 0x1;
    byte RESPONSE = 0x2;
    byte HEARTBEAT = 0x4;
    int HEAD_LENGGTH = 0xc;
    /**
     * service name length
     */
    int SERVERNAME_LENGTH = 0x40;

}
