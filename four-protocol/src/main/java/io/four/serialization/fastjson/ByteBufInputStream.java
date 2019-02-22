package io.four.serialization.fastjson;

import io.netty.buffer.ByteBuf;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ByteBufInputStream extends InputStream {
    private ByteBuf buffer;
    private int startIndex;
    private int endIndex;
    private boolean closed;

    private boolean releaseOnClose;

    public ByteBufInputStream() {
    }

    public ByteBufInputStream(ByteBuf buffer) {
        setBuffer(buffer);
    }

    public ByteBufInputStream setBuffer(ByteBuf buffer) {
        int length = buffer.readableBytes();
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        if (length < 0) {
            if (releaseOnClose) {
                buffer.release();
            }
            throw new IllegalArgumentException("length: " + length);
        }
        if (length > buffer.readableBytes()) {
            if (releaseOnClose) {
                buffer.release();
            }
            throw new IndexOutOfBoundsException("Too many bytes to be read - Needs "
                    + length + ", maximum is " + buffer.readableBytes());
        }

        this.releaseOnClose = false;
        this.buffer = buffer;
        startIndex = buffer.readerIndex();
        endIndex = startIndex + length;
        buffer.markReaderIndex();
        return this;
    }

    public int readBytes() {
        return buffer.readerIndex() - startIndex;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            // The Closable interface says "If the stream is already closed then invoking this method has no effect."
            if (releaseOnClose && !closed) {
                closed = true;
                buffer.release();
            }
        }
    }

    @Override
    public int available() throws IOException {
        return endIndex - buffer.readerIndex();
    }

    @Override
    public void mark(int readlimit) {
        buffer.markReaderIndex();
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() throws IOException {
        if (!buffer.isReadable()) {
            return -1;
        }
        return buffer.readByte() & 0xff;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int available = available();
        if (available == 0) {
            return -1;
        }

        len = Math.min(available, len);
        buffer.readBytes(b, off, len);
        return len;
    }

    @Override
    public void reset() throws IOException {
        buffer.resetReaderIndex();
    }

    @Override
    public long skip(long n) throws IOException {
        if (n > Integer.MAX_VALUE) {
            return skipBytes(Integer.MAX_VALUE);
        } else {
            return skipBytes((int) n);
        }
    }


    public byte readByte() throws IOException {
        if (!buffer.isReadable()) {
            throw new EOFException();
        }
        return buffer.readByte();
    }

    public char readChar() throws IOException {
        return (char) readShort();
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    public void readFully(byte[] b, int off, int len) throws IOException {
        checkAvailable(len);
        buffer.readBytes(b, off, len);
    }

    public int readInt() throws IOException {
        checkAvailable(4);
        return buffer.readInt();
    }

    private StringBuilder lineBuf;


    public long readLong() throws IOException {
        checkAvailable(8);
        return buffer.readLong();
    }


    public short readShort() throws IOException {
        checkAvailable(2);
        return buffer.readShort();
    }


    public int readUnsignedByte() throws IOException {
        return readByte() & 0xff;
    }

    public int readUnsignedShort() throws IOException {
        return readShort() & 0xffff;
    }


    public int skipBytes(int n) throws IOException {
        int nBytes = Math.min(available(), n);
        buffer.skipBytes(nBytes);
        return nBytes;
    }

    private void checkAvailable(int fieldSize) throws IOException {
        if (fieldSize < 0) {
            throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
        }
        if (fieldSize > available()) {
            throw new EOFException("fieldSize is too long! Length is " + fieldSize
                    + ", but maximum is " + available());
        }
    }
}
