package day3_Thread;
// 这是继承Thread类的方式实现新建线程
// java只能单继承，因此如果是采用继承Thread的方法，那么在以后进行代码重构的时候可能会遇到问题，因为你无法继承别的类了。
// 其次，如果一个类继承Thread，则不适合资源共享。见下面代码
public class ExtendsThread {
    public static class testThread extends Thread{
        private int count = 5;
        private String name;
        public testThread(String name) {
            this.name=name;
        }
        public void run() {
            for (int i = 0; i < 5; i++) {
                System.out.println(name + "运行  count= " + count--);
                try {
                    sleep((int) Math.random() * 10); // 使当前线程暂停一段时间，让其他线程先跑 也就实现了交替AB线程输出
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void main(String[] args) {
        // mTh1 和 mTh2 是两个独立的线程，它们几乎同时启动。由于线程调度的不确定性，
        // 有时线程 mTh1 可能会先运行，有时线程 mTh2 可能会先运行。
        testThread mTh1= new testThread("A");
        testThread mTh2= new testThread("B");
        mTh1.start();
        mTh2.start();
    }
}
