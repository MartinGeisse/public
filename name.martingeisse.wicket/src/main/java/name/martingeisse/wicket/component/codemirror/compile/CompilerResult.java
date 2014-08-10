/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.component.codemirror.compile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The result returned by the compiler for a document.
 */
public final class CompilerResult implements Serializable {

	/**
	 * the overallErrorLevel
	 */
	private CompilerErrorLevel overallErrorLevel;

	/**
	 * the markers
	 */
	private final List<CompilerMarker> markers = new ArrayList<>();

	/**
	 * Getter method for the overallErrorLevel.
	 * @return the overallErrorLevel
	 */
	public CompilerErrorLevel getOverallErrorLevel() {
		return overallErrorLevel;
	}

	/**
	 * Setter method for the overallErrorLevel.
	 * @param overallErrorLevel the overallErrorLevel to set
	 */
	public void setOverallErrorLevel(final CompilerErrorLevel overallErrorLevel) {
		this.overallErrorLevel = overallErrorLevel;
	}

	/**
	 * Getter method for the markers.
	 * @return the markers
	 */
	public List<CompilerMarker> getMarkers() {
		return markers;
	}

}
