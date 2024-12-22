package day7_Condition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  - wait()、与notify()、notifyAll()几个方法实现Java对象视角下的等待/通知机制，
 *    同样的， 在java Lock体系下依然会有同样的方法实现等待/通知机制。也就是Condition。
 *  - 从整体上来看Object的wait和notify/notify是与对象监视器配合完成线程间的等待/通知机制，
 *    而Condition与Lock配合完成等待通知机制，前者是java底层级别的，后者是语言级别的，具有更高的可控制性和扩展性。
 *    两者除了在使用方式上不同外，在功能特性上还是有很多的不同：
 */
public class MyCondition {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition(); // Condition由ReentrantLock对象创建

    public static class T1 extends Thread {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + "," + this.getName() + "准备获取锁!");
            lock.lock(); // Condition接口在使用前必须先调用ReentrantLock的lock()方法获得锁
            try {
                System.out.println(System.currentTimeMillis() + "," + this.getName() + "获取锁成功!");
                condition.await(); // 调用Condition接口的await()将释放锁，并且在该Condition上等待，直到有其他线程调用Condition的signal()方法唤醒线程
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println(System.currentTimeMillis() + "," + this.getName() + "释放锁成功!");
        }
    }

    public static class T2 extends Thread {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + "," + this.getName() + "准备获取锁!");
            lock.lock();
            try {
                System.out.println(System.currentTimeMillis() + "," + this.getName() + "获取锁成功!");
                condition.signal(); // Condition接口在使用前必须先调用ReentrantLock的lock()方法获得锁
                System.out.println(System.currentTimeMillis() + "," + this.getName() + " signal!");
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + "," + this.getName() + "准备释放锁!");
            } finally {
                lock.unlock();
            }
            System.out.println(System.currentTimeMillis() + "," + this.getName() + "释放锁成功!");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T1 t1 = new T1();
        t1.setName("t1");
        t1.start();
        TimeUnit.SECONDS.sleep(5);
        T2 t2 = new T2();
        t2.setName("t2");
        t2.start();
    }
}