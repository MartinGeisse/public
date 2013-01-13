/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler.classpath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

/**
 * {@link JavaFileManager} implementation based on JAR files.
 */
public class JarFileManager extends AbstractLibraryFileManager {

	/**
	 * the jarFile
	 */
	private final JarFile jarFile;

	/**
	 * Constructor.
	 * @param jarFile the JAR file
	 * @param next the next file manager to search
	 */
	public JarFileManager(final JarFile jarFile, final JavaFileManager next) {
		super(next);
		this.jarFile = jarFile;
	}

	/**
	 * Getter method for the jarFile.
	 * @return the jarFile
	 */
	public JarFile getJarFile() {
		return jarFile;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.java.compiler.classpath.AbstractLibraryFileManager#getLibraryFile(java.lang.String, java.lang.String)
	 */
	@Override
	protected FileObject getLibraryFile(final String packageName, final String relativeName) throws IOException {
		String entryName = (packageName.isEmpty() ? relativeName : (packageName.replace('.', '/') + '/' + relativeName));
		JarEntry entry = jarFile.getJarEntry(entryName);
		if (entry == null) {
			return null;
		} else {
			return new JarFileObject(this, entry);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.java.compiler.classpath.AbstractLibraryFileManager#getLibraryClassFile(java.lang.String)
	 */
	@Override
	protected JavaFileObject getLibraryClassFile(final String className) throws IOException {
		JarEntry entry = jarFile.getJarEntry(className.replace('.', '/') + ".class");
		if (entry == null) {
			return null;
		} else {
			return new JarJavaFileObject(this, entry);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.java.compiler.classpath.AbstractLibraryFileManager#listLibraryClassFiles(java.lang.String, boolean)
	 */
	@Override
	protected Iterable<JavaFileObject> listLibraryClassFiles(final String packageName, final boolean recurse) throws IOException {
		String prefix = (packageName.isEmpty() ? "" : (packageName.replace('.', '/') + '/'));
		String suffix = ".class";
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		List<JavaFileObject> files = new ArrayList<JavaFileObject>();
		while (jarEntries.hasMoreElements()) {
			JarEntry entry = jarEntries.nextElement();
			if (entry.getName().startsWith(prefix) && entry.getName().endsWith(suffix)) {
				if (!recurse) {
					String localName = entry.getName().substring(prefix.length());
					localName = localName.substring(0, localName.length() - suffix.length());
					if (localName.indexOf('/') != -1) {
						continue;
					}
				}
				files.add(new JarJavaFileObject(this, entry));
			}
		}
		return files;
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#isSameFile(javax.tools.FileObject, javax.tools.FileObject)
	 */
	@Override
	public boolean isSameFile(FileObject a, FileObject b) {
		if (a instanceof JarFileObject && b instanceof JarFileObject) {
			JarFileObject a2 = (JarFileObject)a;
			JarFileObject b2 = (JarFileObject)b;
			return (a2.getJarEntry() == b2.getJarEntry());
		}
		return super.isSameFile(a, b);
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#close()
	 */
	@Override
	public void close() throws IOException {
		jarFile.close();
		super.close();
	}
	
}
