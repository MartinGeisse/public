/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.glworker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.log4j.Logger;

/**
 * This class must be used by the OpenGL worker thread to handle work units.
 * 
 * Clients should pass work units to the worker by calling {@link #schedule(GlWorkUnit)}.
 * 
 * The queue will stop when requested to do so by {@link #scheduleStop()}.
 * This schedules a special work unit that stops the loop when executed.
 * 
 * Workload and frame skipping: This class supports measuring the current
 * workload in frames, and also supports skipping frames when the workload
 * becomes too high. To do this, clients must invoke {@link #scheduleFrameBoundary()}
 * between frames. {@link #getWorkload()} then returns the number of such
 * markers in the queue. First of all, clients should use this value to
 * avoid inserting too many frames to render; even if they get skipped, this
 * places additional work on the worker thread and possibly on the GC
 * and also increases rendering latency.
 * 
 * In addition, clients can use {@link #setFrameSkipThreshold(int)} to set
 * the minimum workload to start skipping frames. When this threshold is
 * reached or exceeded at a frame boundary, this worker loop skips work units
 * until the next frame boundary, then checks again. As an exception, certain
 * units can be marked as having side effects using {@link #scheduleBeginSideEffectsMarker()}
 * and {@link #scheduleEndSideEffectsMarker()}. WUs between these markers will be
 * executed even when skipping. Note that a frame boundary marker also acts
 * as an implicit "end side effects" marker.
 * 
 * This class also keeps a second threshold called the "overload threshold" that
 * should be slightly less than the frame skip threshold. When the number of
 * "todo" frames exceeds this threshold, the worker loop is overloaded and the
 * high-level rendering threads should render fewer frames. Ideally, the frame
 * skip threshold is never reached because high-level rendering code drops frames
 * this way. Whenever the GL worker loop *does* cross the frame skip threshold,
 * frames get high-level rendered, and that partial work gets thrown away. On the
 * other hand, if the gap between the two thresholds is too large, it might happen
 * that only the low-level code gets overloaded up to the frame skip threshold,
 * causing visible rendering latency.
 */
public final class GlWorkerLoop {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(GlWorkerLoop.class);
	
	/**
	 * the queue
	 */
	private final BlockingQueue<GlWorkUnit> queue;
	
	/**
	 * the workload
	 */
	private volatile int workload = 0;
	
	/**
	 * the frameSkipThreshold
	 */
	private volatile int frameSkipThreshold = 5;
	
	/**
	 * the wantsToSkip
	 */
	private boolean wantsToSkip = false;
	
	/**
	 * the actuallySkipping
	 */
	private boolean actuallySkipping = false;
	
	/**
	 * the overloadThreshold
	 */
	private volatile int overloadThreshold = 3;
	
	/**
	 * "Executing" this work unit stops the worker loop.
	 */
	private static final GlWorkUnit STOP_LOOP = new NullWorkUnit();
	
	/**
	 * This WU is inserted between frames to measure the workload
	 * and to ensure that only whole frames get skipped. It also
	 * acts as an implicit END_SIDE_EFFECTS.
	 */
	private static final GlWorkUnit FRAME_BOUNDARY = new NullWorkUnit();

	/**
	 * Marks the WUs following this WU as having side effects, and thus "not skippable".
	 */
	private static final GlWorkUnit BEGIN_SIDE_EFFECTS = new NullWorkUnit();
	
	/**
	 * Marks the WUs following this WU as no longer having side effects, and thus "skippable".
	 */
	private static final GlWorkUnit END_SIDE_EFFECTS = new NullWorkUnit();
	
	/**
	 * Constructor.
	 */
	public GlWorkerLoop() {
		this.queue = new LinkedBlockingQueue<GlWorkUnit>();
	}
	
	/**
	 * Getter method for the queue.
	 * @return the queue
	 */
	public BlockingQueue<GlWorkUnit> getQueue() {
		return queue;
	}
	
