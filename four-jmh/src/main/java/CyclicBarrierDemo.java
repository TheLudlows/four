import java.io.IOException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo implements Runnable {

    private CyclicBarrier cyclicBarrier;

    public CyclicBarrierDemo(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " running ");
        try {
            cyclicBarrier.await();
            System.out.println("all arrive at " + System.currentTimeMillis());
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) throws IOException {
        CyclicBarrier barrier = new CyclicBarrier(5,() -> System.out.println("done!"));
        for (int i = 0; i < 5; i++) {
            new Thread(new CyclicBarrierDemo(barrier)).start();
        }
        System.in.read();
    }
}