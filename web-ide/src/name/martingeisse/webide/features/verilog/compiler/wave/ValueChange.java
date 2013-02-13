/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.compiler.wave;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a value change on a hardware signal. The time of the change
 * is given as a long value with unspecified scale. This object stores
 * the new value; the old value should be obtained from the previous
 * change in a list of changes.
 * 
 * The value can use various types. Which types are supported ultimately
 * depends on the renderer; standard types are:
 * 
 * - null: the new value is unknown or undefined
 * - boolean: used to represent single-wire digital signals
 * - integer: used to represent numbers or multi-wire digital signals
 * - string: displayed literally by renderers
 * - CHAOS: used by the renderer as a replacement for too many changes
 *   in a short time to display them properly
 * 
 * This class is intended to be immutable; clients should use either
 * immutable values or respect immutability by not changing values.
 */
public class ValueChange {

	/**
	 * This object is used to represent CHAOS values (see class comment).
	 */
	public static final Object CHAOS = new Object();

	/**
	 * This object is used to represent Z (high-impedance) values.
	 */
	public static final Object VALUE_Z = new Object();
	
	/**
	 * Shared instance of the {@link TimeComparator} class.
	 */
	public static final TimeComparator TIME_COMPARATOR = new TimeComparator();

	/**
	 * the time
	 */
	private final long time;

	/**
	 * the value
	 */
	private final Object value;

	/**
	 * Constructor.
	 * @param time the time of this change
	 * @param value the new value
	 */
	public ValueChange(final long time, final Object value) {
		this.time = time;
		this.value = value;
	}

	/**
	 * Getter method for the time.
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Getter method for the value.
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Returns a new instance with the specified time and with the value from
	 * this object.
	 * 
	 * @param time the time to use for the new object
	 * @return the new object
	 */
	public ValueChange withTime(final long time) {
		return new ValueChange(time, value);
	}

	/**
	 * Returns a new instance with the specified value and with the time from
	 * this object.
	 * 
	 * @param value the value to use for the new object
	 * @return the new object
	 */
	public ValueChange withValue(final Object value) {
		return new ValueChange(time, value);
	}

	/**
	 * Generic method used to transform the time of this change. It basically consists
	 * of a factor and an added offset, so it can represent general shift/scale operations.
	 * 
	 * The shift is split into a preOffset and a postOffset, allowing to shift both before
	 * and after the multiplication -- this simplifies things for the caller.
	 * 
	 * The factor is represented as a long value that stores 1/256 of the actual factor;
	 * this allows to use non-integral factors.
	 * 
	 * NOTE: The current implementation will overflow if new time exceeds 2^24, not only
	 * at 2^32.
	 * 
	 * @param preOffset the offset to add before the multiplication
	 * @param factorShifted8 1/256 of the factor to apply
	 * @param postOffset the offset to add after the multiplication
	 * @return the new change object
	 */
	public ValueChange transformTime(final long preOffset, final long factorShifted8, final long postOffset) {
		final long transformed = postOffset + (((preOffset + time) * factorShifted8) >> 8);
		return new ValueChange(transformed, value);
	}

	/**
	 * Applies transformTime() to a list of value changes.
	 * @param valueChanges the list of value changes
	 * @param preOffset the offset to add before the multiplication
	 * @param factorShifted8 1/256 of the factor to apply
	 * @param postOffset the offset to add after the multiplication
	 * @return the new change objects
	 */
	public static List<ValueChange> transformTime(final List<ValueChange> valueChanges, final long preOffset, final long factorShifted8, final long postOffset) {
		final List<ValueChange> result = new ArrayList<ValueChange>();
		for (final ValueChange original : valueChanges) {
			result.add(original.transformTime(preOffset, factorShifted8, postOffset));
		}
		return result;
	}

	/**
	 * Compares ValueChanges by time.
	 */
	public static class TimeComparator implements Comparator<ValueChange> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(final ValueChange a, final ValueChange b) {
			return Long.signum(a.getTime() - b.getTime());
		}

	}

}
