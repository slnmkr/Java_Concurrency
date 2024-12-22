
package day6_ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;




public class InteruptableReentrantLock{
    private static ReentrantLock lock1 = new ReentrantLock(false);
    private static ReentrantLock lock2 = new ReentrantLock(false);

    public static class T extends Thread {
        int lock;

        public T(String name, int lock) {
            super(name); // 调用父类Thread的构造方法
            this.lock = lock;
        }

        @Override
        public void run() {
            try {
                if (this.lock == 1) {
                    lock1.lockInterruptibly(); // lockInterruptibly()方法是一个可以响应中断的锁申请动作
                    TimeUnit.SECONDS.sleep(1);
                    lock2.lockInterruptibly();
                } else {
                    lock2.lockInterruptibly();
                    TimeUnit.SECONDS.sleep(1);
                    lock1.lockInterruptibly();
                }
            } catch (InterruptedException e) {
                System.out.println("中断标志:" + this.isInterrupted());
                e.printStackTrace();
            } finally {
                if (lock1.isHeldByCurrentThread()) {
                    lock1.unlock();
                }
                if (lock2.isHeldByCurrentThread()) {
                    lock2.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T t1 = new T("t1", 1);
        T t2 = new T("t2", 2);

        t1.start();
        t2.start();

        TimeUnit.SECONDS.sleep(5);
        t2.interrupt();
    }
    // t2在31行一直获取不到lock1的锁，主线程中等待了5秒之后，t2线程调用了interrupt()方法，将线程的中断标志置为true，
    // 此时31行会触发InterruptedException异常，然后线程t2可以继续向下执行，释放了lock2的锁，然后线程t1可以正常获取锁，
    // 程序得以继续进行。线程发送中断信号触发InterruptedException异常之后，中断标志将被清空。
}
