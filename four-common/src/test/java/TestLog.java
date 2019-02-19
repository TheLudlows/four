import io.four.log.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TestLog {

    private static Logger logger = Logger.getLogger("com.bes.logging");
    public static void main(String argv[]) {
        // Log a FINEtracing message
        logger.info("Main running.");
        logger.fine("doingstuff");
        try {
           throw  new RuntimeException();
        } catch(Exception ex) {
            logger.log(Level.WARNING,"trouble sneezing", ex);
        }
        logger.fine("done");
    }

}
