package com.hdsxtech.www.test_exercise;

/**
 * 作者:丁文 on 2017/10/17.
 * copyright: www.tpri.org.cn
 */

public class Ticket implements Runnable {
    private int num = 100;

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(10);

                synchronized (this) {
                    if (num > 0) {
                        System.out.print(Thread.currentThread().getName() + "还剩" + num + "张票");
                        num--;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
