package day3_Thread;

// 继承Runnable接口，支持多继承等
public class RunnableInterface {
    public static class testRunnable implements Runnable{
        private int count = 15;
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                // 三个不同的线程之间的变量是共享的，每次count--得到的结果都是再上一个线程运行结果之上得到的。
                System.out.println(Thread.currentThread().getName() + "运行  count= " + count--);
                try {
                    Thread.sleep((int) Math.random() * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args) {
        testRunnable mTh = new testRunnable(); // 继承了Runnable接口的类的实例化对象，而不是继承Thread类
        // 这里调用的就是Thread类有一个非常重要的构造方法：public Thread(Runnable target, String name)
        new Thread(mTh, "C").start(); // 同一个mTh，但是在Thread中就不可以，如果用同一个实例化对象mt，就会出现异常
        new Thread(mTh, "D").start();
        new Thread(mTh, "E").start();
    }
}

