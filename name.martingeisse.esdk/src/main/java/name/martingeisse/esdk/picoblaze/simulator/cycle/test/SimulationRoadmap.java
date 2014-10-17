/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Assert;

/**
 * A list of {@link ISimulationRoadmapSection}s that get processed during
 * a simulation.
 */
public final class SimulationRoadmap {

	/**
	 * the sections
	 */
	private final Queue<ISimulationRoadmapSection> sections = new LinkedList<ISimulationRoadmapSection>();
	
	/**
	 * Adds a section to this roadmap.
	 * @param section the section to add
	 */
	public void addSection(ISimulationRoadmapSection section) {
		sections.add(section);
	}

	/**
	 * 
	 */
	private void removeEmptySections() {
		while (!sections.isEmpty() && sections.peek().peek() == null) {
			sections.poll();
		}
	}
	
	/**
	 * Peeks into the roadmap to return the next pending milestone.
	 * @return the milestone
	 */
	public SimulationMilestone peek() {
		removeEmptySections();
		return (sections.peek() == null ? null : sections.peek().peek());
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
			removeEmptySections();
			if (sections.peek() == null) {
				throw new RoadmapFinishedException();
			}
			SimulationMilestone nextMilestone = sections.peek().peek();
			if (nextMilestone.notifyVisibleAsNextMilestone()) {
				sections.peek().fetch();
				removeEmptySections();
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
		removeEmptySections();
		if (sections.peek() == null) {
			Assert.fail("handleBusTransaction() -- no more milestones in the roadmap");
		}
		SimulationMilestone nextMilestone = sections.peek().fetch();
		int result = nextMilestone.handleBusTransaction(write, address, data);
		notifyVisibleAsNextMilestone();
		return result;
	}

}
