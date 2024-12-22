package day3_Thread;

public class WaitAndNotify {
    static Object object = new Object();

    public static class T1 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + ":T1 start!");
                try {
                    System.out.println(System.currentTimeMillis() + ":T1 wait for object");
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + ":T1 end!");
            }
        }
    }

    public static class T2 extends Thread {
        @Override
        public void run() {
            synchronized (object) {
                System.out.println(System.currentTimeMillis() + ":T2 start，notify one thread! ");
                object.notify(); // T2调用notify方法之后，T1并不能立即继续执行，而是要等待T2释放objec投递锁之后，T1重新成功获取锁后，才能继续执行。
                System.out.println(System.currentTimeMillis() + ":T2 end!");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new T1().start();
        new T2().start();
    }
}