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
	 * @throws Exception ...
	 */
	@Test
	public void testFoo() throws Exception {
		AsciiGpuControllerProgramMemory programMemory = new AsciiGpuControllerProgramMemory();
		IsolatedPicoblazeTestHarness testHarness = new IsolatedPicoblazeTestHarness();
		testHarness.addToRoadmap(new ExpectedBusTransaction(false, 1, 1));
		testHarness.run();
	}
	
}
