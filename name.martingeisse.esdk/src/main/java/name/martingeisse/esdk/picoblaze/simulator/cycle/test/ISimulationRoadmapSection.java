/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

/**
 * Defines a section in the simulation roadmap. Sections have no
 * higher-level meaning; they are just a tool to define the
 * roadmap in a simple way: The default implementation,
 * {@link SimulationRoadmapSection}, just keeps a list of milestones.
 * Other implementations, however, produce the sequence of
 * milestones in a different way.
 */
public interface ISimulationRoadmapSection {

	/**
	 * Peeks into this section to return the next pending milestone,
	 * or null if there are no milestones left in this section.
	 * 
	 * @return the milestone or null
	 */
	public SimulationMilestone peek();
	
	/**
	 * Fetches the next milestone. Returns null if there are no
	 * milestones left.
	 * 
	 * @return the milestone or null
	 */
	public SimulationMilestone fetch();
	
}
