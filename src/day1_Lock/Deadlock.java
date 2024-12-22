package day1_Lock;

public class Deadlock {

    // 运行后点击dump threads（照相机jstack）按钮，可以看到线程死锁的情况
    public static void main(String[] args) {
        Obj1 obj1 = new Obj1();
        Obj2 obj2 = new Obj2();
        Thread thread1 = new Thread(new SynAddRunalbe(obj1, obj2, 1, 2, true));
        thread1.setName("thread1");
        thread1.start();
        Thread thread2 = new Thread(new SynAddRunalbe(obj1, obj2, 2, 1, false));
        thread2.setName("thread2");
        thread2.start();
    }

    /**
     * 线程死锁等待演示
     */
    public static class SynAddRunalbe implements Runnable {
        Obj1 obj1;
        Obj2 obj2;
        int a, b;
        boolean flag;

        public SynAddRunalbe(Obj1 obj1, Obj2 obj2, int a, int b, boolean flag) { // 构造函数 初始化一个runnable对象
            this.obj1 = obj1;
            this.obj2 = obj2;
            this.a = a;
            this.b = b;
            this.flag = flag;
        }

        @Override
        public void run() {
            try {
                if (flag) { // 根据flag决定顺序
                    synchronized (obj1) { // 请求obj1对象锁
                        Thread.sleep(100); // 休眠100ms，让线程2有足够的时间请求obj2对象锁
                        synchronized (obj2) { // 请求obj2对象锁
                            System.out.println(a + b); // 打印a+b的值
                        }
                    }
                } else {
                    synchronized (obj2) {
                        Thread.sleep(100);
                        synchronized (obj1) {
                            System.out.println(a + b);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Obj1 {
    }

    public static class Obj2 {
    }
}