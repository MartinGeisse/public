/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources;

import java.nio.charset.Charset;
import java.util.Collection;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.Files;
import name.martingeisse.webide.entity.QFiles;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * Utility methods to deal with workspace resources.
 */
public class WorkspaceUtil {

	/**
	 * Prevent instantiation.
	 */
	private WorkspaceUtil() {
	}

	/**
	 * Creates a file with the specified contents. No file with the same name may
	 * exist yet.
	 * @param filename the filename
	 * @param contents the file contents
	 */
	public static void createFile(String filename, String contents) {
		final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QFiles.files);
		insert.set(QFiles.files.name, filename);
		insert.set(QFiles.files.contents, (contents == null ? "" : contents).getBytes(Charset.forName("utf-8")));
		insert.execute();
	}
	
	/**
	 * Creates a file with the specified contents. No file with the same name may
	 * exist yet.
	 * @param filename the filename
	 * @param contents the file contents
	 */
	public static void createFile(String filename, byte[] contents) {
		final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QFiles.files);
		insert.set(QFiles.files.name, filename);
		insert.set(QFiles.files.contents, (contents == null ? new byte[0] : contents));
		insert.execute();
	}
	
	/**
	 * Replaces the contents of the specified file with the specified contents.
	 * Has no effect if the file does not exist.
	 * 
	 * @param filename the name of the file
	 * @param contents the new contents (null is treated like the empty string).
	 * Will be UTF-8 encoded to obtain the new binary contents of the file.
	 */
	public static void replaceContents(String filename, String contents) {
		final SQLUpdateClause update = EntityConnectionManager.getConnection().createUpdate(QFiles.files);
		update.where(QFiles.files.name.eq(filename));
		update.set(QFiles.files.contents, (contents == null ? "" : contents).getBytes(Charset.forName("utf-8")));
		update.execute();
	}
	
	/**
	 * Deletes the specified files. It is not an error if any of the specified
	 * files do not exist; they will simply be skipped.
	 * 
	 * @param filenames the names of the files to delete
	 */
	public static void delete(String... filenames) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QFiles.files);
		delete.where(QFiles.files.name.in(filenames));
		delete.execute();
	}
	
	/**
	 * Deletes the specified files. It is not an error if any of the specified
	 * files do not exist; they will simply be skipped.
	 * 
	 * @param filenames the names of the files to delete
	 */
	public static void delete(Collection<? extends String> filenames) {
		final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QFiles.files);
		delete.where(QFiles.files.name.in(filenames));
		delete.execute();
	}
	
	/**
	 * Returns the record for the specified file.
	 * @param filename the name of the file
	 * @return the record, or null if the file does not exist
	 */
	public static Files getFile(String filename) {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(QFiles.files).where(QFiles.files.name.eq(filename)).singleResult(QFiles.files);
	}
	
	/**
	 * Returns the contents of the specified file.
	 * @param filename the name of the file
	 * @return the contents, or null if the file does not exist
	 */
	public static byte[] getContents(String filename) {
		// this looks like a bug in QueryDSL...
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		Object[] row = (Object[])(Object)query.from(QFiles.files).where(QFiles.files.name.eq(filename)).singleResult(QFiles.files.contents);
		return (row == null ? null : (byte[])row[0]);
	}
	
}
