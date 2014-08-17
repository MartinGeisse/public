/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.codemirror.compile;

import java.util.Comparator;

/**
 * Compares compiler markers by error level, putting errors at the start.
 */
public final class CompilerMarkerErrorLevelComparator implements Comparator<CompilerMarker> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(final CompilerMarker o1, final CompilerMarker o2) {
		return o1.getErrorLevel().ordinal() - o2.getErrorLevel().ordinal();
	}

}
