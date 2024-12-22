package day7_Condition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 微信公众号：路人甲Java，专注于java技术分享（带你玩转 爬虫、分布式事务、异步消息服务、任务调度、分库分表、大数据等），喜欢请关注！
 */

// 同一个锁支持创建多个Condition 使用两个Condition来实现一个阻塞队列的例子：
public class BlockingQueueDemo<E> {
    int size;//阻塞队列最大容量

    ReentrantLock lock = new ReentrantLock();

    LinkedList<E> list = new LinkedList<>();// 队列底层实现

    Condition notFull = lock.newCondition();// 队列满时的等待条件
    Condition notEmpty = lock.newCondition();// 队列空时的等待条件

    public BlockingQueueDemo(int size) {
        this.size = size;
    }

    public void enqueue(E e) throws InterruptedException {
        lock.lock();
        try {
            while (list.size() == size) // 队列已满,在notFull条件上等待
                notFull.await(); // 释放锁，等待非空条件
            list.add(e); // 入队:加入链表末尾
            System.out.println("入队：" + e);
            notEmpty.signal(); // 通知在notEmpty条件上等待的线程 只要有元素入队，就一定非空，换醒等待非空的线程
        } finally {
            lock.unlock(); // 释放锁 为了保证一次只有一个线程在操作队列
        }
    }

    public E dequeue() throws InterruptedException {
        E e;
        lock.lock();
        try {
            while (list.size() == 0) // 队列为空,在notEmpty条件上等待
                notEmpty.await();
            e = list.removeFirst(); // 出队:移除链表首元素
            System.out.println("出队：" + e);
            notFull.signal(); // 通知在notFull条件上等待的线程
            return e;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        BlockingQueueDemo<Integer> queue = new BlockingQueueDemo<>(2);
        // 10个生产者线程都会尝试入队，但是队列大小为2，所以只有2个线程能入队，其他的都在等NotFull条件
        for (int i = 0; i < 10; i++) {
            int data = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.enqueue(data);
                    } catch (InterruptedException e) {

                    }
                }
            }).start();
        }
        // 消费者线程 同样的，10次循环一瞬间跑完，真正能消费的只有2个线程
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Integer data = queue.dequeue();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }






}

