import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class Executor extends Thread {

    ConcurrentLinkedQueue<Worker> workerQ;
    CountDownLatch latch;
    Callback callback;

    public Executor(List<Runnable> taskList, Callback callback) {
        workerQ = new ConcurrentLinkedQueue<>();
        latch = new CountDownLatch(taskList.size());
        this.callback = callback;

        for (Runnable task : taskList) {
            workerQ.add(new Worker(task, latch));
        }
    }

    public void execute() {
        start();
    }

    @Override
    public void run() {
        while (true) {
            Worker worker = workerQ.poll();
            if (worker == null) {
                break;
            }

            worker.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (callback != null) {
            callback.onComplete();
        }
    }

    public interface Callback {
        void onComplete();
    }

    public static class Builder {
        List<Runnable> taskList = new ArrayList<>();
        Callback callback;


        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder add(Runnable task) {
            taskList.add(task);
            return this;
        }

        public Builder addCallback(Callback callback) {
            this.callback = callback;
            return this;
        }

        public Executor build() {
            return new Executor(taskList, callback);
        }
    }
}
