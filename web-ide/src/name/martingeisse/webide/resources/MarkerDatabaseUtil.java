/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.resources;

import java.util.List;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.Markers;
import name.martingeisse.webide.entity.QFiles;
import name.martingeisse.webide.entity.QMarkers;

/**
 * TODO: document me
 *
	Model zum abrufen (parameter: {file, origins, meanings} (zum filtern), accept() (zum in-memory filtern))
	ListView zum anzeigen (Icon, Texte)
		
	setzen beim compile
	anzeigen per ListView/Model
	compile bei save
	run getrennt per button

 */
public class MarkerDatabaseUtil {

	/**
	 * Fetches all markers for the specified file.
	 * @param fileId the file ID
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @return the markers
	 */
	public static List<Markers> fetchMarkersForFile(long fileId, MarkerMeaning[] meaningFilter) {
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(QMarkers.markers);
		query = query.where(QMarkers.markers.fileId.eq(fileId));
		if (meaningFilter != null) {
			query = query.where(QMarkers.markers.meaning.in(meaningFilter));
		}
		return query.list(QMarkers.markers);
	}
	
	/**
	 * Fetches all markers for the specified file.
	 * @param fileName the file name
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @return the markers
	 */
	public static List<Markers> fetchMarkersForFile(String fileName, MarkerMeaning[] meaningFilter) {
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(QMarkers.markers, QFiles.files);
		query = query.where(QMarkers.markers.fileId.eq(QFiles.files.id));
		query = query.where(QFiles.files.name.eq(fileName));
		if (meaningFilter != null) {
			query = query.where(QMarkers.markers.meaning.in(meaningFilter));
		}
		return query.list(QMarkers.markers);
	}

	/**
	 * Removes all markers from the specified file. If origin is set, then only markers
	 * with that origin are deleted, otherwise all markers are deleted.
	 * @param fileId the file ID
	 * @param origin the origin (to delete specific markers) or null (to delete all markers)
	 */
	public static void removeMarkersForFile(long fileId, MarkerOrigin origin) {
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.fileId.eq(fileId));
		if (origin != null) {
			delete.where(QMarkers.markers.origin.eq(origin.toString()));
		}
		delete.execute();
	}
	
	/**
	 * Inserts a marker into the database.
	 * @param fileId the ID of the file to which the marker belongs
	 * @param origin the origin of the marker
	 * @param meaning the meaning of the marker
	 * @param line the line of the marker's position
	 * @param column the column of the marker's position
	 * @param message the marker message
	 */
	public static void insertMarker(long fileId, String origin, String meaning, int line, int column, String message) {
		SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QMarkers.markers);
		insert.set(QMarkers.markers.fileId, fileId);
		insert.set(QMarkers.markers.origin, origin);
		insert.set(QMarkers.markers.meaning, meaning);
		insert.set(QMarkers.markers.line, line);
		insert.set(QMarkers.markers.column, column);
		insert.set(QMarkers.markers.message, message);
		insert.execute();
	}
	
}
