package day5_threadSecurityAndSync;

public class ThreadSecurity {
    static int num = 0;

    public /* synchronized */static void m1() {
        for (int i = 0; i < 10000; i++) {
            num++;
        }
    }

    public static class T1 extends Thread {
        @Override
        public void run() {
            ThreadSecurity.m1(); // 需要保证同一时刻有且只有一个线程在操作共享数据，其他线程必须等到该线程处理完数据后再进行，互斥锁
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T1 t1 = new T1();
        T1 t2 = new T1();
        T1 t3 = new T1();
        t1.start();
        t2.start();
        t3.start();

        //等待3个线程结束打印num
        t1.join();
        t2.join();
        t3.join();

        System.out.println(ThreadSecurity.num);
        /**
         * 打印结果：
         * 有时是所预期的30000，有时不是，说明此程序不是线程安全的
         */
    }
}
