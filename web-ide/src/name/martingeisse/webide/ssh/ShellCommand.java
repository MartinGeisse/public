/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.PushbackReader;
import java.util.LinkedList;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.ssh.IShellContext.ExecuteStatus;
import name.martingeisse.webide.ssh.workspace.WorkspaceContext;

import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

/**
 * The shell command.
 * 
 * Note: This class does not use the stderr stream passed to it.
 * When used interactively, the streams do not get transmitted
 * synchronously, so stdout and stderr end up mixed the wrong way.
 */
public class ShellCommand implements Command, Runnable {

	/**
	 * the in
	 */
	private InputStream in;
	
	/**
	 * the out
	 */
	private OutputStream out;
	
	/**
	 * the exitCallback
	 */
	private ExitCallback exitCallback;
	
	/**
	 * the inputReader
	 */
	private PushbackReader inputReader;
	
	/**
	 * the outputWriter
	 */
	private PrintWriter outputWriter;
	
	/**
	 * the commandLineBuilder
	 */
	private StringBuilder commandLineBuilder;
	
	/**
	 * the contextStack
	 */
	private LinkedList<IShellContext> contextStack;
	
	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#setInputStream(java.io.InputStream)
	 */
	@Override
	public void setInputStream(final InputStream in) {
		this.in = in;
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#setOutputStream(java.io.OutputStream)
	 */
	@Override
	public void setOutputStream(final OutputStream out) {
		this.out = out;
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#setErrorStream(java.io.OutputStream)
	 */
	@Override
	public void setErrorStream(final OutputStream err) {
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#setExitCallback(org.apache.sshd.server.ExitCallback)
	 */
	@Override
	public void setExitCallback(final ExitCallback callback) {
		this.exitCallback = callback;
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#start(org.apache.sshd.server.Environment)
	 */
	@Override
	public void start(final Environment env) throws IOException {
        new Thread(this, "SSH shell thread").start();
	}

	/* (non-Javadoc)
	 * @see org.apache.sshd.server.Command#destroy()
	 */
	@Override
	public void destroy() {
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			inputReader = new PushbackReader(new InputStreamReader(in, "utf-8"));
			outputWriter = new PrintWriter(new OutputStreamWriter(out, "utf-8"));
			commandLineBuilder = new StringBuilder();
			contextStack = new LinkedList<IShellContext>();
			contextStack.push(new WorkspaceContext());
			printPrompt();
			while (!contextStack.isEmpty()) {
				try {
					int c = inputReader.read();
					if (c == '\r' || c == '\n') {
						executeCommand();
						printPrompt();
					} else if (c == '\t') {
						autocomplete();
					} else if (c == 127) {
						erase();
					} else if (c >= 32) {
						typeRegular(c);
					}
					outputWriter.flush();
				} finally {
					EntityConnectionManager.disposeConnections();
				}
			}
		} catch (Exception e) {
		}
        if (exitCallback != null) {
        	exitCallback.onExit(0, "ok");
        }
	}
	
	/**
	 * 
	 */
	private void printPrompt() {
		contextStack.getFirst().printPrompt(outputWriter);
		outputWriter.flush();
	}
	
	/**
	 * 
	 */
	private void executeCommand() {
		
		// obtain the command
		String command = commandLineBuilder.toString();
		commandLineBuilder.setLength(0);
		outputWriter.print("\r\n");
		
		// pass the command to the TOS context
		ExecuteStatus status = contextStack.getFirst().execute(command, outputWriter, outputWriter);
		outputWriter.flush();
		
		// modify the context stack as needed
		if (status == ExecuteStatus.ENTER) {
			contextStack.push(contextStack.getFirst().getSubContext());
		} else if (status == ExecuteStatus.TERMINATE) {
			contextStack.pop();
		}
		
	}
	
	/**
	 * 
	 */
	private void autocomplete() {
		beep();
	}
	
	/**
	 * 
	 */
	private void erase() {
		if (commandLineBuilder.length() > 0) {
			commandLineBuilder.setLength(commandLineBuilder.length() - 1);
			outputWriter.print("\b \b");
		} else {
			beep();
		}
	}
	
	/**
	 * 
	 */
	private void typeRegular(int c) {
		commandLineBuilder.append((char)c);
		outputWriter.print((char)c);
	}

	/**
	 * 
	 */
	private void beep() {
		outputWriter.print((char)7);
	}

}
