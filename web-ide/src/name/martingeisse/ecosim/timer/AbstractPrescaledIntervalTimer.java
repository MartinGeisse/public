/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.timer;

/**
 * This timer type works similar to AbstractIntervalTimer. However, it
 * uses two interval lengths and two remaining tick counts. One of
 * each is used for "micro" ticks that pre-divide the tick frequency
 * received by this timer. The others are used for "macro" ticks that
 * actually trigger the onExpire() method.
 */
public abstract class AbstractPrescaledIntervalTimer implements ITickable {

	/**
	 * the microTimer
	 */
	private final MyMicroTimer microTimer;

	/**
	 * the macroTimer
	 */
	private final MyMacroTimer macroTimer;

	/**
	 * Constructor
	 * @param microInterval the initial micro interval length
	 * @param macroInterval the initial macro interval length
	 */
	public AbstractPrescaledIntervalTimer(int microInterval, int macroInterval) {
		this.microTimer = new MyMicroTimer(microInterval);
		this.macroTimer = new MyMacroTimer(macroInterval);
	}

	/**
	 * @return Returns the micro interval.
	 */
	public final int getMicroInterval() {
		return microTimer.getInterval();
	}

	/**
	 * Sets the micro interval.
	 * @param microInterval the new value to set
	 */
	public final void setMicroInterval(int microInterval) {
		microTimer.setInterval(microInterval);
	}

	/**
	 * @return Returns the macro interval.
	 */
	public final int getMacroInterval() {
		return macroTimer.getInterval();
	}

	/**
	 * Sets the macro interval.
	 * @param macroInterval the new value to set
	 */
	public final void setMacroInterval(int macroInterval) {
		macroTimer.setInterval(macroInterval);
	}

	/**
	 * @return Returns the micro ticks left.
	 */
	public final int getMicroTicksLeft() {
		return microTimer.getTicksLeft();
	}

	/**
	 * Sets the micro ticks left.
	 * @param microTicksLeft the new value to set
	 */
	public final void setMicroTicksLeft(int microTicksLeft) {
		microTimer.setTicksLeft(microTicksLeft);
	}

	/**
	 * @return Returns the macro ticks left.
	 */
	public final int getMacroTicksLeft() {
		return macroTimer.getTicksLeft();
	}

	/**
	 * Sets the macro ticks left.
	 * @param macroTicksLeft the new value to set
	 */
	public final void setMacroTicksLeft(int macroTicksLeft) {
		macroTimer.setTicksLeft(macroTicksLeft);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public final void tick() {
		microTimer.tick();
	}
	
	/**
	 * This method is invoked when the timer expires.
	 */
	protected abstract void onExpire();

	/**
	 * The implementation of the micro timer.
	 */
	private final class MyMicroTimer extends AbstractIntervalTimer {

		/**
		 * Constructor
		 * @param interval the initial interval length
		 */
		public MyMicroTimer(int interval) {
			super(interval);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.timer.AbstractIntervalTimer#onExpire()
		 */
		@Override
		protected void onExpire() {
			macroTimer.tick();
		}

	}

	/**
	 * The implementation of the macro timer.
	 */
	private final class MyMacroTimer extends AbstractIntervalTimer {

		/**
		 * Constructor
		 * @param interval the initial interval length
		 */
		public MyMacroTimer(int interval) {
			super(interval);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.timer.AbstractIntervalTimer#onExpire()
		 */
		@Override
		protected void onExpire() {
			AbstractPrescaledIntervalTimer.this.onExpire();
		}

	}

}
