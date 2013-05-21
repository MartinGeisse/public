/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import name.martingeisse.webide.ipc.IpcEvent;
import name.martingeisse.webide.process.msgserv.MessageHandler;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.apache.commons.exec.ExecuteStreamHandler;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;

/**
 * A companion process is a sub-process that runs alongside the
 * IDE and communicates with the IDE via {@link IpcEvent}s.
 * The concrete subclass of this class determines the framework
 * used by the child process (if any), such as (Java + support
 * libraries) or (NodeJS + support libraries).
 * 
 * Depending on the framework, the data types that can be sent
 * via events may be limited.
 */
public abstract class CompanionProcess {

	/**
	 * the nextCompanionId
	 */
	private static long nextCompanionId = 0;
	
	/**
	 * the runningProcesses
	 */
	private static final ConcurrentHashMap<Long, CompanionProcess> runningProcesses = new ConcurrentHashMap<Long, CompanionProcess>();
	
	/**
	 * Returns the companion process for the specified companion ID, or
	 * null if none exists.
	 * 
	 * @param companionId the companion ID
	 * @return the process or null
	 */
	public static CompanionProcess getRunningProcess(long companionId) {
		return runningProcesses.get(companionId);
	}
	
	/**
	 * the companionId
	 */
	private final long companionId;
	
	/**
	 * the started
	 */
	private boolean started = false;
	
	/**
	 * the stopped
	 */
	private boolean stopped = false;
	
	/**
	 * the messageHandler
	 */
	private MessageHandler messageHandler;
	
	/**
	 * Constructor.
	 */
	public CompanionProcess() {
		synchronized(CompanionProcess.class) {
			companionId = nextCompanionId;
			nextCompanionId++;
		}
	}
	
	/**
	 * Getter method for the companionId.
	 * @return the companionId
	 */
	public long getCompanionId() {
		return companionId;
	}
	
	/**
	 * Getter method for the started.
	 * @return the started
	 */
	public final boolean isStarted() {
		return started;
	}
	
	/**
	 * Returns true if the child process has connected, so this
	 * object can be used to send {@link IpcEvent}s.
	 * @return true if connected, false if not
	 */
	public final boolean isConnected() {
		return messageHandler != null;
	}

	/**
	 * Getter method for the stopped.
	 * @return the stopped
	 */
	public final boolean isStopped() {
		return stopped;
	}
	
	/**
	 * This method creates the command line that starts the
	 * subprocess.
	 * 
	 * @return the command line
	 */
	protected abstract CommandLine buildCommandLine();
	
	/**
	 * This method creates the environment for the subprocess.
	 * The default implementation returns null to use the
	 * default environment.
	 * 
	 * @return the environment
	 */
	protected Map<String, String> buildEnvironment() {
		return null;
	}
	
	/**
	 * Performs custom configuration on the {@link Executor} that is
	 * used to spawn the subprocess, such as an {@link ExecuteStreamHandler}
	 * or a custom working directory. The default implementation
	 * does nothing.
	 * 
	 * @param executor the executor
	 */
	protected void configureExecutor(Executor executor) {
	}
	
	/**
	 * Starts the subprocess.
	 * 
	 * @throws IOException on I/O errors
	 */
	public final synchronized void start() throws IOException {
		if (started) {
			throw new IllegalStateException("companion process has already been started");
		}
		started = true;
		onBeforeStart();
		CommandLine commandLine = buildCommandLine();
		Map<String, String> environment = buildEnvironment();
		Executor executor = new DefaultExecutor();
		executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
		configureExecutor(executor);
		
		boolean successfullyStarted = false;
		try {
			runningProcesses.put(companionId, this);
			executor.execute(commandLine, environment, new ExecuteResultHandler() {
				
				@Override
				public void onProcessFailed(ExecuteException e) {
					messageHandler = null;
					runningProcesses.remove(companionId);
					CompanionProcess.this.onProcessFailed(e.getExitValue(), e.getMessage());
				}
				
				@Override
				public void onProcessComplete(int exitValue) {
					messageHandler = null;
					runningProcesses.remove(companionId);
					CompanionProcess.this.onProcessComplete(exitValue);
				}
				
			});
			successfullyStarted = true;
		} finally {
			if (!successfullyStarted) {
				runningProcesses.remove(companionId);
			}
		}
		
		onAfterStart();
	}
	
	/**
	 * Internal method. Do not call.
	 * @param messageHandler the message handler
	 */
	public void internalNotifyConnected(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
		onConnected();
	}
	
	/**
	 * Internal method. Do not call.
	 * @param envelope the message envelope
	 */
	public void internalNotifyIpc(Map<String, Object> envelope) {
		Object ipcEventType = envelope.get("ipcEventType");
		if (!(ipcEventType instanceof String)) {
			return;
		}
		Object ipcEventData = envelope.get("ipcEventData");
		IpcEvent ipcEvent = new IpcEvent((String)ipcEventType, null, ipcEventData);
		onEventReceived(ipcEvent);
	}
	
	/**
	 * This callback is invoked at the beginning of the start() method.
	 */
	protected void onBeforeStart() {
	}
	
	/**
	 * This callback is invoked at the end of the start() method.
	 */
	protected void onAfterStart() {
	}
	
	/**
	 * This callback is invoked when the subprocess fails.
	 * 
	 * @param exitValue the exit value
	 * @param message the error message
	 */
	protected void onProcessFailed(int exitValue, String message) {
	}

	/**
	 * This callback is invoked when the subprocess completes.
	 * 
	 * @param exitValue the exit value
	 */
	protected void onProcessComplete(int exitValue) {
	}

	/**
	 * This method is invoked when the connection to the subprocess
	 * has been established. Depending on the application protocol,
	 * either the parent process or the subprocess would start
	 * sending the first IPC event, and this method gives the parent
	 * process the chance to do just that. On the other hand, if the
	 * child process sends the first event, then this method would
	 * be empty, and the parent process would wait until the first
	 * message arrives at {@link #onEventReceived(IpcEvent)}.
	 */
	protected void onConnected() {
	}
	
	/**
	 * This method is invoked when an event is received from the
	 * subprocess. The default implementation does nothing.
	 * 
	 * @param event the event
	 */
	protected void onEventReceived(IpcEvent event) {
	}
	
	/**
	 * Sends an event to the subprocess.
	 * 
	 * If the child process has not yet connected, or has already stopped,
	 * has crashed or is unreachable, then this method silently returns.
	 * The reason for this is that in general, the subprocess would be
	 * in an undefined state, but there is little guarantee that such
	 * a condition can actually be detected, so even if this method tried
	 * to throw an exception, the caller must still be prepared that
	 * the event silently disappears.
	 * 
	 * Use onConnected() to detect when the connection to
	 * the subprocess has been established.
	 * 
	 * @param event the event
	 */
	public final void sendEvent(IpcEvent event) {
		if (started) {
			Map<String, Object> envelope = new HashMap<String, Object>();
			envelope.put("type", "ipc");
			envelope.put("ipcEventType", event.getType());
			envelope.put("ipcEventData", event.getData());
			messageHandler.sendEnvelope(envelope);
		}
	}

}
