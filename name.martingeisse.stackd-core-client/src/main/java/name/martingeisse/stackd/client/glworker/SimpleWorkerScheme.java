/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.glworker;

import java.util.concurrent.BlockingQueue;

/**
 * Implements a simple, globally accessible worker thread scheme
 * that uses a single OpenGL thread.
 */
public final class SimpleWorkerScheme {

	/**
	 * the glWorkerLoop
	 */
	private static GlWorkerLoop glWorkerLoop;
	
	/**
	 * Initializes the "simple worker" scheme.
	 */
	public synchronized static void initialize() {
		glWorkerLoop = new GlWorkerLoop();
	}
	
	/**
	 * Getter method for the OpenGL worker loop.
	 * @return the OpenGL worker loop
	 */
	public static GlWorkerLoop getGlWorkerLoop() {
		return glWorkerLoop;
	}

	/**
	 * Getter method for the OpenGL work queue.
	 * @return the OpenGL work queue
	 */
	public static BlockingQueue<GlWorkUnit> getGlWorkQueue() {
		return glWorkerLoop.getQueue();
	}
	
	/**
	 * Requests execution of a work unit.
	 * @param workUnit the work unit
	 */
	public static void requestExecution(GlWorkUnit workUnit) {
		glWorkerLoop.getQueue().add(workUnit);
	}
	
	/**
	 * Requests stopping the queue.
	 */
	public static void requestStop() {
		glWorkerLoop.scheduleStop();
	}

	/**
	 * Executes work units until the queue is empty, then returns.
	 * This method must only be called by the OpenGL thread.
	 */
	public static void workUntilEmpty() {
		glWorkerLoop.workUntilEmpty();
	}
	
	/**
	 * Executes work units, waiting for new ones when the queue becomes empty.
	 * 
	 * @throws InterruptedException if interrupted while waiting
	 */
	public static void workAndWait() throws InterruptedException {
		glWorkerLoop.workAndWait();
	}

	/**
	 * Adds a frame boundary "work unit".
	 */
	public static void addFrameBoundary() {
		glWorkerLoop.scheduleFrameBoundary();
	}

	/**
	 * Getter method for the workload.
	 * @return the workload
	 */
	public static int getWorkload() {
		return glWorkerLoop.getWorkload();
	}

	/**
	 * Getter method for the frameSkipThreshold.
	 * @return the frameSkipThreshold
	 */
	public static int getFrameSkipThreshold() {
		return glWorkerLoop.getFrameSkipThreshold();
	}
	
	/**
	 * Setter method for the frameSkipThreshold.
	 * @param frameSkipThreshold the frameSkipThreshold to set
	 */
	public static void setFrameSkipThreshold(int frameSkipThreshold) {
		glWorkerLoop.setFrameSkipThreshold(frameSkipThreshold);
	}
	
	/**
	 * 
	 */
	public static void addBeginSideEffectsMarker() {
		glWorkerLoop.scheduleBeginSideEffectsMarker();
	}
	
	/**
	 * 
	 */
	public static void addEndSideEffectsMarker() {
		glWorkerLoop.scheduleEndSideEffectsMarker();
	}
	
	/**
	 * Prevent instantiation.
	 */
	private SimpleWorkerScheme() {
	}
	
}
