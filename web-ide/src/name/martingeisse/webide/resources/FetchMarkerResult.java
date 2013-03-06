/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.resources;

import java.io.Serializable;

import name.martingeisse.webide.entity.Markers;

/**
 * This class keeps data fields for a marker fetched from
 * the database. It does not store the marker's database
 * ID since that is typically not interesting for IDE code.
 */
public final class FetchMarkerResult implements Serializable {

	/**
	 * the resourceHandle
	 */
	private final ResourceHandle resourceHandle;
	
	/**
	 * the origin
	 */
	private final MarkerOrigin origin;

	/**
	 * the meaning
	 */
	private final MarkerMeaning meaning;

	/**
	 * the line
	 */
	private final Long line;

	/**
	 * the column
	 */
	private final Long column;

	/**
	 * the message
	 */
	private final String message;

	/**
	 * Constructor.
	 * @param marker the marker to create this object from
	 */
	FetchMarkerResult(Markers marker) {
		this.resourceHandle = new ResourceHandle(marker.getWorkspaceId(), new ResourcePath(marker.getPath()));
		this.origin = MarkerOrigin.valueOf(marker.getOrigin());
		this.meaning = MarkerMeaning.valueOf(marker.getMeaning());
		this.line = marker.getLine();
		this.column = marker.getColumn();
		this.message = marker.getMessage();
	}

	/**
	 * Getter method for the resourceHandle.
	 * @return the resourceHandle
	 */
	public ResourceHandle getResourceHandle() {
		return resourceHandle;
	}
	
	/**
	 * Getter method for the origin.
	 * @return the origin
	 */
	public MarkerOrigin getOrigin() {
		return origin;
	}

	/**
	 * Getter method for the meaning.
	 * @return the meaning
	 */
	public MarkerMeaning getMeaning() {
		return meaning;
	}

	/**
	 * Getter method for the line.
	 * @return the line
	 */
	public Long getLine() {
		return line;
	}

	/**
	 * Getter method for the column.
	 * @return the column
	 */
	public Long getColumn() {
		return column;
	}

	/**
	 * Getter method for the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{FetchMarkerResult. resource = " + resourceHandle + ", origin = " + origin + ", meaning = " + meaning + ", line = " + line + ", column = " + column + ", message = " + message + "}";
	}

}
