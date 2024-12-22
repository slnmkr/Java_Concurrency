package day7_Condition;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 微信公众号：路人甲Java，专注于java技术分享（带你玩转 爬虫、分布式事务、异步消息服务、任务调度、分库分表、大数据等），喜欢请关注！
 */
public class AwaitBeenInterrupted {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static class T1 extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                condition.await(); // 线程进入阻塞中，然后主线程沉睡2秒醒来执行t1.interrupt()，await()方法内部会检测到线程中断信号，触发异常
            } catch (InterruptedException e) {
                System.out.println("中断标志：" + this.isInterrupted()); // 触发异常后，中断标志会被清空
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
        TimeUnit.SECONDS.sleep(2);
        //给t1线程发送中断信号
        System.out.println("1、t1中断标志：" + t1.isInterrupted());
        t1.interrupt();
        System.out.println("2、t1中断标志：" + t1.isInterrupted());
    }
}
// 下面的输出顺序值得思考：

//1、t1中断标志：false （主线程2秒后醒来，此时已经t1已经在进入阻塞等待condition）
//2、t1中断标志：true  （主线程执行t1.interrupt()，t1的中断位被标记为true）
//中断标志：false      （但进入异常处理块，中断标志被清空）