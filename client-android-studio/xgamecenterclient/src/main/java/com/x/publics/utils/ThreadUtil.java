package com.x.publics.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Thread UTIL
 * 
 
 * 
 */
public class ThreadUtil {
	private static ExecutorService executorService;

	/**
	 * InIt Executor
	 * 
	 * @return
	 */
	private static ExecutorService newInstance() {
		if (executorService == null) {
			executorService = Executors.newCachedThreadPool();
		}
		return executorService;
	}

	public static void start(Runnable runnable) {
		if (executorService == null) {
			executorService = newInstance();
		}
		executorService.submit(runnable);
	}

	/**
	 * 清除static
	 */
	public static void clearThreadsta() {
		if (executorService != null) {
			executorService.shutdown();
			executorService = null;
		}
	}
}
