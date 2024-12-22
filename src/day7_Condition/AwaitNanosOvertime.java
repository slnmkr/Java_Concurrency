package day7_Condition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class AwaitNanosOvertime {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static class T1 extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                System.out.println(System.currentTimeMillis() + "," + this.getName() + ",start");
                // nanos 是 nanoseconds 的缩写，表示纳秒。TimeUnit.SECONDS.toNanos(5) 将 5 秒转换为纳秒。
                long r = condition.awaitNanos(TimeUnit.SECONDS.toNanos(5)); // t1调用await方法等待5秒超时返回
                System.out.println(r); // 返回结果为负数，表示超时之后返回的。
                System.out.println(System.currentTimeMillis() + "," + this.getName() + ",end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T1 t1 = new T1();
        t1.setName("t1");
        t1.start();
        //休眠1秒之后，唤醒t1线程
        TimeUnit.SECONDS.sleep(3);
        lock.lock();
        try {
            condition.signal(); // 在超时之前唤醒t1线程 就能返回正数
        } finally {
            lock.unlock();
        }
    }
}
