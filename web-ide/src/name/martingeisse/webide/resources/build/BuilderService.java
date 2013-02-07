/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import name.martingeisse.webide.features.java.compiler.JavaCompilerFacade;
import name.martingeisse.webide.features.verilog.compiler.VerilogCompilerFacade;
import name.martingeisse.webide.pde.PluginBuilderFacade;

/**
 * This class acts as a façade for the builder thread.
 */
public class BuilderService {

	/**
	 * the synchronizationKey
	 */
	private static final Object synchronizationKey = new Object();
	
	/**
	 * the buildRequested
	 */
	private static volatile boolean buildRequested = true;

	/**
	 * the buildFinished
	 */
	private static volatile boolean buildFinished = false;
	
	/**
	 * Requests a build.
	 */
	public static void requestBuild() {
		synchronized(synchronizationKey) {
			buildRequested = true;
			buildFinished = false;
			synchronizationKey.notify();
		}
	}
	
	/**
	 * Checks whether building is finished.
	 * @return true if finished, false if running
	 */
	public static boolean isBuildFinished() {
		return buildFinished;
	}

	/**
	 * static initializer
	 */
	static {
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						
						// wait until requested, then mark as no longer requested to detect subsequent requests
						synchronized(synchronizationKey) {
							if (!buildRequested) {
								synchronizationKey.wait();
							}
							buildRequested = false;
						}

						// actually compile
						performBuild();
						
						// mark as finished unless subsequent requests have arrived
						buildFinished = !buildRequested;
						
					}
				} catch (InterruptedException e) {
				}
			}
		}.start();
	}

	/**
	 * This method is invoked by the builder thread to actually build projects.
	 */
	private static void performBuild() {
		try {
			JavaCompilerFacade.performCompilation();
			VerilogCompilerFacade.performCompilation();
			PluginBuilderFacade.performBuild();
		} catch (Throwable e) {
			System.err.println("exception in builder thread: " + e);
			e.printStackTrace(System.err);
		}
	}
	
}
