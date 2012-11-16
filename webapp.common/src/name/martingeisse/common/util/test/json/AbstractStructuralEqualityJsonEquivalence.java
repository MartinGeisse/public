/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.common.util.test.json;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.martingeisse.common.util.ParameterUtil;

/**
 * Base class for equivalences that require structural equality. That is,
 * lists are only equivalent to lists with the same number and equivalent
 * elements, and objects are only equivalent to objects with the same
 * keys and equivalent values for matching keys. 
 * 
 * Subclasses must define equivalence on primitive values.
 */
public abstract class AbstractStructuralEqualityJsonEquivalence extends AbstractJsonEquivalence {

	/* (non-Javadoc)
	 * @see name.martingeisse.common.util.test.json.AbstractJsonEquivalence#assertEquivalent(java.lang.Object, java.lang.Object, java.util.LinkedList)
	 */
	@Override
	public void assertEquivalent(Object x, Object y, LinkedList<String> contextStack) {
		check(x, y, ParameterUtil.ensureNotNull(contextStack, "contextStack"));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.util.test.json.IJsonEquivalence#equivalent(java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean equivalent(Object x, Object y) {
		return check(x, y, null);
	}

	/**
	 * Common handling for {@link #assertEquivalent(Object, Object)} and
	 * {@link #equivalent(Object, Object)}.
	 */
	private boolean check(Object x, Object y, LinkedList<String> assertContextStack) {

		// check for lists
		if (x instanceof List<?>) {
			if (y instanceof List<?>) {
				List<?> xl = (List<?>)x;
				List<?> yl = (List<?>)y;
				if (xl.size() != yl.size()) {
					return nonEquivalent(xl, yl, assertContextStack, "found different-sized lists");
				}
				Iterator<?> xi = xl.iterator();
				Iterator<?> yi = yl.iterator();
				int i = 0;
				while (xi.hasNext()) {
					Object xe = xi.next();
					Object ye = yi.next();
					boolean elementsEquivalent = checkElementsEquivalent(xe, ye, assertContextStack, Integer.toString(i));
					if (!elementsEquivalent) {
						return false;
					}
					i++;
				}
			} else {
				return nonEquivalent(x, y, assertContextStack, "list vs. non-list");
			}
		} else if (y instanceof List<?>) {
			return nonEquivalent(x, y, assertContextStack, "non-list vs. list");
		}

		// check for objects
		if (x instanceof Map<?, ?>) {
			if (y instanceof Map<?, ?>) {
				Map<?, ?> xm = (Map<?, ?>)x;
				Map<?, ?> ym = (Map<?, ?>)y;
				if (xm.size() != ym.size()) {
					return nonEquivalent(xm, ym, assertContextStack, new StructuralObjectDifferenceMessage(xm, ym));
				}
				for (Map.Entry<?, ?> xEntry : xm.entrySet()) {
					String key = xEntry.getKey().toString();
					Object xe = xEntry.getValue();
					if (!ym.containsKey(key)) {
						return nonEquivalent(x, y, assertContextStack, new StructuralObjectDifferenceMessage(xm, ym));
					}
					Object ye = ym.get(key);
					boolean elementsEquivalent = checkElementsEquivalent(xe, ye, assertContextStack, key);
					if (!elementsEquivalent) {
						return false;
					}
				}
			} else {
				return nonEquivalent(x, y, assertContextStack, "object vs. non-object");
			}
		} else if (y instanceof Map<?, ?>) {
			return nonEquivalent(x, y, assertContextStack, "non-object vs. object");
		}

		// primitive values
		return primitiveValuesEquivalent(x, y, assertContextStack);

	}

	/**
	 * Compares two primitive values for equivalence.
	 * @param x the first value to compare
	 * @param y the second value to compare
	 * @return true if equivalent, false if not
	 */
	protected abstract boolean primitiveValuesEquivalent(Object x, Object y, LinkedList<String> assertContextStack);

	private static class StructuralObjectDifferenceMessage {

		/**
		 * the x
		 */
		private Map<?, ?> x;

		/**
		 * the y
		 */
		private Map<?, ?> y;

		/**
		 * Constructor.
		 */
		StructuralObjectDifferenceMessage(Map<?, ?> x, Map<?, ?> y) {
			this.x = x;
			this.y = y;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			buildKeys(builder, x, y, "first");
			buildKeys(builder, y, x, "second");
			return builder.toString();
		}
		
		private void buildKeys(StringBuilder builder, Map<?, ?> thisObject, Map<?, ?> otherObject, String description) {
			Set<Object> thisOnly = new HashSet<Object>(thisObject.keySet());
			thisOnly.removeAll(otherObject.keySet());
			if (!thisOnly.isEmpty()) {
				builder.append("keys only in the " + description + " object: [");
				boolean first = true;
				for (Object key : thisOnly) {
					if (first) {
						first = false;
					} else {
						builder.append(", ");
					}
					builder.append(key);
				}
				builder.append("] ");
			}
		}

	}

}
