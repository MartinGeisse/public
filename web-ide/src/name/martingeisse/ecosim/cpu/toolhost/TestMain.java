/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.ecosim.cpu.toolhost;


/**
 * Test application for "coherence mode".
 */
public class TestMain {

	/**
	 * @param args ...
	 * @throws Exception ...
	 */
	public static void main(String[] args) throws Exception {
		
		ToolhostProcessSet processSet = new ToolhostProcessSet(new ToolhostFileSystem());
		ToolhostProcess process = processSet.createInitialProcess();
		process.exec("../ecobuild/build/modules/eos32/userland/coherence/hello/hello");
		processSet.run();
		
	}
	
}
