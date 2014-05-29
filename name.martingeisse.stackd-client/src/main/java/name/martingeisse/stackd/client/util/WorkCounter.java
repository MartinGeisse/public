/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Utility class to count how often a piece of work
 * was done. Used to optimize performance.
 * 
 * Work is measured in cycles with a period length
 * specified in the constructor. Whenever the
 * corresponding piece of work is done, the {@link #count()}
 * method must be called. After a unit of time, the
 * {@link #tick()} method must be called. Every
 * (period) calls to the tick function, stats are
 * updated and the {@link #onCycle()} method gets
 * called (allowing subclasses to react to such updates).
 */
public class WorkCounter {

	/**
	 * the period
	 */
	private final int period;
	
	/**
	 * the record
	 */
	private final List<Integer> record;
	
	/**
	 * the currentWork
	 */
	private int currentWork;
	
	/**
	 * the stats
	 */
	private int[] stats;
	
	/**
	 * the minimumWork
	 */
	private int minimumWork;
	
	/**
	 * the maximumWork
	 */
	private int maximumWork;
	
	/**
	 * the averageWork
	 */
	private int averageWork;
	
	/**
	 * Constructor.
	 * @param period the period
	 */
	public WorkCounter(final int period) {
		if (period < 1) {
			throw new IllegalArgumentException("period argument must be at least 1");
		}
		this.period = period;
		this.record = new ArrayList<Integer>();
		this.currentWork = 0;
		this.stats = new int[period];
		this.minimumWork = 0;
		this.maximumWork = 0;
		this.averageWork = 0;
	}
	
	/**
	 * Getter method for the period.
	 * @return the period
	 */
	public final int getPeriod() {
		return period;
	}
	
	/**
	 * Counts a unit of work.
	 */
	public void count() {
		currentWork++;
	}
	
	/**
	 * Counts a unit of time.
	 */
	public void tick() {
		record.add(currentWork);
		currentWork = 0;
		if (record.size() == period) {
			minimumWork = Integer.MAX_VALUE;
			maximumWork = Integer.MIN_VALUE;
			averageWork = 0;
			int i = 0;
			for (Integer work : record) {
				stats[i] = work;
				if (work < minimumWork) {
					minimumWork = work;
				}
				if (work > maximumWork) {
					maximumWork = work;
				}
				averageWork += work;
				i++;
			}
			averageWork /= period;
			record.clear();
			onCycle();
		}
	}
	
	/**
	 * This method is called whenever the specified period
	 * is completed, after stats have been updated. Subclasses
	 * can override this method to react to such updates.
	 * The default implementation does nothing.
	 */
	protected void onCycle() {
	}
	
	/**
	 * Getter method for the stats.
	 * @return the stats
	 */
	public final int[] getStats() {
		return stats;
	}
	
	/**
	 * Getter method for the minimumWork.
	 * @return the minimumWork
	 */
	public final int getMinimumWork() {
		return minimumWork;
	}
	
	/**
	 * Getter method for the maximumWork.
	 * @return the maximumWork
	 */
	public final int getMaximumWork() {
		return maximumWork;
	}
	
	/**
	 * Getter method for the averageWork.
	 * @return the averageWork
	 */
	public final int getAverageWork() {
		return averageWork;
	}
	
	/**
	 * Work counter implementation that logs min/max/average at INFO level.
	 * The full stats are not printed by default.
	 */
	public static class WorkLogger extends WorkCounter {
		
		/**
		 * the logger
		 */
		private static Logger logger = Logger.getLogger(WorkCounter.WorkLogger.class);
		
		/**
		 * the description
		 */
		private final String description;
		
		/**
		 * Constructor.
		 * @param period the period
		 * @param description a string that is printed with every change message
		 */
		public WorkLogger(int period, String description) {
			super(period);
			this.description = description;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.stackd.util.WorkCounter#onCycle()
		 */
		@Override
		protected void onCycle() {
			logger.info(description + ": min = " + getMinimumWork() + ", max = " + getMaximumWork() + ", avg = " + getAverageWork());
		}
		
	}
	
}
