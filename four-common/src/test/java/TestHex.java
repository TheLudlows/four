import io.four.UnsafeStringUtils;
import io.four.UnsafeUtils;
import io.four.hex.HexUtils;
import org.junit.Test;

import static io.four.hex.HexUtils.toHex;

/**
 * @author TheLudlows
 */
public class TestHex {
    /*@Test
    public void getLength() {
        System.out.println(BYTE_TABLE.length);
        System.out.println(BYTE_TABLE_LE.length);
        System.out.println(HexUtils.HEX_TABLE.length);
        System.out.println(HexUtils.UPPER_HEX_TABLE.length);
        System.out.println(HexUtils.UPPER_HEX_TABLE_LE.length);
        System.out.println(26215/255);
    }
    @Test
    public void getByteTable() {
        int n=0;
        for(int i = 0; i< BYTE_TABLE.length; i++) {

            short x = BYTE_TABLE_LE[i];
            if(x != 0) {
                n++;
                System.out.println("i:"+i+",value:"+x+" ");
            }
        }
    System.out.println(n);
    }*/
    @Test
    public void getB(){
        System.out.println(0xFF*0xFF);
        System.out.println((short)(102 << 8 | 102));
    }
    public static String str ;
    @Test
    public void testUnsafe() throws InstantiationException {
        long start = System.currentTimeMillis();
        for(int i=0;i<1000000;i++) {
            str = (String)UnsafeUtils.unsafe().allocateInstance(String.class);

            //str = new String();
        }
        System.out.println(System.currentTimeMillis()-start);
    }

    @Test
    public void getBytes() {
        long start = System.currentTimeMillis();
        for(int i=0;i<1000000;i++) {
            String str = ""+i;
            //str.getBytes();
            UnsafeStringUtils.getLatin1Bytes(str);
        }
        System.out.println(System.currentTimeMillis()-start);
        String version = System.getProperty("java.version");

    }

    @Test
    public void testHex() {
        HexUtils.toHex(1);
        long start = System.currentTimeMillis();
        for(long i=0;i<1000000;i++) {
            //Long.toHexString(i);
           toHex(i);
        }

        System.out.println(System.currentTimeMillis()-start);
    }

    @Test
    public void testHex2() {
        System.out.println(toHex(Long.MAX_VALUE));
        System.out.println(Long.toHexString(Long.MAX_VALUE));
    }



}
