package day7_Condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RoundPrint {

    ReentrantLock lock = new ReentrantLock();

    Condition condition = lock.newCondition();

    static int num = 1;

    public static void main(String[] args) {

    }

}
