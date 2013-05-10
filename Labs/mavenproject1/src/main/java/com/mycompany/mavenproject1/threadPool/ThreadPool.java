package com.mycompany.mavenproject1.threadPool;

import java.util.LinkedList;
/**
 * Created with IntelliJ IDEA.
 * User: Valik
 * Date: 09.05.13
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */
public class ThreadPool
{
    private final int mNumThreads;
    private final PoolWorker[] mThreads;
    private final LinkedList mQueue;

    public ThreadPool(int nThreads)
    {
        this.mNumThreads = nThreads;
        mQueue = new LinkedList();
        mThreads = new PoolWorker[nThreads];

        for (int i=0; i<nThreads; i++) // Запускаем потоки
        {
            mThreads[i] = new PoolWorker();
            mThreads[i].start();
        }
    }

    public void execute(Runnable r)
    {
        synchronized(mQueue)
        {
            mQueue.addLast(r);
            mQueue.notify();
        }
    }

    private class PoolWorker extends Thread
    {
        public void run()
        {
            Runnable r;

            while (true)
            {
                synchronized(mQueue)
                {
                    while (mQueue.isEmpty())// Ждем, пока в очереди не появится задачи для выполнения
                    {
                        try
                        {
                            mQueue.wait();
                        }
                        catch (InterruptedException ignored)
                        {
                        }
                    }

                    r = (Runnable) mQueue.removeFirst();// Выполняем задачу
                }

                try
                {
                    r.run();
                }
                catch (RuntimeException e)
                {}
            }
        }
    }
}
