/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.components.ascii_gpu;

import name.martingeisse.esdk.picoblaze.simulator.cycle.test.ExpectedBusTransaction;
import name.martingeisse.esdk.picoblaze.simulator.cycle.test.IsolatedPicoblazeTestHarness;

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
	 * @throws Exception ...
	 */
	@Test
	public void testFoo() throws Exception {
		testHarness.addToRoadmap(new ExpectedBusTransaction(true, 0, 0));
		testHarness.addToRoadmap(new ExpectedBusTransaction(true, 0, 1));
		testHarness.addToRoadmap(new ExpectedBusTransaction(true, 0, 2));
		testHarness.addToRoadmap(new ExpectedBusTransaction(true, 0, 3));
		testHarness.run();
	}
	
}
