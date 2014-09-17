package org.wangran.javatutorial.concurrency;

/**
 * Examples for coordination between Producer/Consumer that leverages a intrinsic lock
 * Created by wangran on 9/16/14.
 */
public class ProducerConsumer {
        private boolean isProductEmpty = true;
        private Product product = null;

        synchronized void addProduct(Product product){
            while(!isProductEmpty){
                try {
                    wait();
                } catch (InterruptedException e) { }
            }
            this.product = product;
            this.isProductEmpty = false;
            //Do not forget this notification!!!
            notifyAll();
        }

        synchronized Product removeProduct(){
            while(isProductEmpty){
                try {
                    wait();
                } catch (InterruptedException e) {}
            }
            Product ret = this.product;
            this.product = null;
            isProductEmpty = true;
            //Do not forget this notification!!!
            notifyAll();
            return ret;
        }

    static class Product {
        int id;
        public Product (int id) {
            this.id = id;
        }
    }

    static class Consumer implements Runnable{
        private  ProducerConsumer env = null;

        public Consumer(ProducerConsumer env){
            this.env = env;
        }

        void consume() {
           Product product = env.removeProduct();
           System.out.println("CNS: Successfully consume product:"+product.id);
        }

        @Override
        public void run(){
            while (true) {
                try {
                    Thread.sleep(1000l);
                    consume();
                    if(Thread.currentThread().isInterrupted()){
                        return;
                    }
                } catch (InterruptedException e){
                    return;
                }
            }
        }

    }

    static class Producer implements Runnable{
        private  ProducerConsumer env = null;

        public Producer(ProducerConsumer env){
            this.env = env;
        }

        void produce( int i){
            Product product = new Product(i);
            env.addProduct(product);
            System.out.println("PRO: Successfully produce product:"+i);
        }

        @Override
        public void run() {
            int id = 0;
            while(true) {
                try {
                    Thread.sleep(10l);
                    produce(id++);
                    if(Thread.currentThread().isInterrupted()){
                        return;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    public static void main(String [] args) {

        ProducerConsumer env = new ProducerConsumer();
        Thread consumer = new Thread(new Consumer(env));
        Thread producer = new Thread(new Producer(env));
        Thread current = Thread.currentThread();

        consumer.start();
        producer.start();

        for(int i = 0; i<10; ++i) {
            try {
                Thread.sleep(1000l);
            System.out.println("Main Thread: Cycle -"+i);
            } catch (InterruptedException e){ }
        }
        System.out.println("Closing Consumer...");
        try {
            //Do not forget to interrupt if you want to join the child process
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
            System.out.println("Encounter Exception while interrupt consumer");
        }
        System.out.println("Closing Producer...");
        try {
            //Do not forget to interrupt if you want to join the child process
            producer.interrupt();
            producer.join();
        } catch (InterruptedException e) {
            System.out.println("Encounter Exception while interrupt Producer");
        }
        System.out.println("Main thread finish!");
    }
}
