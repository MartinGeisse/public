/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util.test.json;

import java.util.LinkedList;

/**
 * Checks equality of values as defined by JSON.
 */
public class JsonEquality extends AbstractStructuralEqualityJsonEquivalence {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.util.test.json.AbstractStructuralEqualityJsonEquivalence#primitiveValuesEquivalent(java.lang.Object, java.lang.Object, java.util.LinkedList)
	 */
	@Override
	protected boolean primitiveValuesEquivalent(Object x, Object y, LinkedList<String> assertContextStack) {
		
		// handle null
		if ((x == null) != (y == null)) {
			return nonEquivalent(x, y, assertContextStack, x == null ? "null vs. non-null" : "non-null vs. null");
		} else if (x == null) {
			return true;
		}
		
		// handle normal primitive values
		if (!x.equals(y)) {
			return nonEquivalent(x, y, assertContextStack, "primitive values not equal");
		} else {
			return true;
		}
		
	}
	
}
