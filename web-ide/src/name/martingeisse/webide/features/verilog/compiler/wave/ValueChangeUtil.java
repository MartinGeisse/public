/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.compiler.wave;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility methods used to deal with {@link ValueChange} objects.
 * 
 * The methods in this class assume lists of value changes to be
 * pre-sorted by time. These methods assume that multiple value
 * changes with the "same" time might actually differ in time by
 * an amount less than 1, but that these fractional amounts are
 * considered in the list order.
 */
public final class ValueChangeUtil {

	/**
	 * Prevent instantiation.
	 */
	private ValueChangeUtil() {
	}
	
	/**
	 * Returns a list containing only value changes relevant for displaying
	 * the specfied time range. This includes the value changes within the
	 * time range AND the last change just before the range (since it defines
	 * the intial value of the range).
	 * 
	 * @param valueChanges the list of value changes
	 * @param startTime the start time of the range
	 * @param endTime the end time of the range
	 * @return the relevant value changes
	 */
	public static List<ValueChange> cropForDisplay(List<ValueChange> valueChanges, long startTime, long endTime) {
		
		// find the value change that defines the initial value
		ValueChange initialValueChange = null;
		for (ValueChange change : valueChanges) {
			if (change.getTime() >= startTime) {
				break;
			}
			initialValueChange = change;
		}
		
		// add this initial change
		List<ValueChange> result = new ArrayList<ValueChange>();
		if (initialValueChange != null) {
			result.add(initialValueChange);
		}
		
		// add all changes in the time range
		for (ValueChange change : valueChanges) {
			if (change.getTime() >= startTime) {
				result.add(change);
			}
		}
		
		return result;
	}
	
	/**
	 * Performs corrections for displaying the specified value change list.
	 * This handles redundant changes and too-close changes.
	 * 
	 * @param valueChanges the raw list of changes
	 * @return the corrected list of changes for displaying
	 */
	public static List<ValueChange> prepareForDisplay(List<ValueChange> valueChanges) {
		
		// First, remove redundant changes. This must happen before we can detect CHAOS.
		valueChanges = removeRedundantChanges(valueChanges);
		
		// now mark the places where CHAOS occurs
		valueChanges = replaceChaos(valueChanges, 2);
		
		// collapse multiple CHAOS changes in a row into a single one
		valueChanges = removeRedundantChanges(valueChanges);
		
		// finally, merge CHAOS changes into the next change if the CHAOS is too short
		valueChanges = mergeChanges(valueChanges);
		
		return valueChanges;
	}

	/**
	 * Removes changes with an equal value as the previous change. This method
	 * uses null-safe equals() comparison.
	 * 
	 * No initial value is assumed, i.e. the first element of the input
	 * list is always preserved, even if its value is null.
	 * 
	 * The input value changes must be pre-sorted by time. The output value
	 * changes are guaranteed to be sorted by time.
	 * 
	 * @param valueChanges the changes
	 * @return the input list without redundant changes
	 */
	private static List<ValueChange> removeRedundantChanges(List<ValueChange> valueChanges) {
		LinkedList<ValueChange> result = new LinkedList<ValueChange>();
		boolean first = true;
		Object previousValue = null;
		for (ValueChange change : valueChanges) {
			if (first || !nullSafeEquals(previousValue, change.getValue())) {
				result.add(change);
			}
			first = false;
			previousValue = change.getValue();
		}
		return result;
	}
	
	/**
	 * Performs a null-safe equals() comparison.
	 */
	private static boolean nullSafeEquals(Object a, Object b) {
		if (a == b) {
			return true;
		} else if (a == null) {
			return false;
		} else if (b == null) {
			return false;
		} else {
			return a.equals(b);
		}
	}
	
	/**
	 * Replaces value changes by CHAOS changes if the next change is closer
	 * than the specified resolution.
	 * 
	 * The input value changes must be pre-sorted by time. The output value
	 * changes are guaranteed to be sorted by time.
	 * 
	 * @param valueChanges the changes
	 * @param resolution the resolution
	 * @return the new value changes
	 */
	public static List<ValueChange> replaceChaos(List<ValueChange> valueChanges, long resolution) {
		LinkedList<ValueChange> result = new LinkedList<ValueChange>();
		ValueChange previousChange = null;
		for (ValueChange change : valueChanges) {
			if (previousChange == null) {
				previousChange = change;
			} else {
				if (change.getTime() - previousChange.getTime() < resolution) {
					previousChange = previousChange.withValue(ValueChange.CHAOS);
				}
				result.add(previousChange);
				previousChange = change;
			}
		}
		if (previousChange != null) {
			result.add(previousChange);
		}
		return result;
	}

	/**
	 * Drops changes with the same time as the next change.
	 * 
	 * The input value changes must be pre-sorted by time. The output value
	 * changes are guaranteed to be sorted by time.
	 * 
	 * @param valueChanges the changes
	 * @return the input list without redundant changes
	 */
	private static List<ValueChange> mergeChanges(List<ValueChange> valueChanges) {
		LinkedList<ValueChange> result = new LinkedList<ValueChange>();
		ValueChange previousChange = null;
		for (ValueChange change : valueChanges) {
			if (previousChange == null) {
				previousChange = change;
			} else {
				if (change.getTime() != previousChange.getTime()) {
					result.add(previousChange);
				}
				previousChange = change;
			}
		}
		if (previousChange != null) {
			result.add(previousChange);
		}
		return result;
	}
	
}
