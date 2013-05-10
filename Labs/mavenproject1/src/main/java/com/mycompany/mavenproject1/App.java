package com.mycompany.mavenproject1;

import com.mycompany.mavenproject1.mylocks.ILock;
import com.mycompany.mavenproject1.mylocks.MyLock;
import com.mycompany.mavenproject1.mylocks.MyReentrantLock;
import com.mycompany.mavenproject1.threadPool.ThreadPool;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ThreadPool myQueue = new ThreadPool(5);

        for(int i = 0; i < 10; i++)
        {
            final int taskNum = i;
            myQueue.execute(new Runnable() {
                @Override
                public void run()
                {
                    System.out.println("Task" + taskNum);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            });
        }

    }
}