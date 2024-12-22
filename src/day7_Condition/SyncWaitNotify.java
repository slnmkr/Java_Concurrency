package day7_Condition;

import java.util.concurrent.TimeUnit;

public class SyncWaitNotify {
    static Object lock = new Object();

    public static class T1 extends Thread {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + "," + this.getName() + "准备获取锁!");
            synchronized (lock) { // t1获取到lock的锁
                System.out.println(System.currentTimeMillis() + "," + this.getName() + "获取锁成功!");
                try {
                    // wait() 方法暂停当前线程，并立即释放对象锁; 当其他线程调用对象的 notify() 或 notifyAll() 方法并且当前线程重新获得对象锁时，线程会继续执行。
                    lock.wait(); // t1先获取锁，调用了wait()方法将当前线程t1置为等待状态，并释放lock的锁
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(System.currentTimeMillis() + "," + this.getName() + "释放锁成功!");
        }
    }

    public static class T2 extends Thread {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + "," + this.getName() + "准备获取锁!");
            synchronized (lock) {
                System.out.println(System.currentTimeMillis() + "," + this.getName() + "获取锁成功!");
                lock.notify(); // t2获取到lock的锁之后，调用了notify()方法，唤醒t1线程，但t1并不能立即被唤醒，因为同步块内仍然占用着lock对象。需要等到t2将synchronized块执行完毕，释放锁之后，t1才被唤醒
                System.out.println(System.currentTimeMillis() + "," + this.getName() + " notify!");
                try {
                    TimeUnit.SECONDS.sleep(5); // t2休眠5秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + "," + this.getName() + "准备释放锁!");
            }
            System.out.println(System.currentTimeMillis() + "," + this.getName() + "释放锁成功!"); // 必须等到t2释放锁之后，t1才能被唤醒
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
