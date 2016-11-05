package com.example.concurrency.threads;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TestThread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Entering main()");

		final Boolean deamonThread = new Boolean(args.length > 0);
		// Using thread
		UserOrDeamonThread thread = new UserOrDeamonThread(deamonThread);
		thread.start();

		// Using Runnable
		UserOrDaemonRunnable runnable = new UserOrDaemonRunnable(
				deamonThread.booleanValue() ? "daemon" : "user");
		Thread thread1 = new Thread(runnable);
		thread1.setDaemon(deamonThread.booleanValue());
		thread1.start();
		// Using worker thread
		final int POOL_SIZE = 2;

		final ThreadFactory threadFactory = new ThreadFactory() {

			public Thread newThread(Runnable runnable) {
				Thread thr = new Thread(runnable);
				if (deamonThread.booleanValue()) {
					thr.setDaemon(true);
				}
				return thr;
			}
		};

		final Executor executor = Executors.newFixedThreadPool(POOL_SIZE,
				threadFactory);

		for (int i = 0; i < POOL_SIZE; i++) {
			executor.execute(runnable);
		}

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Exiting main()");
	}

}
