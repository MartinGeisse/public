//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.util.thread;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;


public class QueuedThreadPoolTest
{
    final AtomicInteger _jobs=new AtomicInteger();
    
    class RunningJob implements Runnable
    {
        private final CountDownLatch _run = new CountDownLatch(1);
        private final CountDownLatch _stopping = new CountDownLatch(1);
        private final CountDownLatch _stopped = new CountDownLatch(1);
        public void run()
        {
            try 
            {
                _run.countDown();
                _stopping.await();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                _jobs.incrementAndGet();
                _stopped.countDown();
            }
        }
        
        public void stop() throws InterruptedException
        {
            _run.await(10,TimeUnit.SECONDS);
            _stopping.countDown();
            if (!_stopped.await(10,TimeUnit.SECONDS))
                throw new IllegalStateException(); 
        }
    };   
    
    
    @Test
    public void testThreadPool() throws Exception
    {        
        QueuedThreadPool tp= new QueuedThreadPool();
        tp.setMinThreads(5);
        tp.setMaxThreads(10);
        tp.setMaxIdleTimeMs(1000);
        tp.setThreadsPriority(Thread.NORM_PRIORITY-1);

        tp.start();
        
        waitForThreads(tp,5);
        waitForIdle(tp,5);
        
        Thread.sleep(1000);
        waitForThreads(tp,5);
        waitForIdle(tp,5);
        
        RunningJob job=new RunningJob();
        tp.dispatch(job);
        waitForIdle(tp,4);
        waitForThreads(tp,5);

        job.stop();
        waitForIdle(tp,5);
        waitForThreads(tp,5);
        
        Thread.sleep(200);
        waitForIdle(tp,5);
        waitForThreads(tp,5);

        RunningJob[] jobs = new RunningJob[5];
        for (int i=0;i<jobs.length;i++)
        {
            jobs[i]=new RunningJob();
            tp.dispatch(jobs[i]);
        }
        waitForIdle(tp,0);
        waitForThreads(tp,5);
        
        job=new RunningJob();
        tp.dispatch(job);
        waitForThreads(tp,6);
        
        job.stop();
        waitForThreads(tp,5);
        
        jobs[0].stop();
        waitForIdle(tp,1);
        waitForThreads(tp,5);
        
        for (int i=1;i<jobs.length;i++)
            jobs[i].stop();

        waitForIdle(tp,5);
        waitForThreads(tp,5);
        
        jobs = new RunningJob[15];
        for (int i=0;i<jobs.length;i++)
        {
            jobs[i]=new RunningJob();
            tp.dispatch(jobs[i]);
        }
        waitForIdle(tp,0);
        waitForThreads(tp,10);
        for (int i=0;i<9;i++)
            jobs[i].stop();
        waitForThreads(tp,9);
        
        for (int i=9;i<jobs.length;i++)
            jobs[i].stop();
        waitForIdle(tp,5);
        tp.stop();
    }

    @Test
    public void testShrink() throws Exception
    {
        final AtomicInteger sleep = new AtomicInteger(100);
        Runnable job = new Runnable()
        {
            public void run()
            {
                try 
                {
                    Thread.sleep(sleep.get());
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            
        };
        
        QueuedThreadPool tp= new QueuedThreadPool();
        tp.setMinThreads(2);
        tp.setMaxThreads(10);
        tp.setMaxIdleTimeMs(400);
        tp.setThreadsPriority(Thread.NORM_PRIORITY-1);
        
        tp.start();
        waitForIdle(tp,2);
        waitForThreads(tp,2);
        
        sleep.set(200);
        tp.dispatch(job);
        tp.dispatch(job);
        for (int i=0;i<20;i++)
            tp.dispatch(job);

        waitForThreads(tp,10);
        waitForIdle(tp,0);
        
        sleep.set(5);
        for (int i=0;i<500;i++)
        {
            tp.dispatch(job);
            Thread.sleep(10);
        }
        waitForThreads(tp,2);
        waitForIdle(tp,2);
    }

    @Test
    public void testMaxStopTime() throws Exception
    {
        QueuedThreadPool tp= new QueuedThreadPool();
        tp.setMaxStopTimeMs(500);
        tp.start();
        tp.dispatch(new Runnable(){
            public void run () {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ie) {}
                }
            }
        });

        long beforeStop = System.currentTimeMillis();
        tp.stop();
        long afterStop = System.currentTimeMillis();
        assertTrue(tp.isStopped());
        assertTrue(afterStop - beforeStop < 1000);
    }


    private void waitForIdle(QueuedThreadPool tp, int idle)
    {
        long now=System.currentTimeMillis();
        long start=now;
        while (tp.getIdleThreads()!=idle && (now-start)<10000)
        {
            try
            {
                Thread.sleep(10);
            }
            catch(InterruptedException e)
            {}
        }
        Assert.assertEquals(idle,tp.getIdleThreads());
    }

    private void waitForThreads(QueuedThreadPool tp, int threads)
    {
        long now=System.currentTimeMillis();
        long start=now;
        while (tp.getThreads()!=threads && (now-start)<10000)
        {
            try
            {
                Thread.sleep(10);
            }
            catch(InterruptedException e)
            {} 
            now=System.currentTimeMillis();
        }
        Assert.assertEquals(threads,tp.getThreads());
    }

}
