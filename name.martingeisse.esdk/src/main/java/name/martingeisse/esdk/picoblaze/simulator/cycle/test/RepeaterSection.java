/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;


/**
 * This section just keeps a repetition of the same milestone.
 */
public final class RepeaterSection implements ISimulationRoadmapSection {

	/**
	 * the milestone
	 */
	private final SimulationMilestone milestone;

	/**
	 * the repetitions
	 */
	private int repetitions;

	/**
	 * Constructor.
	 * @param milestone the milestone
	 * @param repetitions the number of repetitions
	 */
	public RepeaterSection(SimulationMilestone milestone, int repetitions) {
		this.milestone = milestone;
		this.repetitions = repetitions;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.ISimulationRoadmapSection#peek()
	 */
	@Override
	public SimulationMilestone peek() {
		return (repetitions > 0 ? milestone : null);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.picoblaze.simulator.cycle.test.ISimulationRoadmapSection#fetch()
	 */
	@Override
	public SimulationMilestone fetch() {
		if (repetitions > 0) {
			repetitions--;
			return milestone;
		} else {
			return null;
		}
	}
	
}
