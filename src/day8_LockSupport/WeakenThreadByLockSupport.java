package day8_LockSupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class WeakenThreadByLockSupport {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);  // t1休眠5秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + " start!");
            LockSupport.park(); // 调用park()方法后，线程t1本应进入等待状态，但在main中的unpark方法以及获得了一张许可证，所以t1不会进入等待状态，而是立即被唤醒
//            park 方法是先检查是否有许可证：（这也是和notify wait最大的区别之一）
//            如果有许可证（如 unpark 已调用），直接返回，不阻塞。
            System.out.println(System.currentTimeMillis() + "," + Thread.currentThread().getName() + " 被唤醒!");
        });
        t1.setName("t1");
        t1.start();
        //休眠1秒
        TimeUnit.SECONDS.sleep(1); // main休眠1秒
        LockSupport.unpark(t1); // 休眠1秒之后，唤醒t1线程 此时t1其实还未等待， 即使 t1 此时尚未进入 park，调用 unpark 仍然有效。
        // unpark 会给 t1 一个“许可证”，这张许可证允许 t1 在未来调用 park 时立即返回，而不会进入等待状态
        System.out.println(System.currentTimeMillis() + ",LockSupport.unpark();执行完毕");
    }
}

//        3种让线程等待和唤醒的方法
//
//        方式1：Object中的wait、notify、notifyAll方法
//        方式2：juc中Condition接口提供的await、signal、signalAll方法
//        方式3：juc中的LockSupport提供的park、unpark方法

