/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.rtl.structure.basic_parts;

import name.martingeisse.esdk.rtl.IClockSignalConsumer;
import name.martingeisse.esdk.rtl.structure.ValueNexus;
import name.martingeisse.esdk.util.IValueSource;

/**
 * A clocked register.
 */
public final class Register<T> extends ValueNexus<T, Register<T>> implements IClockSignalConsumer, IValueSource<T> {

	/**
	 * the source
	 */
	private IValueSource<T> source;

	/**
	 * the clockEnable
	 */
	private IValueSource<Boolean> clockEnable;
	
	/**
	 * the currentValue
	 */
	private T currentValue;
	
	/**
	 * the nextValue
	 */
	private T nextValue;

	/**
	 * Constructor.
	 * @param source the value source to load the register from
	 */
	public Register(IValueSource<T> source) {
		this.source = source;
	}

	/**
	 * Constructor.
	 * @param source the value source to load the register from
	 * @param clockEnable the clock-enable signal that determines for
	 * every clock event whether the register will be loaded
	 */
	public Register(IValueSource<T> source, IValueSource<Boolean> clockEnable) {
		this.source = source;
		this.clockEnable = clockEnable;
	}
	
	/**
	 * Constructor.
	 * @param source the value source to load the register from
	 */
	public Register(IValueSource<T> source, T initialValue) {
		this.source = source;
		this.currentValue = initialValue;
	}
	
	/**
	 * Constructor.
	 * @param source the value source to load the register from
	 * @param clockEnable the clock-enable signal that determines for
	 * every clock event whether the register will be loaded
	 */
	public Register(IValueSource<T> source, T initialValue, IValueSource<Boolean> clockEnable) {
		this.source = source;
		this.clockEnable = clockEnable;
		this.currentValue = initialValue;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.structure.ValueNexus#getThis()
	 */
	@Override
	protected Register<T> getThis() {
		return this;
	}
	
	/**
	 * Getter method for the source.
	 * @return the source
	 */
	public IValueSource<T> getSource() {
		return source;
	}

	/**
	 * Setter method for the source.
	 * @param source the source to set
	 */
	public void setSource(IValueSource<T> source) {
		this.source = source;
	}

	/**
	 * Getter method for the clockEnable.
	 * @return the clockEnable
	 */
	public IValueSource<Boolean> getClockEnable() {
		return clockEnable;
	}

	/**
	 * Setter method for the clockEnable.
	 * @param clockEnable the clockEnable to set
	 */
	public void setClockEnable(IValueSource<Boolean> clockEnable) {
		this.clockEnable = clockEnable;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.util.IValueSource#getValue()
	 */
	@Override
	public T getValue() {
		return currentValue;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.IClockSignalConsumer#computeNextState()
	 */
	@Override
	public void computeNextState() {
		nextValue = (clockEnable == null || clockEnable.getValue() ? source.getValue() : currentValue);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.rtl.IClockSignalConsumer#enterNextState()
	 */
	@Override
	public void enterNextState() {
		currentValue = nextValue;
	}

}
