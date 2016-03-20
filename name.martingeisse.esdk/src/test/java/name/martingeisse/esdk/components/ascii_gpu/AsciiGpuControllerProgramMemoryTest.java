/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.esdk.components.ascii_gpu;

import java.util.Random;

import name.martingeisse.esdk.picoblaze.simulator.cycle.test.ExpectedBusTransaction;
import name.martingeisse.esdk.picoblaze.simulator.cycle.test.IsolatedPicoblazeTestHarness;
import name.martingeisse.esdk.picoblaze.simulator.cycle.test.LoopSection;
import name.martingeisse.esdk.picoblaze.simulator.cycle.test.RepeaterSection;
import name.martingeisse.esdk.picoblaze.simulator.cycle.test.SimulationMilestone;

import org.junit.Test;

/**
 *
 */
public class AsciiGpuControllerProgramMemoryTest {

	/**
	 * the programMemory
	 */
	private AsciiGpuControllerProgramMemory programMemory = new AsciiGpuControllerProgramMemory();
	
	/**
	 * the testHarness
	 */
	private IsolatedPicoblazeTestHarness testHarness = new IsolatedPicoblazeTestHarness(programMemory);

	/**
	 * the random
	 */
	private Random random = new Random();
	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private ExpectedBusTransaction busWrite(int address, int data) {
		return new ExpectedBusTransaction(true, address, data);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private ExpectedBusTransaction busRead(int address, int data) {
		return new ExpectedBusTransaction(false, address, data);
	}
	
	/**
	 * Adds milestones for the specified message bytes to the roadmap.
	 * No other actions than reading the bytes from the message data queue
	 * are expected from the controller.
	 */
	private void addSimulatedMessageInput(int... bytes) {
		for (int b : bytes) {
			testHarness.addToRoadmap(new RepeaterSection(busRead(0x00, 0x00), random.nextInt(20)));
			testHarness.addToRoadmap(busRead(00, 01), busRead(01, b));
		}
	}

	//
	// ------------------------------------------------------------------------------------
	//

	/**
	 * @throws Exception ...
	 */
	@Test
	public void testFillScreen() throws Exception {
		final int CELL_BYTES = 3;
		final int ROW_BYTES = 1 + 128 * CELL_BYTES;
		addSimulatedMessageInput(0x00, 0x23, 0x9a);
		testHarness.addToRoadmap(new LoopSection(37 * ROW_BYTES) {
			@Override
			protected SimulationMilestone getMilestone(final int index) {
				
				// write row pointer
				if (index % ROW_BYTES == 0) {
					return busWrite(4, index / ROW_BYTES);
				}
				
				// all others are row-independent operations
				final int rowByteIndex = index % ROW_BYTES - 1;
				final int cellByteIndex = rowByteIndex % CELL_BYTES;
				if (cellByteIndex == 0) {
					// write column pointer
					return busWrite(2, rowByteIndex / CELL_BYTES);
				} else if (cellByteIndex == 1) {
					// write attributes
					return busWrite(8, 0x23);
				} else {
					// write character
					return busWrite(16, 0x9a);
				}
				
			}
		});
		testHarness.setTimeoutInstructionCount(1000000);
		testHarness.run();
	}
	
	//
	// ------------------------------------------------------------------------------------
	//
	
	/**
	 * 
	 */
	private void addSetCellTest(int x, int y, int attribute, int character) {
		addSimulatedMessageInput(0x01, x);
		testHarness.addToRoadmap(busWrite(2, x));
		addSimulatedMessageInput(y);
		testHarness.addToRoadmap(busWrite(4, y));
		addSimulatedMessageInput(attribute);
		testHarness.addToRoadmap(busWrite(8, attribute));
		addSimulatedMessageInput(character);
		testHarness.addToRoadmap(busWrite(16, character));
	}
	
	/**
	 * @throws Exception ...
	 */
	@Test
	public void testSetCell() throws Exception {
		addSetCellTest(0, 0, 0x0f, 'A');
		addSetCellTest(127, 0, 0x0f, 'B');
		addSetCellTest(0, 37, 0x0f, 'C');
		addSetCellTest(127, 37, 0x0f, 'D');
		testHarness.run();
	}
	
}
