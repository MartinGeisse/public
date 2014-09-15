/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package test;

import name.martingeisse.esdk.hdl.components.SimpleCounter;
import name.martingeisse.esdk.hdl.core.ClockSource;
import name.martingeisse.esdk.hdl.experiment.SynchronousExperimentUtil;
import name.martingeisse.esdk.hdl.probe.MultiBitWatcher;

/**
 * 
 */
public class ThreeBitCounterMain {

	/**
	 * The main method.
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		
		SimpleCounter counter = new SimpleCounter();
		MultiBitWatcher watcher = new MultiBitWatcher("counter");
		watcher.addSource(counter.createValueSource(2));
		watcher.addSource(counter.createValueSource(1));
		watcher.addSource(counter.createValueSource(0));
		
		ClockSource clockSource = new ClockSource();
		clockSource.addConsumer(counter);
		clockSource.addConsumer(watcher);
		
		SynchronousExperimentUtil.simulateCycles(clockSource, 100);

	}
	
}
