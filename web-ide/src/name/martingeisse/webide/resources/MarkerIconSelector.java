/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import name.martingeisse.webide.entity.Markers;
import name.martingeisse.webide.resources.operation.FetchMarkerResult;
import name.martingeisse.wicket.icons.silk.Dummy;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * This enum type selects an icon for a marker.
 */
public enum MarkerIconSelector {

	/**
	 * used for warning markers
	 */
	WARNING("error.png"),

	/**
	 * used for error markers
	 */
	ERROR("exclamation.png"),

	/**
	 * used if no other icon is appropriate
	 */
	MISSING_ICON("tag_blue.png");

	/**
	 * the filename
	 */
	private final String filename;

	/**
	 * Constructor.
	 * @param filename the simple icon filename
	 */
	private MarkerIconSelector(String filename) {
		this.filename = filename;
	}

	/**
	 * Getter method for the filename.
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * @return a resource reference for the icon
	 */
	public ResourceReference getResourceReference() {
		return new PackageResourceReference(Dummy.class, filename);
	}

	/**
	 * Returns the appropriate icon selector for the specified marker.
	 * @param marker the marker
	 * @return the icon selector
	 */
	public static MarkerIconSelector get(Markers marker) {
		return get(MarkerMeaning.valueOf(marker.getMeaning()));
	}

	/**
	 * Returns the appropriate icon selector for the specified marker.
	 * @param marker the marker
	 * @return the icon selector
	 */
	public static MarkerIconSelector get(FetchMarkerResult marker) {
		return get(marker.getMeaning());
	}

	/**
	 * Returns the appropriate icon selector for the specified marker meaning.
	 * @param meaning the marker meaning
	 * @return the icon selector
	 */
	public static MarkerIconSelector get(MarkerMeaning meaning) {
		if (meaning == MarkerMeaning.WARNING) {
			return WARNING;
		} else if (meaning == MarkerMeaning.ERROR) {
			return ERROR;
		} else {
			return MISSING_ICON;
		}
	}

}
