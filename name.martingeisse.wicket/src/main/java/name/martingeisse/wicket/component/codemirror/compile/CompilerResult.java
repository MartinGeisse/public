/**
 * Copyright (c) 2015 Martin Geisse
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
	 * the document
	 */
	private final String document;

	/**
	 * the overallErrorLevel
	 */
	private CompilerErrorLevel overallErrorLevel;

	/**
	 * the markers
	 */
	private final List<CompilerMarker> markers = new ArrayList<>();

	/**
	 * Constructor.
	 * @param document the document being compiled
	 */
	public CompilerResult(final String document) {
		this.document = document;
	}
	
	/**
	 * Getter method for the document.
	 * @return the document
	 */
	public String getDocument() {
		return document;
	}

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
