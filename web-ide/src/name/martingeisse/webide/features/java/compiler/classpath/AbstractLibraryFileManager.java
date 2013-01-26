/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.compiler.classpath;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import name.martingeisse.common.util.GenericTypeUtil;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Base class for {@link JavaFileManager} implementations for
 * class libraries. Such implementations handle only specific
 * locations, and do not accept output files.
 * 
 * This class implements {@link ForwardingJavaFileManager} for
 * file manager chaining.
 */
public abstract class AbstractLibraryFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AbstractLibraryFileManager.class);
	
	/**
	 * the libraryNameForLogging
	 */
	private final String libraryNameForLogging;
	
	/**
	 * Constructor.
	 * @param libraryNameForLogging the name used for this library in log messages
	 * @param next the next file manager to search
	 */
	public AbstractLibraryFileManager(final String libraryNameForLogging, final JavaFileManager next) {
		super(next);
		this.libraryNameForLogging = libraryNameForLogging;
	}

	/**
	 * Getter method for the libraryNameForLogging.
	 * @return the libraryNameForLogging
	 */
	public String getLibraryNameForLogging() {
		return libraryNameForLogging;
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
		String loggingName = "library [" + libraryNameForLogging + "] package [" + packageName + "], name [" + relativeName + "]";
		if (location == StandardLocation.CLASS_PATH) {
			logger.trace("searching library for file: " + loggingName);
			final FileObject file = getLibraryFile(packageName, relativeName);
			if (file != null) {
				logger.trace("found file in library: " + loggingName);
				return file;
			}
		} else {
			logger.trace("skipping library for non-classpath file: " + loggingName);
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
		String loggingName = "library [" + libraryNameForLogging + "] class [" + className + "], kind [" + kind + "]";
		if (location == StandardLocation.CLASS_PATH && kind == Kind.CLASS) {
			logger.trace("searching library for class " + loggingName);
			final JavaFileObject file = getLibraryClassFile(className);
			if (file != null) {
				logger.trace("found class in library: " + loggingName);
				return file;
			}
		} else {
			logger.trace("skipping library for non-class or non-classpath class: " + loggingName);
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
		boolean result = (location == StandardLocation.CLASS_PATH) || super.hasLocation(location);
		logger.trace("library [" + libraryNameForLogging + "] has location [" + location + "]: " + result);
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#inferBinaryName(javax.tools.JavaFileManager.Location, javax.tools.JavaFileObject)
	 */
	@Override
	public final String inferBinaryName(Location location, JavaFileObject file) {
		String loggingName = "location [" + location + "], file [" + file + "]";
		if (location == StandardLocation.CLASS_PATH) {
			logger.trace("trying to infer binary name in library for " + loggingName);
			String result = inferLibraryBinaryName(file);
			logger.trace("result: " + result);
			if (result != null) {
				return result;
			}
		}
		return super.inferBinaryName(location, file);
	}

	/**
	 * Tries to infer the binary name for the specified file.
	 * @param file the file
	 * @return the binary name, or null if not known
	 */
	public abstract String inferLibraryBinaryName(JavaFileObject file);
	
	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#list(javax.tools.JavaFileManager.Location, java.lang.String, java.util.Set, boolean)
	 */
	@Override
	public final Iterable<JavaFileObject> list(final Location location, final String packageName, final Set<Kind> kinds, final boolean recurse) throws IOException {
		logger.trace("listing library [" + libraryNameForLogging + "] files for location [" + location + "], package [" + packageName + "], kinds [" + StringUtils.join(kinds, ", ") + "], recurse [" + recurse + "]");
		final Iterable<JavaFileObject> superIterable = super.list(location, packageName, kinds, recurse);
		if (location == StandardLocation.CLASS_PATH && kinds.contains(Kind.CLASS)) {
			final Iterable<JavaFileObject> libraryIterable = listLibraryClassFiles(packageName, recurse);
			logger.trace("contributed files from this library: " + StringUtils.join(libraryIterable.iterator(), ", "));
			return new Iterable<JavaFileObject>() {
				@Override
				public Iterator<JavaFileObject> iterator() {
					return GenericTypeUtil.unsafeCast(new IteratorChain(superIterable.iterator(), libraryIterable.iterator()));
				}
			};
		} else {
			logger.trace("no contribution from this library because of location/kinds");
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
