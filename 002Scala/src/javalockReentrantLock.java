import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class javalockReentrantLock {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    public static void main(String[] args)  {
        final javalockReentrantLock javalocktest = new javalockReentrantLock();

        new Thread(){
            public void run() {
                for(int i=0;i<50;i++) {
                    javalocktest.insert(i,Thread.currentThread());
                }
            };
        }.start();

        new Thread(){
            public void run() {
                for(int i=0;i<50;i++) {
                    javalocktest.insert(i,Thread.currentThread());
                }
            };
        }.start();

        new Thread(){
            public void run() {
                for(int i=0;i<50;i++) {
                    javalocktest.insert(i,Thread.currentThread());
                }
            };
        }.start();
    }
/*
a、用在定时任务时，如果任务执行时间可能超过下次计划执行时间，确保该有状态任务只有一个正在执行，忽略重复触发。
b、用在界面交互时点击执行较长时间请求操作时，防止多次点击导致后台重复执行（忽略重复触发）。
* */
    public String insert(Integer intMarker,Thread thread) {
        Lock lock = new ReentrantLock();    //ReentrantLock，意思是“可重入锁”
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁"+intMarker);
            for(int i=0;i<50;i++) {
                Thread.sleep(20);    //延时2秒
                arrayList.add(i);
                System.out.println(thread.getName()+"_"+i+"——得到了锁"+intMarker);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            System.out.println(thread.getName()+"释放了锁"+intMarker);
            lock.unlock();
        }

        return thread.getName()+"返回锁"+intMarker;
    }
}
