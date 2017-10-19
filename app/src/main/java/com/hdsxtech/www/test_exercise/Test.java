package com.hdsxtech.www.test_exercise;

import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 作者:丁文 on 2017/10/16.
 * copyright: www.tpri.org.cn
 */

public class Test {
    public static void find() {
        /**
         * 1 5 10
         * 2 6 12
         * 5 9 20
         */
    }

    static int[] ints = {1, 5, 59, 84, 2, 4, 6, 32, 848, 2, 6, 54, 12, 5, 4,};

    public static void main(String arg[]) {
//        stactTest();
        findArray0();
//        System.out.print(getHashCode("call"));
//        sort(ints);
//        leguansuo();
//        threadTest();
//        futureTask();
//        threadTest_ticket();

    }

    private static void threadTest_ticket() {
        Ticket t = new Ticket();
        Ticket t1 = new Ticket();
        Ticket t2 = new Ticket();
        Thread thread1 = new Thread(t);
        Thread thread2 = new Thread(t);
        Thread thread3 = new Thread(t);
        Thread thread4 = new Thread(t);
        Thread thread5 = new Thread(t1);
        Thread thread6 = new Thread(t2);
        Thread thread7 = new Thread(t1);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
    }


    private static void futureTask() {
        CallableThreadTest callableThreadTest = new CallableThreadTest();
        FutureTask<Integer> ft = new FutureTask<Integer>(callableThreadTest);
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " 的循环变量i的值" + i);
            if (i == 20) {
                new Thread(ft, "有返回值的线程").start();
            }
        }
        try {
            System.out.println("子线程的返回值：" + ft.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static void threadTest() {
        final HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.print(System.currentTimeMillis() + "\n");
                    synchronized ("") {
                        map.put(UUID.randomUUID().toString(), "第" + finalI + "个");
                        System.out.print(UUID.randomUUID().toString() + "第" + finalI + "个" + Thread.currentThread().getName() + "\n");
                        System.out.print(System.currentTimeMillis());
                    }
                }
            }).start();
        }
        System.out.print(System.currentTimeMillis() + "\n");
        synchronized ("a") {
            map.put(UUID.randomUUID().toString(), "第" + 101 + "个");
            System.out.print(UUID.randomUUID().toString() + "第" + 101 + "个" + "\n");
            System.out.print(System.currentTimeMillis());
        }
    }

    private static void leguansuo() {
        Runnable run = new CASCount();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
        new Thread(run).start();
    }

    private static void sort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int tem = 0;
            for (int j = i + 1; j < a.length; j++) {
                if (a[i] > a[j]) {
                    tem = a[j];
                    a[j] = a[i];
                    a[i] = tem;
                }
            }
        }
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + ",");
        }

    }

    private static void findArray0() {
        boolean findIt = findArray(Test.array, c);
        if (findIt) {
            System.out.print("找到了");
        } else {
            System.out.print("没有");
        }
        System.out.print("hello java ");
    }

    public static int getHashCode(String str) {
        char[] s = str.toCharArray();
        int hash = 0;
        for (int i = 0; i < s.length; i++) {
            hash = s[i] + (31 * hash);
        }
        return hash;
    }

    private static void stactTest() {
        Stack<String> stack = new Stack<String>();
        stack.add("China");
        stack.add("Japan");
        stack.add("Russia");
        stack.add("England");

        System.out.println(stack.size() + "<=" + stack.capacity());
        System.out.println(stack.elementAt(0));
        System.out.println(stack.get(0));
        System.out.println(stack.peek());
        System.out.println(stack.push("France"));
        System.out.println(stack.pop());
        System.out.println(stack.iterator());
        System.out.println(stack.empty());
        System.out.println(stack.isEmpty());
        System.out.println(stack.search("Russia"));
    }

    public static int[][] array = {{1, 5, 10}, {2, 6, 12}, {5, 9, 20}};
    public static int a = 9;
    public static int b = 20;
    public static int c = 14;

    public static boolean findArray(int[][] array, int num) {
        if (array == null || array.length < 1 || array[0].length < 1) {
            return false;
        }
        int rows = array.length;
        int cols = array[0].length;


        int row = 0;
        int col = cols - 1;

        while (row >= 0 && row < rows && col >= 0 && col < cols) {
            if (array[row][col] == num) {
                return true;
            } else if (array[row][col] < num) {
                row++;
            } else {
                col--;
            }
        }

        return false;
    }

    static class CASCount implements Runnable {
        SimilatedCAS counter = new SimilatedCAS();

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(this.increment());
            }
        }

        public int increment() {
            int oldValue = counter.getValue();
            int newValue = oldValue + 1;

            while (!counter.compareAndSwap(oldValue, newValue)) { //如果CAS失败,就去拿新值继续执行CAS
                oldValue = counter.getValue();
                newValue = oldValue + 1;
            }

            return newValue;
        }
    }

    static class SimilatedCAS {
        private int value;

        public int getValue() {
            return value;
        }

        // 这里只能用synchronized了,毕竟无法调用操作系统的CAS
        public synchronized boolean compareAndSwap(int expectedValue, int newValue) {
            if (value == expectedValue) {
                value = newValue;
                return true;
            }
            return false;
        }
    }

    private static class CallableThreadTest implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            int i = 0;
            for (; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "第" + i + "个" + "\n");
            }
            return i;
        }
    }
}
