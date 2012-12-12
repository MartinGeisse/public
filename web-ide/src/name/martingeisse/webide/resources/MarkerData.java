/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.webide.entity.Markers;

/**
 * This class keeps data fields for a marker. It does not
 * store the marker's database ID since that is typically
 * not interesting for IDE code.
 * 
 * This class also doesn't store information about the file
 * to which the marker belongs (if any). This is because there
 * are too many ways to represent the file. Instead, code
 * using this class should make it clear from the context to
 * which file the marker belongs (e.g. keep a list of
 * {@link MarkerData} objects per file).
 */
public final class MarkerData implements Serializable {

	/**
	 * Constructor.
	 */
	public MarkerData() {
	}
	
	/**
	 * Constructor.
	 * @param marker the marker to create this object from
	 */
	public MarkerData(Markers marker) {
		this.origin = MarkerOrigin.valueOf(marker.getOrigin());
		this.meaning = MarkerMeaning.valueOf(marker.getMeaning());
		this.line = marker.getLine();
		this.column = marker.getColumn();
		this.message = marker.getMessage();
	}

	/**
	 * the origin
	 */
	private MarkerOrigin origin;

	/**
	 * the meaning
	 */
	private MarkerMeaning meaning;

	/**
	 * the line
	 */
	private Integer line;

	/**
	 * the column
	 */
	private Integer column;

	/**
	 * the message
	 */
	private String message;

	/**
	 * Getter method for the origin.
	 * @return the origin
	 */
	public MarkerOrigin getOrigin() {
		return origin;
	}

	/**
	 * Setter method for the origin.
	 * @param origin the origin to set
	 */
	public void setOrigin(final MarkerOrigin origin) {
		this.origin = origin;
	}

	/**
	 * Getter method for the meaning.
	 * @return the meaning
	 */
	public MarkerMeaning getMeaning() {
		return meaning;
	}

	/**
	 * Setter method for the meaning.
	 * @param meaning the meaning to set
	 */
	public void setMeaning(final MarkerMeaning meaning) {
		this.meaning = meaning;
	}

	/**
	 * Getter method for the line.
	 * @return the line
	 */
	public Integer getLine() {
		return line;
	}

	/**
	 * Setter method for the line.
	 * @param line the line to set
	 */
	public void setLine(final Integer line) {
		this.line = line;
	}

	/**
	 * Getter method for the column.
	 * @return the column
	 */
	public Integer getColumn() {
		return column;
	}

	/**
	 * Setter method for the column.
	 * @param column the column to set
	 */
	public void setColumn(final Integer column) {
		this.column = column;
	}

	/**
	 * Getter method for the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter method for the message.
	 * @param message the message to set
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{MarkerData. origin = " + origin + ", meaning = " + meaning + ", line = " + line + ", column = " + column + ", message = "
			+ message + "}";
	}
	
	/**
	 * Inserts a marker for the specified file with this data into the database.
	 * @param fileId the file to insert the marker for
	 */
	public void insertIntoDatabase(long fileId) {
		MarkerDatabaseUtil.insertMarker(fileId, origin.toString(), meaning.toString(), line, column, message);
	}
	
	/**
	 * Creates a list of {@link MarkerData} objects from a collection of {@link Markers} objects.
	 * @param markers the marker records
	 * @return the marker data objects
	 */
	public static List<MarkerData> createMarkerDataList(Iterable<Markers> markers) {
		List<MarkerData> result = new ArrayList<MarkerData>();
		for (Markers marker : markers) {
			result.add(new MarkerData(marker));
		}
		return result;
	}
	
	/**
	 * Fetches all markers for the specified file.
	 * @param fileId the file ID
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @return the markers
	 */
	public static List<MarkerData> fetchMarkerDataForFile(long fileId, MarkerMeaning[] meaningFilter) {
		return createMarkerDataList(MarkerDatabaseUtil.fetchMarkersForFile(fileId, meaningFilter));
	}
	
	/**
	 * Fetches all markers for the specified file.
	 * @param fileName the file name
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @return the markers
	 */
	public static List<MarkerData> fetchMarkerDataForFile(String fileName, MarkerMeaning[] meaningFilter) {
		return createMarkerDataList(MarkerDatabaseUtil.fetchMarkersForFile(fileName, meaningFilter));
	}
	
}
