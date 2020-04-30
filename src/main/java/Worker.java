import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Worker implements Runnable{
    CountDownLatch latch;
    Thread thread;
    Runnable task;
    AtomicBoolean started;

    public Worker(Runnable task, CountDownLatch latch) {
        thread = new Thread(this);
        this.task = task;
        this.latch = latch;
        started = new AtomicBoolean(false);
    }

    public void start(){
        if (!started.getAndSet(true)){
            thread.start();
        }
    }

    @Override
    public void run() {
        task.run();
        latch.countDown();
    }
}
