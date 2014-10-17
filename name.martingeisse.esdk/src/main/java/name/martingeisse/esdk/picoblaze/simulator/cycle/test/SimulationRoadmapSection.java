/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Default implementation for {@link ISimulationRoadmapSection}.
 */
public final class SimulationRoadmapSection implements ISimulationRoadmapSection {

	/**
	 * the milestones
	 */
	private final Queue<SimulationMilestone> milestones = new LinkedList<SimulationMilestone>();

	/**
	 * Constructor.
	 * @param milestones initial milestones to add
	 */
	public SimulationRoadmapSection(SimulationMilestone... milestones) {
		for (SimulationMilestone milestone : milestones) {
			this.milestones.add(milestone);
		}
	}
	
	/**
	 * Getter method for the milestones.
	 * @return the milestones
	 */
	public Queue<SimulationMilestone> getMilestones() {
		return milestones;
	}

	/**
	 * Adds a milestone to this roadmap.
	 * @param milestone the milestone to add
	 */
	public void addMilestone(SimulationMilestone milestone) {
		milestones.add(milestone);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.ISimulationRoadmapSection#peek()
	 */
	@Override
	public SimulationMilestone peek() {
		return milestones.peek();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.ISimulationRoadmapSection#fetch()
	 */
	@Override
	public SimulationMilestone fetch() {
		return milestones.poll();
	}

}
