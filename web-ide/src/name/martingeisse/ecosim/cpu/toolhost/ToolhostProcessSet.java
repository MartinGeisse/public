/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.ecosim.cpu.usermode.unix.IFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.StreamFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.SystemInFilePointer;
import name.martingeisse.ecosim.cpu.usermode.unix.UnixSyscallException;

/**
 * A set of {@link ToolhostProcess} objects. Processes are identified
 * in this set by process identifiers (PIDs) of type int.
 */
public class ToolhostProcessSet {

	/**
	 * The number of steps each process takes before the next process executes
	 * its steps. This is more or less the "timeslice" of each process in a
	 * round-robin scheduled OS.
	 */
	private static int STEP_GRANULARITY = 100;
	
	/**
	 * the processes
	 */
	private Map<Integer, ToolhostProcess> processes;
	
	/**
	 * the idCounter
	 */
	private int idCounter;

	/**
	 * the fileSystem
	 */
	private IToolhostFileSystem fileSystem;
	
	/**
	 * the running
	 */
	private boolean running;
	
	/**
	 * Constructor.
	 * @param fileSystem the file system
	 */
	public ToolhostProcessSet(IToolhostFileSystem fileSystem) {
		this.processes = new HashMap<Integer, ToolhostProcess>();
		this.idCounter = 1000;
		this.fileSystem = fileSystem;
		this.running = false;
	}
	
	/**
	 * Creates the initial process. This process has its stdin, stdout
	 * and stderr bound to the host OS's terminal, and has no known parent
	 * process (the PPID is reported as 1, the init process).
	 * 
	 * @return the process
	 * @throws UnixSyscallException on errors
	 */
	public ToolhostProcess createInitialProcess() throws UnixSyscallException {
		SystemInFilePointer stdin = new SystemInFilePointer();
		StreamFilePointer stdout = new StreamFilePointer(System.out);
		StreamFilePointer stderr = new StreamFilePointer(System.err);
		return createInitialProcess(null, stdin, stdout, stderr);
	}

	/**
	 * Creates the initial process. This process uses the specified
	 * stdin, stdout and stderr file pointers, and has no known parent
	 * process (the PPID is reported as 1, the init process).
	 * 
	 * This function will property acquire counted references to the
	 * file pointer. The caller is responsible for releasing its own
	 * references after calling this method.
	 * 
	 * @param workingDirectory the initial working directory of the process
	 * @param stdin the stdin file pointer
	 * @param stdout the stdout file pointer
	 * @param stderr the stderr file pointer
	 * @return the process
	 * @throws UnixSyscallException on errors
	 */
	public ToolhostProcess createInitialProcess(File workingDirectory, IFilePointer stdin, IFilePointer stdout, IFilePointer stderr) throws UnixSyscallException {
		ToolhostProcess process = new ToolhostProcess(this, null, idCounter);
		if (workingDirectory != null) {
			process.setWorkingDirectory(workingDirectory);
		}
		process.getFilePointerTable().replaceEntry(0, stdin);
		process.getFilePointerTable().replaceEntry(1, stdout);
		process.getFilePointerTable().replaceEntry(2, stderr);
		processes.put(process.getId(), process);
		idCounter++;
		return process;
	}
	
	/**
	 * Manually adds a process. This method us used by fork().
	 * @param process the process to add
	 */
	void addProcess(ToolhostProcess process) {
		process.setId(idCounter);
		processes.put(process.getId(), process);
		idCounter++;
	}

	/**
	 * Returns the process with the specified ID.
	 * @param id the process ID
	 * @return the process, or null if there is no process with that ID
	 */
	public ToolhostProcess getProcess(int id) {
		return processes.get(id);
	}
	
	/**
	 * Checks whether this set has any running process left.
	 * @return true if any running processes are left, otherwise false
	 */
	public boolean hasRunningProcess() {
		for (ToolhostProcess process : processes.values()) {
			if (process.getState() != ToolhostProcessState.ZOMBIE) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Getter method for the fileSystem.
	 * @return the fileSystem
	 */
	public IToolhostFileSystem getFileSystem() {
		return fileSystem;
	}
	
	/**
	 * Runs this process. This method blocks until this process is stop()ed.
	 */
	public void run() {
		running = true;
		while (running && hasRunningProcess()) {
			Map<Integer, ToolhostProcess> processesCopy = new HashMap<Integer, ToolhostProcess>(processes);
			for (ToolhostProcess process : processesCopy.values()) {
				process.step(STEP_GRANULARITY);
			}
		}
		running = false;
	}

	/**
	 * Stops this CPU. This breaks the blocking of the run() method.
	 */
	public void stop() {
		running = false;
	}
	
	/**
	 * Tries to find a child process of the specified parent process that
	 * is in zombie state. Returns null if no such child can be found.
	 * @param parent the parent process
	 * @return the zombie child or null
	 */
	public ToolhostProcess findZombieChild(ToolhostProcess parent) {
		for (ToolhostProcess process : processes.values()) {
			if (process.getParent() == parent && process.getState() == ToolhostProcessState.ZOMBIE) {
				return process;
			}
		}
		return null;
	}

	/**
	 * Removes the specified zombie process. Throws an {@link IllegalArgumentException}
	 * if the process is not a zombie or does not belong to this process set.
	 * @param process the process to remove
	 */
	public void removeZombie(ToolhostProcess process) {
		if (process.getProcessSet() != this) {
			throw new IllegalArgumentException("process " + process.getId() + " does not belong to this process group");
		}
		if (process.getState() != ToolhostProcessState.ZOMBIE) {
			throw new IllegalArgumentException("process " + process.getId() + " is not a zombie");
		}
		processes.remove(process.getId());
		process.setProcessSet(null);
	}
	
}
