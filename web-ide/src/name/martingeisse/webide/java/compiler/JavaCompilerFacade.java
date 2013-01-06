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
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import name.martingeisse.webide.resources.MarkerMeaning;
import name.martingeisse.webide.resources.MarkerOrigin;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;
import name.martingeisse.webide.resources.operation.CreateFileOperation;
import name.martingeisse.webide.resources.operation.CreateResourceMarkerOperation;
import name.martingeisse.webide.resources.operation.DeleteResourceOperation;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.RecursiveDeleteMarkersOperation;
import name.martingeisse.webide.resources.operation.RecursiveResourceOperation;

/**
 * This façade is used by the builder thread to invoke the Java compiler.
 */
public class JavaCompilerFacade {

	/**
	 * This method is run by the builder thread.
	 */
	public static void performCompilation() {

		// preparation
		final ResourcePath sourcePath = new ResourcePath("/src");
		final ResourcePath binaryPath = new ResourcePath("/bin");
		
		// delete binary files from previous builds
		new DeleteResourceOperation(binaryPath).run();

		// obtain the standard file manager so we can include the boot classpath
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<JavaFileObject>();
		final Locale locale = null;
		final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticListener, locale, Charset.forName("utf-8"));

		// fetch and wrap source code files as JavaFileObjects
		final MemoryFileManager fileManager = new MemoryFileManager(standardFileManager);
		final List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		new RecursiveResourceOperation(sourcePath) {
			@Override
			protected void onLevelFetched(List<FetchResourceResult> fetchResults) {
				for (FetchResourceResult fetchResult : fetchResults) {
					if (fetchResult.getType() == ResourceType.FILE && "java".equals(fetchResult.getPath().getExtension())) {
						final String key = fetchResult.getPath().removeFirstSegments(sourcePath.getSegmentCount(), true).toString();
						final IMemoryJavaFileObject fileObject = new MemoryJavaFileObject(key, fetchResult.getContents());
						javaFiles.add(fileObject);
						fileManager.getInputFiles().put(key, fileObject);
					}
				}
			}
		}.run();

		// run the java compiler
		final CompilationTask task = compiler.getTask(null, fileManager, diagnosticListener, null, null, javaFiles);
		/* final boolean success = */task.call();

		// save the class files in the database
		for (final IMemoryFileObject file : fileManager.getOutputFiles().values()) {
			final ResourcePath path = new ResourcePath("/bin" + file.getName());
			new CreateFileOperation(path, file.getBinaryContent(), true).run();
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
		new RecursiveDeleteMarkersOperation(binaryPath, MarkerOrigin.JAVAC).run();
		for (final Map.Entry<JavaFileObject, List<Diagnostic<? extends JavaFileObject>>> fileEntry : sourceFileToDiagnostics.entrySet()) {
			final ResourcePath filePath = new ResourcePath(fileEntry.getKey().getName());
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
				long line = diagnostic.getLineNumber();
				long column = diagnostic.getColumnNumber();
				new CreateResourceMarkerOperation(filePath, MarkerOrigin.JAVAC, meaning, line, column, messageText).run();

			}
		}

	}

}
