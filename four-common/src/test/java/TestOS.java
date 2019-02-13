import org.junit.Test;

/**
 * @author TheLudlows
 * @since 0.1
 */
public class TestOS {
    @Test
    public void testos() {
        System.out.println(System.getProperties().getProperty("os.name"));
    }
}