	/**
	 * Executes work units until the queue is empty, then returns.
	 * This method must only be called by the OpenGL thread.
	 */
	public void workUntilEmpty() {
		while (true) {
			GlWorkUnit workUnit = queue.poll();
			if (workUnit == null || workUnit == STOP_LOOP) {
				break;
			}
			execute(workUnit);
		}
	}
	
	/**
	 * Executes work units, waiting for new ones when the queue becomes empty.
	 * Call {@link #scheduleStop()} to stop the loop.
	 * This method must only be called by the OpenGL thread.
	 * 
	 * @throws InterruptedException if interrupted while waiting
	 */
	public void workAndWait() throws InterruptedException {
		while (true) {
			GlWorkUnit workUnit = queue.take();
			if (workUnit == STOP_LOOP) {
				break;
			}
			execute(workUnit);
		}
	}
	
	/**
	 * This method must only be called by the OpenGL thread.
	 * @param workUnit the work unit to execute
	 */
	public void execute(GlWorkUnit workUnit) {
		if (workUnit == FRAME_BOUNDARY) {
			workload--;
			wantsToSkip = (workload >= frameSkipThreshold);
			actuallySkipping = wantsToSkip;
			if (actuallySkipping) {
				logger.info("low-level skip frame");
			}
		} else if (workUnit == BEGIN_SIDE_EFFECTS) {
			actuallySkipping = false;
		} else if (workUnit == END_SIDE_EFFECTS) {
			actuallySkipping = wantsToSkip;
		} else {
			if (!actuallySkipping) {
				workUnit.execute();
			}
		}
	}

	/**
	 * Schedules a work unit for execution.
	 * @param workUnit the work unit
	 */
	public void schedule(GlWorkUnit workUnit) {
		queue.add(workUnit);
	}
	
	/**
	 * Schedules a special work unit that stops the loop when executed.
	 */
	public void scheduleStop() {
		queue.add(STOP_LOOP);
	}

	/**
	 * Schedules a frame boundary "work unit".
	 */
	public void scheduleFrameBoundary() {
		queue.add(FRAME_BOUNDARY);
		workload++;
	}

	/**
	 * Getter method for the workload.
	 * @return the workload
	 */
	public int getWorkload() {
		return workload;
	}

	/**
	 * Getter method for the frameSkipThreshold.
	 * @return the frameSkipThreshold
	 */
	public int getFrameSkipThreshold() {
		return frameSkipThreshold;
	}
	
	/**
	 * Setter method for the frameSkipThreshold.
	 * @param frameSkipThreshold the frameSkipThreshold to set
	 */
	public void setFrameSkipThreshold(int frameSkipThreshold) {
		this.frameSkipThreshold = frameSkipThreshold;
	}
	
	/**
	 * 
	 */
	public void scheduleBeginSideEffectsMarker() {
		queue.add(BEGIN_SIDE_EFFECTS);
	}
	
	/**
	 * 
	 */
	public void scheduleEndSideEffectsMarker() {
		queue.add(END_SIDE_EFFECTS);
	}

	/**
	 * Getter method for the overloadThreshold.
	 * @return the overloadThreshold
	 */
	public int getOverloadThreshold() {
		return overloadThreshold;
	}
	
	/**
	 * Setter method for the overloadThreshold.
	 * @param overloadThreshold the overloadThreshold to set
	 */
	public void setOverloadThreshold(int overloadThreshold) {
		this.overloadThreshold = overloadThreshold;
	}
	
	/**
	 * Checks whether this worker loop is currently overloaded by comparing
	 * the current workload and the frame skip threshold.
	 * @return true if overloaded, false if not
	 */
	public boolean isOverloaded() {
		return (workload >= overloadThreshold);
	}

	/**
	 * Used for "marker" WUs.
	 */
	private static final class NullWorkUnit extends GlWorkUnit {
		@Override
		public void execute() {
		}
	};
	
}
