package day8_LockSupport;

import java.util.concurrent.TimeUnit;

public class WeakenThread {
    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + " start!");
                try {
                    lock.wait(); // 调用wait()方法后，线程t1会释放lock的锁，进入等待状态
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + " 被唤醒!");
            }
        });

        t1.setName("t1");
        t1.start();
        //休眠5秒
        TimeUnit.SECONDS.sleep(5);
        synchronized (lock) {
            lock.notify(); // 休眠5秒之后，唤醒t1线程
        }
    }
}
