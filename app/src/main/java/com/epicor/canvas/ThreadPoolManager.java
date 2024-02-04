package com.epicor.canvas;

import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Circle on 2019/9/17.
 * 线程池管理类
 */

public class ThreadPoolManager {
    private static final String TAG = ThreadPoolManager.class.getSimpleName();
    private static volatile ThreadPoolManager threadPoolManager = null;

    // 使用双重检查锁定实现单例模式
    public static ThreadPoolManager getInstance() {
        if (threadPoolManager == null) {
            synchronized (ThreadPoolManager.class) {
                if (threadPoolManager == null) {
                    threadPoolManager = new ThreadPoolManager();
                }
            }
        }
        return threadPoolManager;
    }

    private LinkedBlockingDeque<Runnable> mQueue = new LinkedBlockingDeque<>();

    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolManager() {
        mThreadPoolExecutor = new ThreadPoolExecutor(1, 100, 15, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(100), new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                try {
                    mQueue.put(r);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        new Thread(coreThread).start();
    }

    public void addTask(Runnable runnable) {

        if (runnable != null) {
            Log.d(TAG, "Adding task: " + runnable.getClass().getSimpleName());
            try {
                mQueue.put(runnable);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void addTopTask(Runnable runnable) {
        if (runnable != null) {
            try {
                mQueue.putFirst(runnable);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Runnable coreThread = new Runnable() {
        @Override
        public void run() {

            while (!Thread.currentThread().isInterrupted()) {
                Log.d(TAG, "coreThread: ");
                try {
                    Runnable runn = mQueue.take();
                    mThreadPoolExecutor.execute(runn);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    };

    public void stopThreadPool() {
        if (mThreadPoolExecutor != null) {
            mThreadPoolExecutor.shutdown();
            try {
                if (!mThreadPoolExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                    mThreadPoolExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                mThreadPoolExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            mQueue.clear();
            threadPoolManager = null;
        }
    }
}
