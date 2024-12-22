package day1_Lock;

import java.util.concurrent.*;


public class StarvationLock {
    // 饥饿现象体现在单线程执行器 single 的使用上。由于 single 是一个单线程执行器，
    // 它一次只能执行一个任务。如果有多个任务提交给这个执行器，后续的任务将不得不等待前面的任务完成。
    private static ExecutorService single = Executors.newSingleThreadExecutor();

    public static class AnotherCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("in AnotherCallable");
            return "annother success";
        }
    }

    // 由于 single 只能同时执行一个任务，MyCallable 的任务会等待 AnotherCallable 完成，
    // 而 AnotherCallable 也在等待 MyCallable 完成，从而导致任务饥饿。
    public static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("in MyCallable");
            Future<String> submit = single.submit(new AnotherCallable()); // 将一个新的任务提交给单线程执行器（single） 通过 Future 对象，可以在任务完成后（异步）获取其结果或检查其状态。
            return "success:" + submit.get();  // 线程池中工作线程在此处等待AnotherCallable的结果
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyCallable task = new MyCallable();
        Future<String> submit = single.submit(task); // 将任务提交给单线程执行器 后续的提交就会等待前面的任务完成
        System.out.println(submit.get()); // main线程在此处等待MyCallable结果
        System.out.println("over");
        single.shutdown();
    }
}