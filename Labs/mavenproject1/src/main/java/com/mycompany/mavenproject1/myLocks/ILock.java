package com.mycompany.mavenproject1.mylocks;

/**
 * Created with IntelliJ IDEA.
 * User: Valik
 * Date: 08.05.13
 * Time: 12:42
 * To change this template use File | Settings | File Templates.
 */
public interface ILock
{
    public void lock() throws InterruptedException;
    public void unlock();
    public boolean tryLock() throws InterruptedException;
}
