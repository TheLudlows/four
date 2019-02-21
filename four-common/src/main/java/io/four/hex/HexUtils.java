package io.four.hex;

import io.four.UnsafeStringUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

import static io.four.UnsafeUtils.unsafe;
import static sun.misc.Unsafe.ARRAY_BYTE_BASE_OFFSET;

/**
 * @author four
 */
public class HexUtils {

    private static final int SIZE = 256;
    private static final byte[] EMPTY_BYTES = new byte[0];

    private static final short[] HEX_TABLE = new short[SIZE];
    private static final short[] HEX_TABLE_LE = new short[SIZE];
    private static final short[] UPPER_HEX_TABLE = new short[SIZE];
    private static final short[] UPPER_HEX_TABLE_LE = new short[SIZE];

    private static final byte[] BYTE_TABLE;
    private static final byte[] BYTE_TABLE_LE;

    static {
        if (ByteOrder.nativeOrder() != ByteOrder.LITTLE_ENDIAN) {
            throw new Error("only support little-endian!");
        }

        final char[] digits = "0123456789abcdef".toCharArray();
        final char[] upDigits = "0123456789ABCDEF".toCharArray();

        for (int i = 0; i < SIZE; i++) {
            int high = digits[(0xF0 & i) >>> 4];
            int low = digits[0x0F & i];

            HEX_TABLE[i] = (short) (high << 8 | low);
            HEX_TABLE_LE[i] = (short) (low << 8 | high);

            high = upDigits[(0xF0 & i) >>> 4];
            low = upDigits[0x0F & i];

            UPPER_HEX_TABLE[i] = (short) (high << 8 | low);
            UPPER_HEX_TABLE_LE[i] = (short) (low << 8 | high);
        }

        BYTE_TABLE = new byte[HEX_TABLE[255] + 1];
        for (int i = 0; i < SIZE; i++) {
            BYTE_TABLE[HEX_TABLE[i]] = (byte) i;
            BYTE_TABLE[UPPER_HEX_TABLE[i]] = (byte) i;
        }

        BYTE_TABLE_LE = new byte[HEX_TABLE_LE[255] + 1];
        for (int i = 0; i < SIZE; i++) {
            BYTE_TABLE_LE[HEX_TABLE_LE[i]] = (byte) i;
            BYTE_TABLE_LE[UPPER_HEX_TABLE_LE[i]] = (byte) i;
        }
    }

    public static short byteToHex(byte b) {
        return HEX_TABLE[b & 0xFF];
    }

    public static short byteToHex(int b) {
        return HEX_TABLE[b & 0xFF];
    }

    public static short byteToHex(long b) {
        return HEX_TABLE[(int) (b & 0xFF)];
    }

    public static short byteToHexLE(byte b) {
        return HEX_TABLE_LE[b & 0xFF];
    }

    public static short byteToHexLE(int b) {
        return HEX_TABLE_LE[b & 0xFF];
    }

    public static short byteToHexLE(long b) {
        return HEX_TABLE_LE[((int) b) & 0xFF];
    }

    public static byte hexToByte(short hex) {
        return BYTE_TABLE[hex];
    }

    public static byte hexToByteLE(short hex) {
        return BYTE_TABLE_LE[hex];
    }

    public static byte hexToByte(long hex) {
        return BYTE_TABLE[(int) (hex & 0xFFFF)];
    }

    public static byte hexToByteLE(long hex) {
        return BYTE_TABLE_LE[(int) (hex & 0xFFFF)];
    }

    /**
     * 略慢
     *
     * @param bytes
     * @return
     */
    public static String toHex2(byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes is null");

        int length = bytes.length;
        if (length == 0) {
            return "";
        }

        byte[] hexBytes = new byte[length << 1];

        for (int i = 0; i < length; i++) {
            byte b = bytes[i];
            unsafe().putShort(hexBytes, (long) ARRAY_BYTE_BASE_OFFSET + i * 2, byteToHexLE(b));
        }

        return new String(hexBytes);
    }

    /**
     * high performance
     */
    public static byte[] toHex(String hex) {
        Objects.requireNonNull(hex, "bytes is null");
        return hex.length() == 0 ? null : toHex0(hex.getBytes());
    }

    private static byte[] toHex0(byte[] bytes) {
        int length = bytes.length;
        byte[] hexBytes = new byte[128];

        int batchLength = length >>> 3 << 3;

        for (int i = 0; i < batchLength; i += 8) {
            long offset = ARRAY_BYTE_BASE_OFFSET + i;
            long bytesLong = unsafe().getLong(bytes, offset);

            long hex;

            hex = ((byteToHexLE(bytesLong) & 0xFFFFL));
            hex |= ((byteToHexLE(bytesLong >>> 8 * 1) & 0xFFFFL) << 8 * 2);
            hex |= ((byteToHexLE(bytesLong >>> 8 * 2) & 0xFFFFL) << 8 * 4);
            hex |= ((byteToHexLE(bytesLong >>> 8 * 3) & 0xFFFFL) << 8 * 6);

            offset += i;
            unsafe().putLong(hexBytes, offset, hex);

            hex = ((byteToHexLE(bytesLong >>> 8 * 4) & 0xFFFFL));
            hex |= ((byteToHexLE(bytesLong >>> 8 * 5) & 0xFFFFL) << 8 * 2);
            hex |= ((byteToHexLE(bytesLong >>> 8 * 6) & 0xFFFFL) << 8 * 4);
            hex |= ((byteToHexLE(bytesLong >>> 8 * 7) & 0xFFFFL) << 8 * 6);

            unsafe().putLong(hexBytes, offset + 8, hex);
        }

        for (int i = batchLength; i < length; i++) {
            unsafe().putShort(hexBytes, (long) ARRAY_BYTE_BASE_OFFSET + (i << 1), byteToHexLE(bytes[i]));
        }

        return hexBytes;
    }

