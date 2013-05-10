package com.mycompany.mavenproject1.mylocks;

/**
 * Created with IntelliJ IDEA.
 * User: Valik
 * Date: 08.05.13
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
class QueueObject
{
    private boolean mIsNotified = false;

    public synchronized void doWait() throws InterruptedException
    {
        while(!mIsNotified)
        {
            this.wait();
        }
        this.mIsNotified = false;
    }

    public synchronized void doNotify()
    {
        this.mIsNotified = true;
        this.notify();
    }

    public boolean equals(Object o)
    {
        return this == o;
    }
}
