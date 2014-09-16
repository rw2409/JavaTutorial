package org.wangran.javatutorial.concurrency;

/**
 * Created by wangran on 9/15/14.
 */
public class Interrupt {


    public static void main(String[] args) throws InterruptedException {

        Thread currentThread = Thread.currentThread();

        currentThread.interrupt();
        System.out.println(Thread.interrupted());
        System.out.println("1.Interrupted?:" + currentThread.isInterrupted());


        currentThread.interrupt();
        System.out.println(currentThread.isInterrupted());
        System.out.println("2.Interrupted?:" + currentThread.isInterrupted());

        currentThread.interrupt();
        try {
            throw new InterruptedException("Test interrupted Exception");
        } finally {
            System.out.println(currentThread.isInterrupted());
            System.out.println("3.Interrupted?:" + currentThread.isInterrupted());
        }
    }

}
