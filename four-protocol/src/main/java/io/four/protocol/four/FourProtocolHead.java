package io.four.protocol.four;

/**
 * ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *     2   │   1   │    1   │     4     │              8
 * ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ┤
 * │       │        │          │        │
 * │   Agg    M Type  S Type    Body Size          MessageId             │
 * │       │        │          │        │
 * └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * <p>
 * Agg: colorful agg Might be 0 or 1
 * M Type: Message Type request/response/heat beat
 * S Type: Serialize Type
 * protocolId: 消息Id
 */


public class FourProtocolHead {

    private final static short AGG = 0x2F;
    private final static byte FASTJSON = 0x1;
    private final static byte KRYO = 0x2;
    private final static byte REQUEST = 0x1;
    private final static byte REPONSE = 0x2;
    private final static byte HEARTBEAT = 0x4;

    private final byte agg = AGG;

    private byte mType;

    private byte sType;

    private int size;

    private long messageId;

    public FourProtocolHead() {

    }

    public FourProtocolHead(byte mType, byte sType, int size) {
        this.mType = mType;
        this.sType = sType;
        this.size = size;
    }

    @Override
    public String toString() {
        return "ProtocolHead{" +
                "mType=" + mType +
                ", sType=" + sType +
                ", size=" + size +
                '}';
    }

    public byte getAgg() {
        return agg;
    }

    public byte getmType() {
        return mType;
    }

    public FourProtocolHead setmType(byte mType) {
        this.mType = mType;
        return this;
    }

    public byte getsType() {
        return sType;
    }

    public FourProtocolHead setsType(byte sType) {
        this.sType = sType;
        return this;
    }

    public int getSize() {
        return size;
    }

    public FourProtocolHead setSize(int size) {
        this.size = size;
        return this;
    }

    public long getMessageId() {
        return messageId;
    }

    public FourProtocolHead setMessageId(long protocolId) {
        this.messageId = protocolId;
        return this;
    }
}
