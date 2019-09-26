package learnbigdata;

import java.util.Random;

public class SynchronizedTest2 {
    public static void main(String[] args)
            throws Exception {
//        pubClasst1 tt2 = new pubClasst1();
        Thread t1 = new Thread(new pubClasst1("pubClasst1"));
        Thread t2 = new Thread(new pubClasst1("pubClasst22222"));
        t1.start();
        System.out.println(" t1.start();");
        t2.start();
        System.out.println("t2.start();");
        //t1.join();///也就是说，t.join()方法阻塞调用此方法的线程(calling thread)进入 TIMED_WAITING 状态，直到线程t完成，此线程再继续；
        System.out.println("Thread.sleep(50000);");
        Thread.sleep(50000);

//        t1.join();
        //System.out.println(pubClasst1.i);
    }

    public void putgetName2() {
        //System.out.print(SynchronizedTest2.class.getName());
    }
}

class pubClasst1 implements Runnable {
    int i = 0;
    static boolean flag = true;
    public String marker;
    // 作用于静态方法,锁是当前class对象,也就是AccountingSyncClass类对应的class对象
    public  static synchronized void increase(String argmarker,Integer argi) throws InterruptedException {
        System.out.println(argmarker+"_Start_i="+argi);
        Random r = new Random();
        int randomIntInRealm = r.nextInt(1000) + 1;
        //System.out.println("randomIntInRealm="+randomIntInRealm);
        Thread.sleep(randomIntInRealm);    //延时2秒
        System.out.println(argmarker+"_Finished_i="+argi);
    }

    public pubClasst1(String argpubDev){
        this.marker=argpubDev;
    }

    // 非静态方法，锁定当前实例对象
    public synchronized void increase2() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 10; j++) {
            try {
                increase(marker,j);

            }
            catch (Exception e) {

            }


            //increase2();
        }
    }
}

