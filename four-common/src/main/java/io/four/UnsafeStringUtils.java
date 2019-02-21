package io.four;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;

/**
 * @author TheLudlows
 */
public class UnsafeStringUtils {

    private static final long coderFieldOffset;
    private static final long bytesFieldOffset;
    private static final long hashFieldOffset;

    static {
        {
            long _coderFieldOffset;

            try {
                Field coderField = String.class.getDeclaredField("coder");
                _coderFieldOffset = UnsafeUtils.unsafe().objectFieldOffset(coderField);

                if (UnsafeUtils.unsafe().getByte("你好，世界", _coderFieldOffset) != 1) {
                    _coderFieldOffset = -1;
                }
            } catch (Throwable e) {
                _coderFieldOffset = -1;
            }

            coderFieldOffset = _coderFieldOffset;
        }

        {
            long _bytesFieldOffset;

            try {
                Field valueField = String.class.getDeclaredField("value");
                _bytesFieldOffset = UnsafeUtils.unsafe().objectFieldOffset(valueField);

                if (UnsafeUtils.unsafe().getObject("你好，世界", _bytesFieldOffset) == null) {
                    _bytesFieldOffset = -1;
                }

                if (!(UnsafeUtils.unsafe().getObject("你好，世界", _bytesFieldOffset) instanceof byte[])) {
                    _bytesFieldOffset = -1;
                }
            } catch (Throwable e) {
                _bytesFieldOffset = -1;
            }

            bytesFieldOffset = _bytesFieldOffset;

            if (bytesFieldOffset == -1) {
                System.err.println("StringUtils2.getUTF8Bytes(String) is broken");
            }
        }

        {
            long _hashFieldOffset;

            try {
                Field hashField = String.class.getDeclaredField("hash");
                _hashFieldOffset = UnsafeUtils.unsafe().objectFieldOffset(hashField);
            } catch (Throwable e) {
                _hashFieldOffset = -1;
            }

            hashFieldOffset = _hashFieldOffset;

            if (hashFieldOffset == -1) {
                System.err.println("StringUtils2.toString(byte[]) is broken");
            }
        }

    }

    public static boolean isLatin1(String str) {
        if (coderFieldOffset > 0) {
            return UnsafeUtils.unsafe().getByte(str, coderFieldOffset) == 0;
        }

        return false;
    }

    /**
     * unsafe, do not change the return bytes
     *
     * @param str
     * @return
     */
    public static byte[] getUTF8Bytes(String str) {
        if (bytesFieldOffset > 0 && isLatin1(str)) {
            byte[] bytes = (byte[]) UnsafeUtils.unsafe().getObject(str, bytesFieldOffset);

            if (bytes != null) {
                boolean ascii = true;

                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] < 0) {
                        ascii = false;
                        break;
                    }
                }

                if (ascii) {
                    return bytes;
                }
            }
        }

        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * unsafe, do not change the return bytes
     *
     * @param str
     * @return
     */
    public static byte[] getLatin1Bytes(String str) {
        if (bytesFieldOffset > 0 && isLatin1(str)) {
            byte[] bytes = (byte[]) UnsafeUtils.unsafe().getObject(str, bytesFieldOffset);

            if (bytes != null) {
                return bytes;
            }
        }

        return str.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * unsafe
     *
     * @param bytes
     * @return
     */
    public static String toString(byte[] bytes) {
        if (bytesFieldOffset == -1 || coderFieldOffset == -1 || hashFieldOffset == -1) {
            return new String(bytes, StandardCharsets.ISO_8859_1);
        }

        // allocate String instance
        Object obj;
        try {
            obj = UnsafeUtils.unsafe().allocateInstance(String.class);
        } catch (Throwable t) {
            return new String(bytes, StandardCharsets.ISO_8859_1);
        }

        UnsafeUtils.unsafe().putObject(obj, bytesFieldOffset, bytes);
        UnsafeUtils.unsafe().putByte(obj, coderFieldOffset, (byte) 0);
        UnsafeUtils.unsafe().putInt(obj, hashFieldOffset, 0);

        return (String) obj;
    }

}

