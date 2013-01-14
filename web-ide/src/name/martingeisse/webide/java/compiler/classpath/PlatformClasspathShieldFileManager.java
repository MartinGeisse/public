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
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

import name.martingeisse.common.util.string.EmptyIterable;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * A {@link JavaFileManager} implementation that intercepts all
 * requests for files and classes outside the platform classpath,
 * and passes only those to the next file manager. This can be
 * used to shield the compiler from nonstandard classes visible
 * through the standard file manager. This class also adds logging.
 */
public class PlatformClasspathShieldFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(PlatformClasspathShieldFileManager.class);

	/**
	 * Constructor.
	 * @param next the next file manager to search
	 */
	public PlatformClasspathShieldFileManager(final StandardJavaFileManager next) {
		super(next);
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#close()
	 */
	@Override
	public void close() throws IOException {
		logger.trace("closing standard file manager");
		super.close();
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#flush()
	 */
	@Override
	public void flush() throws IOException {
		logger.trace("flushing standard file manager");
		super.flush();
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getClassLoader(javax.tools.JavaFileManager.Location)
	 */
	@Override
	public ClassLoader getClassLoader(final Location location) {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			logger.trace("returning class loader from standard file manager for location: " + location);
			return super.getClassLoader(location);
		} else {
			logger.trace("class loader from standard file manager blocked for location: " + location);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location, java.lang.String, java.lang.String)
	 */
	@Override
	public FileObject getFileForInput(final Location location, final String packageName, final String relativeName) throws IOException {
		final String loggingName = "location [" + location + "], package [" + packageName + "], name [" + relativeName + "]";
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			logger.trace("returning input file from standard file manager for " + loggingName);
			return super.getFileForInput(location, packageName, relativeName);
		} else {
			logger.trace("input file from standard file manager blocked for " + loggingName);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, java.lang.String, javax.tools.FileObject)
	 */
	@Override
	public FileObject getFileForOutput(final Location location, final String packageName, final String relativeName, final FileObject sibling) throws IOException {
		final String loggingName = "location [" + location + "], package [" + packageName + "], name [" + relativeName + "]";
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			logger.trace("returning output file from standard file manager for " + loggingName);
			return super.getFileForOutput(location, packageName, relativeName, sibling);
		} else {
			logger.trace("output file from standard file manager blocked for " + loggingName);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getJavaFileForInput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind)
	 */
	@Override
	public JavaFileObject getJavaFileForInput(final Location location, final String className, final Kind kind) throws IOException {
		final String loggingName = "location [" + location + "], class [" + className + "], kind [" + kind + "]";
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			logger.trace("returning java input file from standard file manager for " + loggingName);
			return super.getJavaFileForInput(location, className, kind);
		} else {
			logger.trace("java input file from standard file manager blocked for " + loggingName);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind, final FileObject sibling) throws IOException {
		final String loggingName = "location [" + location + "], class [" + className + "], kind [" + kind + "]";
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			logger.trace("returning java output file from standard file manager for " + loggingName);
			return super.getJavaFileForOutput(location, className, kind, sibling);
		} else {
			logger.trace("java output file from standard file manager blocked for " + loggingName);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#handleOption(java.lang.String, java.util.Iterator)
	 */
	@Override
	public boolean handleOption(final String current, final Iterator<String> remaining) {
		logger.trace("passing option to standard file manager: " + current);
		return super.handleOption(current, remaining);
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#hasLocation(javax.tools.JavaFileManager.Location)
	 */
	@Override
	public boolean hasLocation(final Location location) {
		final String loggingName = "location [" + location + "]";
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			logger.trace("checking location in standard file manager: " + loggingName);
			final boolean result = super.hasLocation(location);
			logger.trace("location found: " + result);
			return result;
		} else {
			logger.trace("location from standard file manager blocked: " + loggingName);
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#inferBinaryName(javax.tools.JavaFileManager.Location, javax.tools.JavaFileObject)
	 */
	@Override
	public String inferBinaryName(final Location location, final JavaFileObject file) {
		logger.trace("standard file manager inferring binary name for location [" + location + "], file [" + file + "] / [" + file.getName() + "]");
		final String result = super.inferBinaryName(location, file);
		logger.trace("binary name: " + result);
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#isSameFile(javax.tools.FileObject, javax.tools.FileObject)
	 */
	@Override
	public boolean isSameFile(final FileObject a, final FileObject b) {
		logger.trace("standard file manager checking files for identity: [" + a + "] / [" + a.getName() + "], [" + b + "] / [" + b.getName() + "]");
		final boolean result = super.isSameFile(a, b);
		logger.trace("identical: " + result);
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#isSupportedOption(java.lang.String)
	 */
	@Override
	public int isSupportedOption(final String option) {
		logger.trace("checking standard file manager for supported option: " + option);
		final int result = super.isSupportedOption(option);
		logger.trace("option supported: " + result);
		return result;
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#list(javax.tools.JavaFileManager.Location, java.lang.String, java.util.Set, boolean)
	 */
	@Override
	public Iterable<JavaFileObject> list(final Location location, final String packageName, final Set<Kind> kinds, final boolean recurse) throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			logger.trace("listing files from standard file manager, location [" + location + "], package [" + packageName + "], kinds [" + StringUtils.join(kinds, ", ") + "], recurse [" + recurse
				+ "]");
			final Iterable<JavaFileObject> files = super.list(location, packageName, kinds, recurse);
			logger.trace("contributed files from the standard file manager: " + StringUtils.join(files.iterator(), ", "));
			return files;
		} else {
			logger.trace("blocking file list from standard file manager, location [" + location + "], package [" + packageName + "], kinds [" + StringUtils.join(kinds, ", ") + "], recurse ["
				+ recurse + "]");
			return new EmptyIterable<JavaFileObject>();
		}
	}

}
