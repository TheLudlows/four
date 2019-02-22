package io.four.serialization.fastjson;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.io.OutputStream;

public class ByteBufOutputStream extends OutputStream {

    private  ByteBuf buffer;
    private  int startIndex;

    public ByteBufOutputStream() {
    }

    public ByteBufOutputStream(ByteBuf buffer) {
        setByteBuf(buffer);
    }

    public ByteBufOutputStream setByteBuf(ByteBuf buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        this.buffer = buffer;
        startIndex = buffer.writerIndex();
        return this;
    }

    /**
     * Returns the number of written bytes by this stream so far.
     */
    public int writtenBytes() {
        return buffer.writerIndex() - startIndex;
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (len == 0) {
            return;
        }

        buffer.writeBytes(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        buffer.writeBytes(b);
    }

    @Override
    public void write(int b) throws IOException {
        buffer.writeByte(b);
    }

    public void writeBoolean(boolean v) throws IOException {
        buffer.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        buffer.writeByte(v);
    }

    public void writeBytes(String s) throws IOException {
        buffer.writeCharSequence(s, CharsetUtil.US_ASCII);
    }

    public void writeChar(int v) throws IOException {
        buffer.writeChar(v);
    }

    public void writeChars(String s) throws IOException {
        int len = s.length();
        for (int i = 0 ; i < len ; i ++) {
            buffer.writeChar(s.charAt(i));
        }
    }

    public void writeDouble(double v) throws IOException {
        buffer.writeDouble(v);
    }

    public void writeFloat(float v) throws IOException {
        buffer.writeFloat(v);
    }

    public void writeInt(int v) throws IOException {
        buffer.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        buffer.writeLong(v);
    }

    public void writeShort(int v) throws IOException {
        buffer.writeShort((short) v);
    }


    public ByteBuf buffer() {
        return buffer;
    }
}
