/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.picoblaze.simulator.cycle.test;

/**
 * The roadmap throws this exception when it has no more
 * remaining milestones. This in turn causes the test harness
 * to finish the simulation successfully.
 */
public final class RoadmapFinishedException extends RuntimeException {
}
