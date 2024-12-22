package day6_ReentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 微信公众号：路人甲Java，专注于java技术分享（带你玩转 爬虫、分布式事务、异步消息服务、任务调度、分库分表、大数据等），喜欢请关注！
 */
public class MyReentrantLock {
    private static int num = 0;
    private static ReentrantLock lock = new ReentrantLock();

    private static void add() {
        lock.lock();
        // lock.lock(); // 可重入锁，加两次锁，需要解两次锁，依然正确输出
        try {
            num++;
        } finally {
            lock.unlock();
            //lock.unlock();
        }
    }
// synchronized 关键字实现方式
//    private static synchronized void add() {
//        num++;
//    }

    public static class T extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                MyReentrantLock.add();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T t1 = new T();
        T t2 = new T();
        T t3 = new T();

        t1.start();
        t2.start();
        t3.start();

        t1.join(); // join()方法的作用是等待线程对象销毁，这里就是等待三个线程执行完毕后再执行主线程中的输出num
        t2.join();
        t3.join();

        System.out.println(MyReentrantLock.num);
    }
}
