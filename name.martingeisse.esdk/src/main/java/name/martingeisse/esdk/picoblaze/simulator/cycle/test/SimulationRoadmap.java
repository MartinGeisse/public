/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Assert;

/**
 * A list of {@link SimulationMilestone}s that get processed during
 * a simulation.
 */
public final class SimulationRoadmap {

	/**
	 * the milestones
	 */
	private final Queue<SimulationMilestone> milestones = new LinkedList<SimulationMilestone>();
	
	/**
	 * Adds a milestone to this roadmap.
	 * @param milestone the milestone to add
	 */
	public void addMilestone(SimulationMilestone milestone) {
		milestones.add(milestone);
	}

	/**
	 * Peeks into the roadmap to return the next pending milestone.
	 * @return the milestone
	 */
	public SimulationMilestone peek() {
		return milestones.peek();
	}
	
	/**
	 *  Notifies this roadmap about starting the simulation. This in turn
	 *  notifies the first milestone that it has become visible.
	 */
	public void notifySimulationStart() throws RoadmapFinishedException {
		notifyVisibleAsNextMilestone();
	}

	/**
	 * 
	 */
	private void notifyVisibleAsNextMilestone() throws RoadmapFinishedException {
		while (true) {
			SimulationMilestone nextMilestone = milestones.peek();
			if (nextMilestone == null) {
				throw new RoadmapFinishedException();
			}
			if (nextMilestone.notifyVisibleAsNextMilestone()) {
				milestones.poll();
			} else {
				break;
			}
		}
	}

	/**
	 * Notifies this roadmap about a bus transaction. This checks
	 * that the bus transaction was expected, computes the return
	 * value for reads, and may trigger actions from subsequent
	 * milestones.
	 * 
	 * @param write whether the transaction was a write
	 * @param address the address
	 * @param data the expected or returned data
	 * @return the data read, or 0 for writes
	 */
	public int handleBusTransaction(boolean write, int address, int data) throws RoadmapFinishedException {
		SimulationMilestone nextMilestone = milestones.poll();
		if (nextMilestone == null) {
			Assert.fail("handleBusTransaction() -- no more milestones in the roadmap");
		}
		int result = nextMilestone.handleBusTransaction(write, address, data);
		notifyVisibleAsNextMilestone();
		return result;
	}

}
