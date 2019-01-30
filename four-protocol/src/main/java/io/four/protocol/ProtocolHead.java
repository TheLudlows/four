package io.four.protocol;


import static io.four.protocol.ProtocolConstant.AGG;

/**
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *       2   │   1   │    1   │     4    │              8
 *  ├ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─  ┤
 *           │       │        │          │
 *  │   Agg    M Type  S Type   Body Size          MessageId             │
 *           │       │        │          │
 *  └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *
 *  Agg: colorful agg Might be 0 or 1
 *  M Type: Message Type request/response/heat beat
 *  S Type: Serialize Type
 *  protocolId: 消息Id
 */


public class ProtocolHead {

    private final  byte agg = AGG;

    private byte mType;

    private byte sType;

    private int size;

    private long messageId;

    public ProtocolHead(){

    }

    public ProtocolHead(byte mType, byte sType, int size) {
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

    public ProtocolHead setmType(byte mType) {
        this.mType = mType;
        return this;
    }

    public byte getsType() {
        return sType;
    }

    public ProtocolHead setsType(byte sType) {
        this.sType = sType;
        return this;
    }

    public int getSize() {
        return size;
    }

    public ProtocolHead setSize(int size) {
        this.size = size;
        return this;
    }

    public long getMessageId() {
        return messageId;
    }

    public ProtocolHead setMessageId(long protocolId) {
        this.messageId = protocolId;
        return this;
    }
}
