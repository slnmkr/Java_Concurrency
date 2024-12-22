package day4_volatile;

// volatile解决了共享变量在多线程中可见性的问题，可见性是指一个线程对共享变量的修改，对于另一个线程来说是否是可以看到的。
public class volatileAndJMM {
    public /* volatile */ static boolean flag = true;
    // 是否有这样的方法：线程中修改了工作内存中的副本之后，立即将其刷新到主内存；工作内存中每次读取共享变量时，都去主内存中重新读取，然后拷贝到工作内存。
    // java帮我们提供了这样的方法，使用volatile修饰共享变量，就可以达到上面的效果，被volatile修改的变量有以下特点：
    // 线程中读取的时候，每次读取都会去主内存中读取共享变量最新的值，然后将其复制到工作内存
    // 线程中修改了工作内存中变量的副本，修改之后会立即刷新到主内存
    public static class T1 extends Thread {
        public T1(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println("线程" + this.getName() + " in");
            while (flag) {
                ;
            }
            System.out.println("线程" + this.getName() + "停止了");
        }
    }

    // 运行下代码，会发现程序无法终止。
    // 主线程中休眠了1秒，将flag置为false，按说此时线程t1会检测到flag为false，打印“线程t1停止了”
    // JMM定义了线程和主内存之间的抽象关系：线程之间的共享变量存储在主内存（main memory）中，每个线程都有一个私有的本地内存（local memory）
    // 本地内存中存储了该线程以读/写共享变量的副本（也就是thread1有自己的flag对象副本）
    public static void main(String[] args) throws InterruptedException {
        new T1("t1").start();
        //休眠1秒
        Thread.sleep(1000);
        //将flag置为false
        flag = false;

        // 因此，从JMM的角度，看不到的原因可能有二：
        // 主线程修改了flag之后，未将其刷新到主内存，所以t1看不到
        // 主线程将flag刷新到了主内存，但是t1一直读取的是自己工作内存中flag的值，没有去主内存中获取flag最新的
    }
}
