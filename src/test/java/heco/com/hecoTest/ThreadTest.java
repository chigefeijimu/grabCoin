package heco.com.hecoTest;

import org.junit.Test;

import java.lang.reflect.Method;

public class ThreadTest {

    //单例内部类
    private static class Method1{
        private static volatile Method1 instance;
        private Method1(){}

        public static Method1 getInstance(){
            if(instance == null){
                synchronized (Method1.class){
                    if(instance == null){
                        instance = new Method1();
                    }
                }
            }
            return instance;
        }

        private long count;
        //一个循环count+10000
        private void test(){
            int idx = 0;
            while(idx++ < 10000){
                count += 1;
            }
        }
    }

    @Test
    public void testCount() throws InterruptedException {
        final Method1 method1 = Method1.getInstance();
        Thread thread1 = new Thread(method1::test);
        Thread thread2 = new Thread(method1::test);
        //开始线程
        thread1.start();
        thread2.start();
        //等待线程执行结束
        thread1.join();
        thread2.join();

        System.out.println(method1.count);

    }
}
