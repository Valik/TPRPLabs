package com.mycompany.mavenproject1.mylocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: Valik
 * Date: 08.05.13
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public class MyReentrantLock implements ILock
{
    private Sync mSync;

    abstract class Sync
    {
        public abstract void lock() throws InterruptedException;
        public abstract void unlock();
        public abstract boolean tryLock() throws InterruptedException;
    }

    class NonFairSync extends Sync
    {
        private boolean mIsLocked = false;
        private Thread mLockedBy = null;
        private int mLockedCount = 0;
        private Object mLockObject = new Object();

        @Override
        public synchronized void lock() throws InterruptedException
        {
            Thread callingThread = Thread.currentThread();
            while (mIsLocked && mLockedBy != callingThread)
            {
                 wait();
            }
            mIsLocked = true;
            mLockedCount++;
            mLockedBy = callingThread;
        }

        @Override
        public synchronized void unlock()
        {
            if(Thread.currentThread() == this.mLockedBy)
            {
                mLockedCount--;

                if(mLockedCount == 0)
                {
                    mIsLocked = false;
                    notify();
                }
            }
        }

        @Override
        public synchronized boolean tryLock() throws InterruptedException
        {
            if(!mIsLocked || mLockedBy == Thread.currentThread())
            {
                lock();
                return true;
            }
            return false;
        }
    }

    class FairSync extends Sync
    {
        private boolean mIsLocked = false;
        private Thread mLockingThread = null;
        private List<QueueObject> mWaitingThreads = new ArrayList<QueueObject>();

        @Override
        public synchronized void lock() throws InterruptedException
        {
            QueueObject queueObject = new QueueObject();
            boolean isLockedForThisThread = true;

            mWaitingThreads.add(queueObject);

            while(isLockedForThisThread)
            {
                isLockedForThisThread =
                    mIsLocked || mWaitingThreads.get(0) != queueObject;
                if(!isLockedForThisThread)
                {
                    mIsLocked = true;
                    mWaitingThreads.remove(queueObject);
                    mLockingThread = Thread.currentThread();
                    return;
                }
                try
                {
                    queueObject.doWait();
                }
                catch(InterruptedException e)
                {
                    synchronized(this) { mWaitingThreads.remove(queueObject); }
                    throw e;
                }
            }
        }

        @Override
        public synchronized void unlock()
        {
            if(this.mLockingThread != Thread.currentThread())
            {
                return;
            }
            mIsLocked = false;
            mLockingThread = null;
            if(mWaitingThreads.size() > 0)
            {
                for(QueueObject curOb : mWaitingThreads)
                    curOb.doNotify();
            }
        }

        @Override
        public boolean tryLock() throws InterruptedException
        {
            if(!mIsLocked)
            {
                lock();
                return true;
            }
            return false;
        }
    }

    public MyReentrantLock()
    {
        mSync = new NonFairSync();
    }


    public MyReentrantLock(boolean fair)// если true, создается fair lock, иначе просто reentrant lock
    {
       if(fair)
           mSync = new FairSync();
       else
           mSync = new NonFairSync();
    }

    public synchronized void lock() throws InterruptedException
    {
        mSync.lock();
    }

    public synchronized void unlock()
    {
            mSync.unlock();
    }

    public synchronized boolean tryLock() throws InterruptedException
    {
        return mSync.tryLock();
    }
}
