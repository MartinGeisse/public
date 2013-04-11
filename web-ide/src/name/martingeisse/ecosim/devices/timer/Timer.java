/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */
package name.martingeisse.ecosim.devices.timer;

import name.martingeisse.ecosim.bus.AbstractPeripheralDevice;
import name.martingeisse.ecosim.bus.BusTimeoutException;
import name.martingeisse.ecosim.bus.IInterruptLine;
import name.martingeisse.ecosim.timer.AbstractPrescaledIntervalTimer;

/**
 * 
 */
public class Timer extends AbstractPeripheralDevice {

	/**
	 * the PRESCALE
	 */
	public static final int PRESCALE = 10;

	/**
	 * the expired
	 */
	private boolean expired;

	/**
	 * the interruptEnable
	 */
	private boolean interruptEnable;

	/**
	 * the timer
	 */
	private MySimulationTimer simulationTimer;
	
	/**
	 * the interruptLine
	 */
	private IInterruptLine interruptLine;

	/**
	 * Constructor
	 */
	public Timer() {
		this.expired = false;
		this.interruptEnable = false;
		this.simulationTimer = new MySimulationTimer(0xFFFFFFFF);
		this.interruptLine = null;
	}

	/**
	 * @return Returns the expired.
	 */
	public boolean isExpired() {
		return expired;
	}

	/**
	 * Sets the expired.
	 * @param expired the new value to set
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
		updateInterruptLine();
	}

	/**
	 * @return Returns the interruptEnable.
	 */
	public boolean isInterruptEnable() {
		return interruptEnable;
	}

	/**
	 * Sets the interruptEnable.
	 * @param interruptEnable the new value to set
	 */
	public void setInterruptEnable(boolean interruptEnable) {
		this.interruptEnable = interruptEnable;
		updateInterruptLine();
	}

	/**
	 * @return Returns the counter.
	 */
	public int getCounter() {
		return simulationTimer.getMacroTicksLeft();
	}

	/**
	 * Sets the counter.
	 * @param counter the new value to set
	 */
	public void setCounter(int counter) {
		simulationTimer.setMacroTicksLeft(counter);
	}

	/**
	 * @return Returns the divisor.
	 */
	public int getDivisor() {
		return simulationTimer.getMacroInterval();
	}

	/**
	 * Sets the divisor.
	 * @param divisor the new value to set
	 */
	public void setDivisor(int divisor) {
		simulationTimer.setMacroInterval(divisor);
	}

	/**
	 * Updates the active flag of the interrupt line.
	 */
	private void updateInterruptLine() {
		interruptLine.setActive(isExpired() && isInterruptEnable());
	}

	/**
	 * @return Returns the current value of the control register.
	 */
	public int readControlRegister() {
		return (expired ? 1 : 0) + (interruptEnable ? 2 : 0);
	}

	/**
	 * Writes a value into the control register.
	 * @param value the value to write
	 */
	public void writeControlRegister(int value) {
		this.expired = (value & 1) != 0;
		this.interruptEnable = (value & 2) != 0;
		updateInterruptLine();
	}

	/**
	 * @return Returns the current value of the divisor register.
	 */
	public int readDivisorRegister() {
		return getDivisor();
	}

	/**
	 * Writes a value into the divisor register.
	 * @param value the value to write
	 */
	public void writeDivisorRegister(int value) {
		setDivisor(value);
		setCounter(value);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#readWord(int)
	 */
	@Override
	protected int readWord(int localAddress) throws BusTimeoutException {
		return (localAddress == 0) ? readControlRegister() : readDivisorRegister();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.AbstractPeripheralDevice#writeWord(int, int)
	 */
	@Override
	protected void writeWord(int localAddress, int value) throws BusTimeoutException {
		if (localAddress == 0) {
			writeControlRegister(value);
		} else {
			writeDivisorRegister(value);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#connectInterruptLines(name.martingeisse.ecotools.simulator.bus.IInterruptLine[])
	 */
	@Override
	public void connectInterruptLines(IInterruptLine[] interruptLines) {
		this.interruptLine = interruptLines[0];
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getInterruptLineCount()
	 */
	@Override
	public int getInterruptLineCount() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.ecotools.simulator.bus.IPeripheralDevice#getLocalAddressBitCount()
	 */
	@Override
	public int getLocalAddressBitCount() {
		return 3;
	}

	/**
	 * Advances this timer.
	 */
	@Override
	public void tick() {
		simulationTimer.tick();
	}

	/**
	 * 
	 */
	private class MySimulationTimer extends AbstractPrescaledIntervalTimer {

		/**
		 * Constructor
		 * @param macroInterval the initial macro interval
		 */
		public MySimulationTimer(int macroInterval) {
			super(PRESCALE, macroInterval);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.ecotools.simulator.timer.AbstractPrescaledIntervalTimer#onExpire()
		 */
		@Override
		protected void onExpire() {
			expired = true;
			updateInterruptLine();
		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "timer";
	}

}
