package day9_Semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 微信公众号：路人甲Java，专注于java技术分享（带你玩转 爬虫、分布式事务、异步消息服务、任务调度、分库分表、大数据等），喜欢请关注！
 */
public class Demo {
    static Semaphore semaphore = new Semaphore(2); // 信号量的许可证数量为2

    public static class T extends Thread {
        public T(String name) {
            super(name);
        }

        @Override
        public void run() {
            Thread thread = Thread.currentThread();
            try {
                semaphore.acquire(); // 获取许可
                System.out.println(System.currentTimeMillis() + "," + thread.getName() + ",获取许可!");
                TimeUnit.SECONDS.sleep(3); // 休眠3秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(); // 释放许可 这里其实有坑 finally是一定会执行的，但是如果semaphore.acquire();之后抛出异常，那么semaphore.release();就不会执行，导致许可证没有释放
                // 又或是acquire()失败，导致没有许可证被占用，但还是要释放一个
                System.out.println(System.currentTimeMillis() + "," + thread.getName() + ",释放许可!");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) { // 创建10个线程
            new T("t-" + i).start();
        }
    }
}
