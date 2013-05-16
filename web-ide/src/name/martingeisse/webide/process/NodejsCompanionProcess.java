/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.process;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.webide.application.Configuration;
import name.martingeisse.webide.ipc.IpcEvent;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

/**
 * This class is used for NodeJS-based companion processes.
 */
public class NodejsCompanionProcess extends CompanionProcess {

	/**
	 * the mainFile
	 */
	private final File mainFile;
	
	/**
	 * Constructor.
	 * @param mainFile the main JS file to execute
	 */
	public NodejsCompanionProcess(File mainFile) {
		this.mainFile = mainFile;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.process.CompanionProcess#buildCommandLine()
	 */
	@Override
	protected CommandLine buildCommandLine() {
		String url = CompanionProcessMessageFilter.getMessageBaseUrl(getCompanionId());
		CommandLine commandLine = new CommandLine(Configuration.getBashPath());
		commandLine.addArgument("--login");
		commandLine.addArgument("-c");
		commandLine.addArgument("node " + mainFile.getName() + " '" + url + "'", false);
		return commandLine;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.process.CompanionProcess#buildEnvironment()
	 */
	@Override
	protected Map<String, String> buildEnvironment() {
		Map<String, String> environment = new HashMap<String, String>();
		environment.put("NODE_PATH", new File("lib/node_modules").getAbsolutePath());
		return environment;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.process.CompanionProcess#configureExecutor(org.apache.commons.exec.Executor)
	 */
	@Override
	protected void configureExecutor(Executor executor) {
		super.configureExecutor(executor);
		ByteArrayInputStream inputStream = null;
		OutputStream outputStream = System.err;
		OutputStream errorStream = System.err;
		executor.setStreamHandler(new PumpStreamHandler(outputStream, errorStream, inputStream));
		executor.setWorkingDirectory(mainFile.getParentFile());
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.process.CompanionProcess#doSendEvent(name.martingeisse.webide.ipc.IpcEvent)
	 */
	@Override
	protected void doSendEvent(IpcEvent event) {
	}
	
}
