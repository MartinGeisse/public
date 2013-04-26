/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.simulation;

import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 * This thread executes the actual SimVM simulation code.
 * It communicates with web threads via {@link IpcEvent}s.
 */
class SimulationThread extends Thread {

	/**
	 * the EVENT_TYPE_RUNNING
	 */
	public static final String EVENT_TYPE_RUNNING = "simvm.running";
	
	/**
	 * the EVENT_INSTANCE_RUNNING
	 */
	private static final IpcEvent EVENT_INSTANCE_RUNNING = new IpcEvent(EVENT_TYPE_RUNNING, null, null);
	
	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(SimulationThread.class);
	
	/**
	 * the simulation
	 */
	private final SimulatedVirtualMachine virtualMachine;
	
	/**
	 * the exitEvent
	 */
	private IpcEvent exitEvent;
	
	/**
	 * Constructor.
	 * @param virtualMachine the simulated virtual machine
	 */
	public SimulationThread(SimulatedVirtualMachine virtualMachine) {
		this.virtualMachine = virtualMachine;
		this.exitEvent = null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		
		// TODO for testing
		try {
			sleep(2000);
		} catch (InterruptedException e) {
		}
		
		try {
			virtualMachine.getSimulationModel().getPrimaryElement().loadRuntimeState(new JSONObject());
			virtualMachine.getOutputEventBus().sendEvent(new IpcEvent(SimulationEvents.EVENT_TYPE_START, virtualMachine, null));
			while (true) {
				IpcEvent event = fetchEvent();
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
			virtualMachine.detachSimulationThread();
			if (exitEvent != null) {
				virtualMachine.getOutputEventBus().sendEvent(exitEvent);
			}
		}
	}
	
	private IpcEvent fetchEvent() {
		try {
			IpcEvent event = virtualMachine.fetchEvent(virtualMachine.getState() == SimulationState.PAUSED);
			return (event == null ? EVENT_INSTANCE_RUNNING : event);
		} catch (InterruptedException e) {
			logger.error("SimVM simulation thread was interrupted while fetching an event");
			return null;
		}
		
	}

	private boolean handleSimulatorControlEvent(IpcEvent event) {
		String type = event.getType();
		if (type.equals(SimulationEvents.EVENT_TYPE_PAUSE)) {
			handlePause();
			return false;
		} else if (type.equals(SimulationEvents.EVENT_TYPE_STEP)) {
			handleStep();
			return false;
		} else if (type.equals(SimulationEvents.EVENT_TYPE_RESUME)) {
			handleResume();
			return false;
		} else if (type.equals(EVENT_TYPE_RUNNING)) {
			handleRunning();
			return false;
		} else if (type.equals(SimulationEvents.EVENT_TYPE_SUSPEND)) {
			handleSuspend();
			return true;
		} else if (type.equals(SimulationEvents.EVENT_TYPE_TERMINATE)) {
			handleTerminate();
			return true;
		} else {
			logger.error("unknown SimVM control event type: " + type);
			return false;
		}
	}

	private void handlePause() {
		virtualMachine.setState(SimulationState.PAUSED);
		virtualMachine.getOutputEventBus().sendEvent(new IpcEvent(SimulationEvents.EVENT_TYPE_PAUSE, virtualMachine, null));
	}
	
	private void handleStep() {
		virtualMachine.getSimulationModel().getPrimaryElement().singleStep();
		virtualMachine.getOutputEventBus().sendEvent(new IpcEvent(SimulationEvents.EVENT_TYPE_STEP, virtualMachine, null));
	}
	
	private void handleResume() {
		virtualMachine.setState(SimulationState.RUNNING);
		virtualMachine.getOutputEventBus().sendEvent(new IpcEvent(SimulationEvents.EVENT_TYPE_RESUME, virtualMachine, null));
	}

	private void handleRunning() {
		virtualMachine.getSimulationModel().getPrimaryElement().batchStep();
	}
	
	private void handleSuspend() {
		virtualMachine.getSimulationModel().getPrimaryElement().saveRuntimeState();
		exitEvent = new IpcEvent(SimulationEvents.EVENT_TYPE_SUSPEND, virtualMachine, null);
	}
	
	private void handleTerminate() {
		virtualMachine.getSimulationModel().getPrimaryElement().deleteSavedRuntimeState();
		exitEvent = new IpcEvent(SimulationEvents.EVENT_TYPE_TERMINATE, virtualMachine, null);
	}
	
	private void handleCustomEvent(IpcEvent event) {
		virtualMachine.getSimulationModel().getPrimaryElement().handleEvent(event);
	}
	
}
