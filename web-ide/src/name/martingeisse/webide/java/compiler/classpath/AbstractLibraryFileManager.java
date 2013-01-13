/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler.classpath;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;
import javax.tools.JavaFileManager.Location;

import name.martingeisse.common.util.GenericTypeUtil;

import org.apache.commons.collections.iterators.IteratorChain;

/**
 * Base class for {@link JavaFileManager} implementations for
 * class libraries. Such implementations handle only specific
 * {@link Location}s, and do not accept output files.
 * 
 * This class implements {@link ForwardingJavaFileManager} for
 * file manager chaining.
 */
public abstract class AbstractLibraryFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	/**
	 * Constructor.
	 * @param next the next file manager to search
	 */
	public AbstractLibraryFileManager(final JavaFileManager next) {
		super(next);
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#flush()
	 */
	@Override
	public final void flush() throws IOException {
		super.flush();
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location, java.lang.String, java.lang.String)
	 */
	@Override
	public final FileObject getFileForInput(final Location location, final String packageName, final String relativeName) throws IOException {
		if (location == StandardLocation.CLASS_PATH) {
			final FileObject file = getLibraryFile(packageName, relativeName);
			if (file != null) {
				return file;
			}
		}
		return super.getFileForInput(location, packageName, relativeName);
	}

	/**
	 * Returns a file from this library, or null if not found.
	 * 
	 * @param packageName the package to look in
	 * @param relativeName the file name
	 * @return the file
	 * @throws IOException on I/O errors
	 */
	protected abstract FileObject getLibraryFile(String packageName, String relativeName) throws IOException;

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, java.lang.String, javax.tools.FileObject)
	 */
	@Override
	public final FileObject getFileForOutput(final Location location, final String packageName, final String relativeName, final FileObject sibling) throws IOException {
		return super.getFileForOutput(location, packageName, relativeName, sibling);
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getJavaFileForInput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind)
	 */
	@Override
	public final JavaFileObject getJavaFileForInput(final Location location, final String className, final Kind kind) throws IOException {
		if (location == StandardLocation.CLASS_PATH && kind == Kind.CLASS) {
			final JavaFileObject file = getLibraryClassFile(className);
			if (file != null) {
				return file;
			}
		}
		return super.getJavaFileForInput(location, className, kind);
	}

	/**
	 * Returns a Java class file from this library, or null if not found.
	 * 
	 * @param className the class name
	 * @return the file
	 * @throws IOException on I/O errors
	 */
	protected abstract JavaFileObject getLibraryClassFile(String className) throws IOException;

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
	 */
	@Override
	public final JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind, final FileObject sibling) throws IOException {
		return super.getJavaFileForOutput(location, className, kind, sibling);
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#hasLocation(javax.tools.JavaFileManager.Location)
	 */
	@Override
	public final boolean hasLocation(final Location location) {
		return (location == StandardLocation.CLASS_PATH) || super.hasLocation(location);
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#inferBinaryName(javax.tools.JavaFileManager.Location, javax.tools.JavaFileObject)
	 */
	@Override
	public final String inferBinaryName(final Location location, final JavaFileObject file) {
		return super.inferBinaryName(location, file);
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#list(javax.tools.JavaFileManager.Location, java.lang.String, java.util.Set, boolean)
	 */
	@Override
	public final Iterable<JavaFileObject> list(final Location location, final String packageName, final Set<Kind> kinds, final boolean recurse) throws IOException {
		final Iterable<JavaFileObject> superIterable = super.list(location, packageName, kinds, recurse);
		if (location == StandardLocation.CLASS_PATH && kinds.contains(Kind.CLASS)) {
			final Iterable<JavaFileObject> libraryIterable = listLibraryClassFiles(packageName, recurse);
			return new Iterable<JavaFileObject>() {
				@Override
				public Iterator<JavaFileObject> iterator() {
					return GenericTypeUtil.unsafeCast(new IteratorChain(superIterable.iterator(), libraryIterable.iterator()));
				}
			};
		} else {
			return superIterable;
		}
	}

	/**
	 * Lists all class files in a specific package (and optionally subpackages) from this library.
	 * @param packageName the package name
	 * @param recurse whether to include subpackages
	 * @return an iterable for the files
	 * @throws IOException on I/O errors
	 */
	protected abstract Iterable<JavaFileObject> listLibraryClassFiles(String packageName, boolean recurse) throws IOException;

}
