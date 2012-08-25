/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.operation;

/**
 * Handling mode for "mix-in" handling that is provided by "foreach"
 * operations. This mode decides whether a specific kind of handling
 * shall be applied globally (once for the whole list), locally (once
 * for each element), none or both.
 */
public enum ForeachHandlingMode {

	/**
	 * Apply handling neither to the whole list nor to individual elements.
	 */
	NONE(false, false),
	
	/**
	 * Apply handling to the whole list, but not to individual elements.
	 */
	GLOBAL(true, false),
	
	/**
	 * Apply handling to individual elements, but not to the whole list.
	 */
	LOCAL(false, true),
	
	/**
	 * Apply handling both to the whole list and to individual elements.
	 */
	BOTH(true, true);

	/**
	 * the includesGlobal
	 */
	private boolean includesGlobal;
	
	/**
	 * the includesLocal
	 */
	private boolean includesLocal;
	
	/**
	 * Constructor.
	 * @param includesGlobal whether this mode includes global handling (true for GLOBAL and BOTH)
	 * @param includesLocal whether this mode includes local handling (true for LOCAL and BOTH)
	 */
	private ForeachHandlingMode(boolean includesGlobal, boolean includesLocal) {
		this.includesGlobal = includesGlobal;
		this.includesLocal = includesLocal;
	}

	/**
	 * Getter method for the includesGlobal.
	 * @return the includesGlobal
	 */
	public boolean isIncludesGlobal() {
		return includesGlobal;
	}
	
	/**
	 * Getter method for the includesLocal.
	 * @return the includesLocal
	 */
	public boolean isIncludesLocal() {
		return includesLocal;
	}
	
}
