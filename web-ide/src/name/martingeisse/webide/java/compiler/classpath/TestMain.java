/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler.classpath;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Locale;
import java.util.jar.JarFile;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import name.martingeisse.webide.java.compiler.MemoryFileManager;

/**
 * TODO: document me
 *
 */
public class TestMain {

	public static void main(String[] args) throws Exception {
		
		final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		final DiagnosticCollector<JavaFileObject> diagnosticListener = new DiagnosticCollector<JavaFileObject>();
		final Locale locale = null;
		final StandardJavaFileManager standardFileManager = compiler.getStandardFileManager(diagnosticListener, locale, Charset.forName("utf-8"));
		final MemoryFileManager memoryFileManager = new MemoryFileManager(standardFileManager);
		
		HashSet<Kind> kinds  = new HashSet<Kind>();
		kinds.add(Kind.CLASS);
		JarFile jar = new JarFile("lib/java/mina-core-2.0.5.jar");
		JarFileManager man = new JarFileManager(jar, memoryFileManager);
		for (JavaFileObject file : man.list(StandardLocation.CLASS_PATH, "org.apache.mina.util", kinds, true)) {
			System.out.println(file.toUri());
		}
		
		/*
		JarFile jar = new JarFile("lib/java/mina-core-2.0.5.jar");
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			System.out.println(entries.nextElement().getName());
		}
		jar.close();
		*/
	}
}
