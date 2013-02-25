/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.compiler;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.webide.application.Configuration;
import name.martingeisse.webide.features.java.compiler.classpath.ClassFolderLibraryFileManager;
import name.martingeisse.webide.features.java.compiler.classpath.JarFileManager;
import name.martingeisse.webide.features.java.compiler.classpath.PlatformClasspathShieldFileManager;
import name.martingeisse.webide.features.java.compiler.memfile.IMemoryFileObject;
import name.martingeisse.webide.features.java.compiler.memfile.IMemoryJavaFileObject;
import name.martingeisse.webide.features.java.compiler.memfile.MemoryFileManager;
import name.martingeisse.webide.features.java.compiler.memfile.MemoryJavaFileObject;
import name.martingeisse.webide.resources.MarkerMeaning;
import name.martingeisse.webide.resources.MarkerOrigin;
import name.martingeisse.webide.resources.RecursiveResourceOperation;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.Workspace;
import name.martingeisse.webide.resources.WorkspaceResourceNotFoundException;
import name.martingeisse.webide.resources.build.BuilderResourceDelta;
import name.martingeisse.webide.resources.build.IBuilder;

/**
 * This façade is used by the builder thread to invoke the Java compiler.
 */
public class JavaBuilder implements IBuilder {

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.resources.build.IBuilder#incrementalBuild(name.martingeisse.common.javascript.analyze.JsonAnalyzer, java.util.Set)
	 */
	@Override
	public void incrementalBuild(final JsonAnalyzer descriptorAnalyzer, final Set<BuilderResourceDelta> deltas) {
		ResourcePath sourcePath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("sourcePath").expectString()); 
		ResourcePath binaryPath = new ResourcePath(descriptorAnalyzer.analyzeMapElement("binaryPath").expectString()); 
		performCompilation(sourcePath, binaryPath);
	}

	/**
	 * Compiles a single Java project.
	 */
	private static void performCompilation(final ResourcePath sourcePath, final ResourcePath binaryPath) {

		// delete binary files from previous builds
		Workspace.delete(binaryPath);

		// obtain the standard file manager so we can include the boot classpath
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<JavaFileObject>();
		final Locale locale = null;
		final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticListener, locale, Charset.forName("utf-8"));
		final PlatformClasspathShieldFileManager wrappedStandardFileManager = new PlatformClasspathShieldFileManager(standardFileManager);

		// create library file managers
		JavaFileManager fileManager = wrappedStandardFileManager;
		try {
			if (Configuration.isDeployedFolderLayout()) {
				fileManager = new JarFileManager(new JarFile("jars/wicket-util-6.4.0.jar"), fileManager);
				fileManager = new JarFileManager(new JarFile("jars/wicket-core-6.4.0.jar"), fileManager);
				fileManager = new JarFileManager(new JarFile("jars/wicket-request-6.4.0.jar"), fileManager);
				fileManager = new JarFileManager(new JarFile("jars/wicket-extensions-6.4.0.jar"), fileManager);
				fileManager = new ClassFolderLibraryFileManager(new File("."), fileManager);
			} else {
				fileManager = new JarFileManager(new JarFile("../webapp.wicket/lib/java/wicket-util-6.4.0.jar"), fileManager);
				fileManager = new JarFileManager(new JarFile("../webapp.wicket/lib/java/wicket-core-6.4.0.jar"), fileManager);
				fileManager = new JarFileManager(new JarFile("../webapp.wicket/lib/java/wicket-request-6.4.0.jar"), fileManager);
				fileManager = new JarFileManager(new JarFile("../webapp.wicket/lib/java/wicket-extensions-6.4.0.jar"), fileManager);
				fileManager = new ClassFolderLibraryFileManager(new File("bin"), fileManager);
				fileManager = new ClassFolderLibraryFileManager(new File("../webapp.wicket/bin"), fileManager);
				fileManager = new ClassFolderLibraryFileManager(new File("../webapp.common/bin"), fileManager);
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		// fetch and wrap source code files as JavaFileObjects
		final MemoryFileManager memoryFileManager = new MemoryFileManager(fileManager);
		final List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		try {
			new RecursiveResourceOperation() {
				@Override
				protected void handleFile(ResourcePath path, File file) {
					if ("java".equals(path.getExtension())) {
						final String key = path.removeFirstSegments(sourcePath.getSegmentCount(), true).toString();
						final IMemoryJavaFileObject fileObject = new MemoryJavaFileObject(key, Workspace.readBinaryFile(path, true));
						javaFiles.add(fileObject);
						memoryFileManager.getInputFiles().put(key, fileObject);
					}
				}
			}.handle(sourcePath);
		} catch (final WorkspaceResourceNotFoundException e) {
			// src folder doesn't exist
			try {
				memoryFileManager.close();
			} catch (final IOException e2) {
			}
			return;
		}

		// run the java compiler
		final CompilationTask task = compiler.getTask(null, memoryFileManager, diagnosticListener, null, null, javaFiles);
		/* final boolean success = */task.call();

		// save the class files in the database
		for (final IMemoryFileObject fileObject : memoryFileManager.getOutputFiles().values()) {
			final ResourcePath path = new ResourcePath(binaryPath.toString() + fileObject.getName());
			Workspace.createFile(path, fileObject.getBinaryContent(), true);
		}

		// dispose of the file manager
		try {
			memoryFileManager.close();
		} catch (final IOException e) {
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

		// these warnings will be ignored because they're nonsense
		final Set<String> ignoredWarnings = new HashSet<String>();
		ignoredWarnings.add("warning: [package-info] a package-info.java file has already been seen for package unnamed package");
		ignoredWarnings.add("[package-info] a package-info.java file has already been seen for package unnamed package");

		// generate markers for the diagnostic messages
		Workspace.deleteMarkersRecursively(sourcePath, MarkerOrigin.JAVAC);
		for (final Map.Entry<JavaFileObject, List<Diagnostic<? extends JavaFileObject>>> fileEntry : sourceFileToDiagnostics.entrySet()) {
			ResourcePath filePath = new ResourcePath(fileEntry.getKey().getName());
			filePath = sourcePath.concat(filePath.withLeadingSeparator(false), false);
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
				final String messageText = diagnostic.getMessage(null);
				if (meaning == MarkerMeaning.WARNING && ignoredWarnings.contains(messageText)) {
					continue;
				}

				// create the marker
				final long line = diagnostic.getLineNumber();
				final long column = diagnostic.getColumnNumber();
				Workspace.createMarker(filePath, MarkerOrigin.JAVAC, meaning, line, column, messageText);

			}
		}

		// copy non-Java files over to the output folder
		new RecursiveResourceOperation() {
			@Override
			protected void handleFile(ResourcePath sourceFilePath, File sourceFile) {
				if (!"java".equals(sourceFilePath.getExtension())) {
					final ResourcePath destinationFilePath = binaryPath.concat(sourceFilePath.removeFirstSegments(sourcePath.getSegmentCount(), false), false);
					Workspace.copyFile(sourceFilePath, destinationFilePath, true);
				}
			}
		}.handle(sourcePath);
		
	}

}
