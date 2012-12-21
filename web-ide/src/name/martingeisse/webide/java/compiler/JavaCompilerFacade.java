/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaCompiler.CompilationTask;

import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.webide.entity.Files;
import name.martingeisse.webide.entity.QFiles;
import name.martingeisse.webide.resources.MarkerData;
import name.martingeisse.webide.resources.MarkerDatabaseUtil;
import name.martingeisse.webide.resources.MarkerMeaning;
import name.martingeisse.webide.resources.MarkerOrigin;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;

/**
 * TODO: document me
 *
 */
public class JavaCompilerFacade {

	/**
	 * the synchronizationKey
	 */
	private static final Object synchronizationKey = new Object();
	
	/**
	 * the compilationRequested
	 */
	private static volatile boolean compilationRequested = true;

	/**
	 * the compilationFinished
	 */
	private static volatile boolean compilationFinished = false;
	
	/**
	 * Requests compilation of .java files to .class files.
	 */
	public static void requestCompilation() {
		synchronized(synchronizationKey) {
			compilationRequested = true;
			compilationFinished = false;
			synchronizationKey.notify();
		}
	}
	
	/**
	 * Checks whether compilation is finished.
	 * @return true if finished, false if running
	 */
	public static boolean isCompilationFinished() {
		return compilationFinished;
	}

	/**
	 * static initializer
	 */
	static {
		new Thread() {
			@Override
			public void run() {
				try {
					while (true) {
						
						// wait until requested, then mark as no longer requested to detect subsequent requests
						synchronized(synchronizationKey) {
							if (!compilationRequested) {
								synchronizationKey.wait();
							}
							compilationRequested = false;
						}

						// actually compile
						performCompilation();
						
						// mark as finished unless subsequent requests have arrived
						compilationFinished = !compilationRequested;
						
					}
				} catch (InterruptedException e) {
				}
			}
		}.start();
	}

	/**
	 * This method is run in the compiler thread when requested.
	 */
	private static void performCompilation() {

		// obtain the standard file manager so we can include the boot classpath
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<JavaFileObject>();
		final Locale locale = null;
		final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticListener, locale,
			Charset.forName("utf-8"));

		// fetch and wrap source code files as JavaFileObjects
		final List<Long> sourceFileIds = new ArrayList<Long>();
		final Map<String, Long> sourceFileNameToId = new HashMap<String, Long>();
		final List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		final MemoryFileManager fileManager = new MemoryFileManager(standardFileManager);
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		for (final Files fileRecord : query.from(QFiles.files).where(QFiles.files.name.like("%.java")).list(QFiles.files)) {
			sourceFileIds.add(fileRecord.getId());
			sourceFileNameToId.put(fileRecord.getName(), fileRecord.getId());
			final String name = fileRecord.getName();
			final IMemoryJavaFileObject fileObject = new MemoryJavaFileObject(name, fileRecord.getContents());
			javaFiles.add(fileObject);
			fileManager.getInputFiles().put(name, fileObject);
		}

		// run the java compiler
		final CompilationTask task = compiler.getTask(null, fileManager, diagnosticListener, null, null, javaFiles);
		/* final boolean success = */task.call();

		// save the class files in the database
		for (final IMemoryFileObject file : fileManager.getOutputFiles().values()) {
			final String filename = file.getName();
			final SQLDeleteClause delete = EntityConnectionManager.getConnection().createDelete(QFiles.files);
			delete.where(QFiles.files.name.eq(filename)).execute();
			final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(QFiles.files);
			insert.set(QFiles.files.name, filename);
			insert.set(QFiles.files.contents, file.getBinaryContent());
			insert.execute();
		}

		// collect diagnostic messages per source file
		final List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticListener.getDiagnostics();
		final Map<JavaFileObject, List<Diagnostic<? extends JavaFileObject>>> sourceFileToDiagnostics = new HashMap<JavaFileObject, List<Diagnostic<? extends JavaFileObject>>>();
		for (final Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
			final JavaFileObject currentFile = diagnostic.getSource();
			List<Diagnostic<? extends JavaFileObject>> currentFileDiagnostics = sourceFileToDiagnostics.get(currentFile);
			if (currentFileDiagnostics == null) {
				currentFileDiagnostics = new ArrayList<Diagnostic<? extends JavaFileObject>>();
				sourceFileToDiagnostics.put(currentFile, currentFileDiagnostics);
			}
			currentFileDiagnostics.add(diagnostic);
		}

		// generate markers for the diagnostic messages
		MarkerDatabaseUtil.removeMarkersForFile(sourceFileIds, MarkerOrigin.JAVAC);
		for (final Map.Entry<JavaFileObject, List<Diagnostic<? extends JavaFileObject>>> fileEntry : sourceFileToDiagnostics.entrySet()) {
			final String filename = fileEntry.getKey().getName();
			final long fileId = sourceFileNameToId.get(filename);
			for (final Diagnostic<? extends JavaFileObject> diagnostic : fileEntry.getValue()) {

				// convert the diagnostic kind to a marker meaning (skip this diagnostic if the kind is unknown)
				final Kind diagnosticKind = diagnostic.getKind();
				MarkerMeaning meaning;
				if (diagnosticKind == Kind.ERROR) {
					meaning = MarkerMeaning.ERROR;
				} else if (diagnosticKind == Kind.WARNING || diagnosticKind == Kind.MANDATORY_WARNING) {
					meaning = MarkerMeaning.WARNING;
				} else {
					continue;
				}

				// skip erroneous markers
				String messageText = diagnostic.getMessage(null);
				if (meaning == MarkerMeaning.WARNING && messageText.equals("warning: [package-info] a package-info.java file has already been seen for package unnamed package")) {
					continue;
				}
				
				// create the marker
				final MarkerData markerData = new MarkerData();
				markerData.setOrigin(MarkerOrigin.JAVAC);
				markerData.setMeaning(meaning);
				markerData.setLine(diagnostic.getLineNumber());
				markerData.setColumn(diagnostic.getColumnNumber());
				markerData.setMessage(messageText);
				markerData.insertIntoDatabase(fileId);

			}
		}
		
	}
	
}
