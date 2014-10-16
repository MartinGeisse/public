/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

import name.martingeisse.esdk.picoblaze.simulator.core.PicoblazeSimulatorException;
import name.martingeisse.esdk.picoblaze.simulator.cycle.InstructionCyclePicoblazeSimulator;
import name.martingeisse.esdk.picoblaze.simulator.port.IPicoblazePortHandler;
import name.martingeisse.esdk.picoblaze.simulator.progmem.IPicoblazeInstructionMemory;

import org.junit.Assert;



/**
 * A test harness to run a Picoblaze program in isolation. The simulation is
 * instruction cycle based (there is little reason to run an RTL simulation
 * if there is no interaction with other hardware).
 */
public final class IsolatedPicoblazeTestHarness {

	/**
	 * the simulator
	 */
	private final InstructionCyclePicoblazeSimulator simulator = new InstructionCyclePicoblazeSimulator();
	
	/**
	 * the roadmap
	 */
	private final SimulationRoadmap roadmap = new SimulationRoadmap();

	/**
	 * the timeoutInstructionCount
	 */
	private int timeoutInstructionCount = 1000;
	
	/**
	 * Constructor.
	 */
	public IsolatedPicoblazeTestHarness(IPicoblazeInstructionMemory instructionMemory) {
		simulator.setInstructionMemory(instructionMemory);
		simulator.setPortHandler(new IPicoblazePortHandler() {
			
			@Override
			public void handleOutput(int address, int value) {
				roadmap.handleBusTransaction(true, address, value);
			}
			
			@Override
			public int handleInput(int address) {
				return roadmap.handleBusTransaction(false, address, 0);
			}
			
		});
	}
	
	/**
	 * Getter method for the simulator.
	 * @return the simulator
	 */
	public InstructionCyclePicoblazeSimulator getSimulator() {
		return simulator;
	}
	
	/**
	 * Getter method for the roadmap.
	 * @return the roadmap
	 */
	public SimulationRoadmap getRoadmap() {
		return roadmap;
	}
	
	/**
	 * Adds the specified milestone to the simulation roadmap.
	 * @param milestone the milestone to add
	 */
	public void addToRoadmap(SimulationMilestone milestone) {
		roadmap.addMilestone(milestone);
	}

	/**
	 * Getter method for the timeoutInstructionCount.
	 * @return the timeoutInstructionCount
	 */
	public int getTimeoutInstructionCount() {
		return timeoutInstructionCount;
	}

	/**
	 * Setter method for the timeoutInstructionCount.
	 * @param timeoutInstructionCount the timeoutInstructionCount to set
	 */
	public void setTimeoutInstructionCount(int timeoutInstructionCount) {
		this.timeoutInstructionCount = timeoutInstructionCount;
	}

	/**
	 * Performs instructions until either the test has finished, an
	 * error has occurred, or the timeout instruction count has been reached.
	 */
	public void run() throws PicoblazeSimulatorException {
		try {
			roadmap.notifySimulationStart();
			simulator.performMultipleInstructionCycles(timeoutInstructionCount);
			Assert.fail("reached the timeout instruction count without finishing the roadmap; next milestone would be: " + roadmap.peek());
		} catch (RoadmapFinishedException e) {
		}
	}
	
}
