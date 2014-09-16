package org.wangran.javatutorial.concurrency;

public class Join {

    static class MyThread implements Runnable {

        public void run() {
            Thread execution = Thread.currentThread();
            for (int i = 0; i < 50; ++i) {
                try {
                    Thread.sleep(3000l);
                    System.out.println(String.format("Thread (%d): Heart beat: %d ", execution.getId(), i));
                } catch (InterruptedException e) {
                    System.out.println(String.format("Thread (%d): exiting upon Interruption: ", execution.getId()));
                    return;
                }
            }
            System.out.println("Child done!");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread parent = Thread.currentThread();
        Thread child = new Thread(new MyThread());
        child.start();
        System.out.println(String.format("Parent (%d): will be waiting for child (%d)", parent.getId(), child.getId()));

        for (int i = 0; i++ < 50 && child.isAlive(); ) {
            System.out.println(String.format("Parent (%d): is still waiting for child (%d)", parent.getId(), child.getId()));
            child.join(1000);
            if (i == 50) {
                System.out.println(String.format("Parent (%d): is tired of waiting for child (%d)", parent.getId(), child.getId()));
                child.interrupt();
                child.join();
            }
        }

        System.out.println("Parent done!");

    }

}

