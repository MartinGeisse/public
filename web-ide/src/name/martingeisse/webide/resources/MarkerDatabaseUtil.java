/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.util.ArrayUtil;
import name.martingeisse.webide.entity.Markers;
import name.martingeisse.webide.entity.QFiles;
import name.martingeisse.webide.entity.QMarkers;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * TODO: document me
 *
	ListView zum anzeigen (Icon, Texte)
	setzen beim compile
	anzeigen per ListView/Model
	compile bei save
	run getrennt per button

 */
public class MarkerDatabaseUtil {

	/**
	 * Fetches all markers.
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 * @return the markers
	 */
	public static List<Markers> fetchAllMarkers(MarkerMeaning[] meaningFilter, long limit) {
		if (meaningFilter != null && meaningFilter.length == 0) {
			return new ArrayList<Markers>();
		}
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(QMarkers.markers);
		if (meaningFilter != null) {
			query = query.where(QMarkers.markers.meaning.in(ArrayUtil.toStringArray(meaningFilter)));
		}
		query.limit(limit);
		return query.list(QMarkers.markers);
	}

	/**
	 * Fetches all markers for the specified file.
	 * @param fileId the file ID
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 * @return the markers
	 */
	public static List<Markers> fetchMarkersForFile(long fileId, MarkerMeaning[] meaningFilter, long limit) {
		if (meaningFilter != null && meaningFilter.length == 0) {
			return new ArrayList<Markers>();
		}
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(QMarkers.markers);
		query = query.where(QMarkers.markers.fileId.eq(fileId));
		if (meaningFilter != null) {
			query = query.where(QMarkers.markers.meaning.in(ArrayUtil.toStringArray(meaningFilter)));
		}
		query.limit(limit);
		return query.list(QMarkers.markers);
	}

	/**
	 * Fetches all markers for the specified file.
	 * @param fileName the file name
	 * @param meaningFilter optional list of accepted marker meanings. If null is
	 * passed for this parameter then all markers are fetched.
	 * @param limit the maximum number of markers to fetch
	 * @return the markers
	 */
	public static List<Markers> fetchMarkersForFile(String fileName, MarkerMeaning[] meaningFilter, long limit) {
		if (meaningFilter != null && meaningFilter.length == 0) {
			return new ArrayList<Markers>();
		}
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query = query.from(QMarkers.markers, QFiles.files);
		query = query.where(QMarkers.markers.fileId.eq(QFiles.files.id));
		query = query.where(QFiles.files.name.eq(fileName));
		if (meaningFilter != null) {
			query = query.where(QMarkers.markers.meaning.in(ArrayUtil.toStringArray(meaningFilter)));
		}
		query.limit(limit);
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
	 * Removes all markers from the specified files. If origin is set, then only markers
	 * with that origin are deleted, otherwise all markers are deleted.
	 * @param fileIds the file IDs
	 * @param origin the origin (to delete specific markers) or null (to delete all markers)
	 */
	public static void removeMarkersForFile(Collection<Long> fileIds, MarkerOrigin origin) {
		if (fileIds.isEmpty()) {
			return;
		}
		SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QMarkers.markers);
		delete.where(QMarkers.markers.fileId.in(fileIds));
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
	public static void insertMarker(long fileId, String origin, String meaning, Long line, Long column, String message) {
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
