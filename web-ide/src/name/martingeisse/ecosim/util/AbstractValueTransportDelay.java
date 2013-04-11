/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.util;

import name.martingeisse.ecosim.timer.AbstractIntervalTimer;
import name.martingeisse.ecosim.timer.ITickable;

/**
 * This class implements the common timing behavior of values that
 * are transported over a channel with a certain delay.
 * @param <T> the static type of the transported value
 */
public abstract class AbstractValueTransportDelay<T> implements ITickable {

	/**
	 * the timer. Note: this timer is kept with the remaining tick count
	 * at the full interval length while this object is inactive.
	 */
	private MyTimer timer;
	
	/**
	 * the active
	 */
	private boolean active;
	
	/**
	 * the data
	 */
	private T data;
	
	/**
	 * Constructor
	 * @param delay the transport delay as number of timer ticks
	 */
	public AbstractValueTransportDelay(int delay) {
		this.timer = new MyTimer(delay);
		this.active = false;
		this.data = null;
	}
	
	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Sends a value through the transport delay. This method may only be called
	 * if this object is not active.
	 * @param value the value to send
	 * @throws IllegalStateException if there is already a value on the way
	 */
	public void send(T value) throws IllegalStateException {
		if (active) {
			throw new IllegalStateException("value transport delay is already active");
		} else {
			active = true;
			data = value;
		}
	}
	
	/**
	 * Cancels the current transmission. This method may only be called if
	 * this object is active.
	 * @throws IllegalStateException if there is no value on the way
	 */
	public void cancel() throws IllegalStateException {
		if (!active) {
			throw new IllegalStateException("value transport delay is not active");
		} else {
			active = false;
			timer.reset();
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.timer.ITickable#tick()
	 */
	@Override
	public void tick() {
		if (active) {
			timer.tick();
		}
	}
	
	/**
	 * This method must be implemented by subclasses to handle a value that has arrived.
	 * @param value the arrived value
	 */
	protected abstract void onArrive(T value);
	
	/**
	 * The timer implementation for this class.
	 */
	private class MyTimer extends AbstractIntervalTimer {

		/**
		 * Constructor
		 * @param interval the timer interval
		 */
		public MyTimer(int interval) {
			super(interval);
		}
		
		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.timer.AbstractIntervalTimer#onExpire()
		 */
		@Override
		protected void onExpire() {
			active = false;
			onArrive(data);
		}
		
	}
}
