/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.simulation;

import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.log4j.Logger;

/**
 * This thread executes the actual SimVM simulation code.
 * It communicates with web threads via {@link IpcEvent}s.
 */
class SimulationThread extends Thread {

	/**
	 * the EVENT_TYPE_PAUSE
	 */
	public static final String EVENT_TYPE_PAUSE = "simvm.pause";
	
	/**
	 * the EVENT_TYPE_STEP
	 */
	public static final String EVENT_TYPE_STEP = "simvm.step";
	
	/**
	 * the EVENT_TYPE_RESUME
	 */
	public static final String EVENT_TYPE_RESUME = "simvm.resume";
	
	/**
	 * the EVENT_TYPE_SUSPEND
	 */
	public static final String EVENT_TYPE_SUSPEND = "simvm.suspend";
	
	/**
	 * the EVENT_TYPE_TERMINATE
	 */
	public static final String EVENT_TYPE_TERMINATE = "simvm.terminate";
	
	/**
	 * the EVENT_TYPE_RUNNING
	 */
	public static final String EVENT_TYPE_RUNNING = "simvm.running";
	
	/**
	 * the EVENT_INSTANCE_RUNNING
	 */
	private static final IpcEvent<?> EVENT_INSTANCE_RUNNING = new IpcEvent<Object>(EVENT_TYPE_RUNNING, null);
	
	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SimulationThread.class);
	
	/**
	 * the simulation
	 */
	private final Simulation simulation;
	
	/**
	 * the running
	 */
	private volatile boolean running;
	
	/**
	 * Constructor.
	 * @param simulation the simulation
	 */
	public SimulationThread(Simulation simulation) {
		this.simulation = simulation;
		this.running = false;
	}
	
	/**
	 * Getter method for the running.
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				IpcEvent<?> event = fetchEvent();
				if (event == null) {
					continue;
				}
				if (event.getType().startsWith("simvm.")) {
					boolean terminate = handleSimulatorControlEvent(event);
					if (terminate) {
						break;
					}
				} else {
					handleCustomEvent(event);
				}
			}
		} finally {
			simulation.dispose();
		}
	}
	
	private IpcEvent<?> fetchEvent() {
		try {
			IpcEvent<?> event = simulation.fetchEvent(!running);
			return (event == null ? EVENT_INSTANCE_RUNNING : event);
		} catch (InterruptedException e) {
			logger.error("SimVM simulation thread was interrupted while fetching an event");
			return null;
		}
		
	}

	private boolean handleSimulatorControlEvent(IpcEvent<?> event) {
		String type = event.getType();
		if (type.equals(EVENT_TYPE_PAUSE)) {
			handlePause();
			return false;
		} else if (type.equals(EVENT_TYPE_STEP)) {
			handleStep();
			return false;
		} else if (type.equals(EVENT_TYPE_RESUME)) {
			handleResume();
			return false;
		} else if (type.equals(EVENT_TYPE_RUNNING)) {
			handleRunning();
			return false;
		} else if (type.equals(EVENT_TYPE_SUSPEND)) {
			handleSuspend();
			return true;
		} else if (type.equals(EVENT_TYPE_TERMINATE)) {
			handleTerminate();
			return true;
		} else {
			logger.error("unknown SimVM control event type: " + type);
			return false;
		}
	}

	private void handlePause() {
		running = false;
	}
	
	private void handleStep() {
		// TODO: call step() method of model
	}
	
	private void handleResume() {
		running = true;
	}

	private void handleRunning() {
		// TODO: call multiStep() method of model
	}
	
	private void handleSuspend() {
		// TODO: call saveState() method of model
	}
	
	private void handleTerminate() {
		// TODO: call deleteState() method of model
	}
	
	private void handleCustomEvent(IpcEvent<?> event) {
		// TODO: pass to simulator model
	}
	
}
