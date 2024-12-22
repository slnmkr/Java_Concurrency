package day8_LockSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WeakenThreadByCondition {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + " start!");
                try {
                    condition.await(); // 调用await()方法后，线程t1会释放lock的锁，进入等待状态
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + " 被唤醒!");
            } finally {
                lock.unlock();
            }
        });
        t1.setName("t1");
        t1.start();
        //休眠5秒
        TimeUnit.SECONDS.sleep(5);
        lock.lock();
        try {
            condition.signal();  // 休眠5秒之后，唤醒t1线程
        } finally {
            lock.unlock();
        }

    }
}

//     关于Object和Condtion中线程等待和唤醒的局限性，有以下几点：
//
//      2中方式中的让线程等待和唤醒的方法能够执行的先决条件是：线程需要先获取锁
//      唤醒方法需要在等待方法之后调用，线程才能够被唤醒
//      关于这2点，LockSupport都不需要，就能实现线程的等待和唤醒。