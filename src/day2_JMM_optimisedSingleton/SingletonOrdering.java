package day2_JMM_optimisedSingleton;

public class SingletonOrdering {

    static SingletonOrdering instance;

    /*
        我们先看instance = new Singleton();
            未被编译器优化的操作：
                指令1：分配一款内存M
                指令2：在内存M上初始化Singleton对象
                指令3：将M的地址赋值给instance变量
            编译器优化后的操作指令：
                指令1：分配一块内存S
                指令2：将M的地址赋值给instance变量
                指令3：在内存M上初始化Singleton对象
    如果线程A，B均被优化，线程A执行完instance=&M后（尚未初始化M）切换到线程B，
     线程B发现instance不为null，直接返回instance，但此时instance
     并未初始化完成，会导致空指针异常。
    * */
    static SingletonOrdering getInstance(){
        if (instance == null) {
            synchronized(SingletonOrdering.class) {
                if (instance == null)
                    instance = new SingletonOrdering();
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        // 用十个线程依次调用 getInstance 方法 一旦instance为null就抛出异常
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                SingletonOrdering.getInstance();
            }).start();
        }
    }


}
