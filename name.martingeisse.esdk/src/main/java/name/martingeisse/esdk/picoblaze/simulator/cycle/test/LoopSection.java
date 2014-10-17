/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;


/**
 * This section class acts as a base to implement a sequence of
 * milestones that follow a pattern, yet are not simply a
 * repetition of the same milestone so {@link RepeaterSection}
 * cannot be used.
 */
public abstract class LoopSection implements ISimulationRoadmapSection {

	/**
	 * the cycles
	 */
	private final int cycles;
	
	/**
	 * the index
	 */
	private int index;

	/**
	 * Constructor.
	 * @param cycles the number of loop cycles
	 */
	public LoopSection(int cycles) {
		this.cycles = cycles;
		this.index = 0;
	}
	
	/**
	 * Returns the milestone for the specified cycle index.
	 * @param index the cycle index
	 * @return the milestone
	 */
	protected abstract SimulationMilestone getMilestone(int index);

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.ISimulationRoadmapSection#peek()
	 */
	@Override
	public SimulationMilestone peek() {
		return (index < cycles ? getMilestone(index) : null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.ISimulationRoadmapSection#fetch()
	 */
	@Override
	public SimulationMilestone fetch() {
		if (index < cycles) {
			SimulationMilestone milestone = getMilestone(index);
			index++;
			return milestone;
		} else {
			return null;
		}
	}
	
}
