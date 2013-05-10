package com.mycompany.mavenproject1.mylocks;

/**
 * Created with IntelliJ IDEA.
 * User: Valik
 * Date: 08.05.13
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public class MyLock implements ILock
{
    private boolean mIsLocked = false;

    public synchronized void lock() throws InterruptedException
    {
        while(mIsLocked)
        {
            wait();
        }
        mIsLocked = true;
    }

    public synchronized void unlock()
    {
        mIsLocked = false;
        notify();
    }

    public synchronized boolean tryLock() throws InterruptedException
    {
        if(!mIsLocked)
        {
            lock();
            return true;
        }
        return false;
    }
}
