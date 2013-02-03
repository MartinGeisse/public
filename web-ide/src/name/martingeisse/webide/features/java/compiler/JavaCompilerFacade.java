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
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.ResourceType;
import name.martingeisse.webide.resources.operation.CreateFileOperation;
import name.martingeisse.webide.resources.operation.CreateResourceMarkerOperation;
import name.martingeisse.webide.resources.operation.DeleteResourceOperation;
import name.martingeisse.webide.resources.operation.FetchResourceResult;
import name.martingeisse.webide.resources.operation.ListResourcesOperation;
import name.martingeisse.webide.resources.operation.RecursiveDeleteMarkersOperation;
import name.martingeisse.webide.resources.operation.RecursiveResourceOperation;
import name.martingeisse.webide.resources.operation.WorkspaceResourceNotFoundException;

/**
 * This façade is used by the builder thread to invoke the Java compiler.
 */
public class JavaCompilerFacade {

	/**
	 * This method is run by the builder thread.
	 */
	public static void performCompilation() {
		ListResourcesOperation list = new ListResourcesOperation(new ResourcePath("/"));
		list.run();
		for (FetchResourceResult fetchResult : list.getChildren()) {
			performCompilation(fetchResult.getPath());
		}
	}
	
	/**
	 * Compiles a single Java project.
	 */
	private static void performCompilation(ResourcePath basePath) {
		
		// preparation
		final ResourcePath sourcePath = basePath.appendSegment("src", false);
		final ResourcePath binaryPath = basePath.appendSegment("bin", false);
		
		// delete binary files from previous builds
		new DeleteResourceOperation(binaryPath).run();

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
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// fetch and wrap source code files as JavaFileObjects
		final MemoryFileManager memoryFileManager = new MemoryFileManager(fileManager);
		final List<JavaFileObject> javaFiles = new ArrayList<JavaFileObject>();
		try {
			new RecursiveResourceOperation(sourcePath) {
				@Override
				protected void onLevelFetched(List<FetchResourceResult> fetchResults) {
					for (FetchResourceResult fetchResult : fetchResults) {
						if (fetchResult.getType() == ResourceType.FILE && "java".equals(fetchResult.getPath().getExtension())) {
							final String key = fetchResult.getPath().removeFirstSegments(sourcePath.getSegmentCount(), true).toString();
							final IMemoryJavaFileObject fileObject = new MemoryJavaFileObject(key, fetchResult.getContents());
							javaFiles.add(fileObject);
							memoryFileManager.getInputFiles().put(key, fileObject);
						}
					}
				}
			}.run();
		} catch (WorkspaceResourceNotFoundException e) {
			// src folder doesn't exist
			try {
				memoryFileManager.close();
			} catch (IOException e2) {
			}
			return;
		}

		// run the java compiler
		final CompilationTask task = compiler.getTask(null, memoryFileManager, diagnosticListener, null, null, javaFiles);
		/* final boolean success = */task.call();

		// save the class files in the database
		for (final IMemoryFileObject file : memoryFileManager.getOutputFiles().values()) {
			final ResourcePath path = new ResourcePath(binaryPath.toString() + file.getName());
			new CreateFileOperation(path, file.getBinaryContent(), true).run();
		}

		// dispose of the file manager
		try {
			memoryFileManager.close();
		} catch (IOException e) {
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
		Set<String> ignoredWarnings = new HashSet<String>();
		ignoredWarnings.add("warning: [package-info] a package-info.java file has already been seen for package unnamed package");
		ignoredWarnings.add("[package-info] a package-info.java file has already been seen for package unnamed package");
		
		// generate markers for the diagnostic messages
		new RecursiveDeleteMarkersOperation(sourcePath, MarkerOrigin.JAVAC).run();
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
				String messageText = diagnostic.getMessage(null);
				if (meaning == MarkerMeaning.WARNING && ignoredWarnings.contains(messageText)) {
					continue;
				}

				// create the marker
				long line = diagnostic.getLineNumber();
				long column = diagnostic.getColumnNumber();
				new CreateResourceMarkerOperation(filePath, MarkerOrigin.JAVAC, meaning, line, column, messageText).run();

			}
		}
		
		// copy non-Java files over to the output folder
		new RecursiveResourceOperation(sourcePath) {
			@Override
			protected void onLevelFetched(List<FetchResourceResult> fetchResults) {
				for (FetchResourceResult fetchResult : fetchResults) {
					if (fetchResult.getType() == ResourceType.FILE && !"java".equals(fetchResult.getPath().getExtension())) {
						ResourcePath destinationPath = binaryPath.concat(fetchResult.getPath().removeFirstSegments(sourcePath.getSegmentCount(), false), false);
						new CreateFileOperation(destinationPath, fetchResult.getContents(), true).run();
					}
				}
			}
		}.run();

	}

}
