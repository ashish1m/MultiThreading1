import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {
        AtomicBoolean isProcessing = new AtomicBoolean(false);
        Executor.Builder.newBuilder()
                .add(() -> {
                    System.out.println("Task 1 started.");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Task 1 completed.");
                })
                .add(() -> {
                    System.out.println("Task 2 started.");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Task 2 completed.");
                })
                .add(() -> {
                    System.out.println("Task 3 started.");
                    try {
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Task 3 completed.");
                })
                .addCallback(() -> {
                    isProcessing.set(false);
                })
                .build()
                .execute();
        isProcessing.set(true);

        while (isProcessing.get()){
            // keep thread alive
        }

        System.out.println("Program is terminated.");
    }
}