    /**
     * high performance
     *
     * @param byteBuffer
     * @param offset
     * @param length
     * @return
     */
    public static String toHex(ByteBuffer byteBuffer, int offset, int length) {
        Objects.requireNonNull(byteBuffer, "byteBuffer is null");

        if (byteBuffer.remaining() + byteBuffer.position() - offset < length) {
            throw new IllegalArgumentException("no enough bytes");
        }

        byte[] hexBytes = new byte[length << 1];

        for (int i = 0; i < offset; i++) {
            byte b = byteBuffer.get(offset + i);
            unsafe().putShort(hexBytes, (long) ARRAY_BYTE_BASE_OFFSET + i * 2, byteToHexLE(b));
        }

        return UnsafeStringUtils.toString(hexBytes);
    }

    /**
     * high performance
     */
    public static byte[] fromHex(String str) {
        int length = str.length();

        if (length == 0) {
            return EMPTY_BYTES;
        }
        if ((length & 1) == 1) {
            throw new IllegalArgumentException("not hex String");
        }
        return fromHex0(str);
        //fromHex1
    }

    public static byte[] fromHex0(String str) {
        int length = str.length();
        byte[] bytes = new byte[128];
        byte[] strBytes = str.getBytes();

        int batchLength = length >>> 4 << 4;
        for (int i = 0; i < batchLength; i += 16) {
            long offset = ARRAY_BYTE_BASE_OFFSET + i;

            long bytesLong = 0;
            long hexLong = unsafe().getLong(strBytes, offset);

            bytesLong = (hexToByteLE(hexLong) & 0xFFL);
            bytesLong |= ((hexToByteLE(hexLong >>> 8 * 2) & 0xFFL) << 8);
            bytesLong |= ((hexToByteLE(hexLong >>> 8 * 4) & 0xFFL) << 8 * 2);
            bytesLong |= ((hexToByteLE(hexLong >>> 8 * 6) & 0xFFL) << 8 * 3);

            hexLong = unsafe().getLong(strBytes, offset + 8);
            bytesLong |= ((hexToByteLE(hexLong) & 0xFFL) << 8 * 4);
            bytesLong |= ((hexToByteLE(hexLong >>> 8 * 2) & 0xFFL) << 8 * 5);
            bytesLong |= ((hexToByteLE(hexLong >>> 8 * 4) & 0xFFL) << 8 * 6);
            bytesLong |= ((hexToByteLE(hexLong >>> 8 * 6) & 0xFFL) << 8 * 7);

            unsafe().putLong(bytes, (long) ARRAY_BYTE_BASE_OFFSET + (i >>> 1), bytesLong);
        }

        for (int i = batchLength; i < length; i += 2) {
            short index = unsafe().getShort(strBytes, ARRAY_BYTE_BASE_OFFSET + i);
            bytes[i >>> 1] = hexToByteLE(index);
        }

        return bytes;
    }

    public static byte[] fromHex1(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length >>> 1];
        byte[] hexBytes = hex.getBytes();

        for (int i = 0; i < length; i += 2) {
            short index = unsafe().getShort(hexBytes, (long) ARRAY_BYTE_BASE_OFFSET + i);
            bytes[i >>> 1] = hexToByteLE(index);
        }

        return bytes;
    }


    public static void toHex(byte[] bytes, int offset, int value) {
        long hex;

        hex = ((byteToHexLE(value) & 0xFFFFL) << 8 * 6);
        hex |= ((byteToHexLE(value >>> 8 * 1) & 0xFFFFL) << 8 * 4);
        hex |= ((byteToHexLE(value >>> 8 * 2) & 0xFFFFL) << 8 * 2);
        hex |= ((byteToHexLE(value >>> 8 * 3) & 0xFFFFL));

        unsafe().putLong(bytes, ARRAY_BYTE_BASE_OFFSET + offset, hex);
    }

    public static void toHex(byte[] bytes, int offset, long value) {
        long hex;

        hex = ((byteToHexLE(value) & 0xFFFFL) << 8 * 6);
        hex |= ((byteToHexLE(value >>> 8 * 1) & 0xFFFFL) << 8 * 4);
        hex |= ((byteToHexLE(value >>> 8 * 2) & 0xFFFFL) << 8 * 2);
        hex |= ((byteToHexLE(value >>> 8 * 3) & 0xFFFFL));
        unsafe().putLong(bytes, ARRAY_BYTE_BASE_OFFSET + offset + 8, hex);

        hex = ((byteToHexLE(value >>> 8 * 4) & 0xFFFFL) << 8 * 6);
        hex |= ((byteToHexLE(value >>> 8 * 5) & 0xFFFFL) << 8 * 4);
        hex |= ((byteToHexLE(value >>> 8 * 6) & 0xFFFFL) << 8 * 2);
        hex |= ((byteToHexLE(value >>> 8 * 7) & 0xFFFFL));
        unsafe().putLong(bytes, ARRAY_BYTE_BASE_OFFSET + offset, hex);
    }

    public static String toHex(int value) {
        byte[] bytes = new byte[8];
        toHex(bytes, 0, value);

        return new String(bytes);
    }

}
