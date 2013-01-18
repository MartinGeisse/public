/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.common.util.iterator.AbstractIterableWrapper;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * In-memory file manager. The "names" used by this file manager
 * are stringified resource paths (with a leading separator, without
 * a trailing separator).
 */
public class MemoryFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(MemoryFileManager.class);

	/**
	 * the inputFiles
	 */
	private final Map<String, IMemoryFileObject> inputFiles = new HashMap<String, IMemoryFileObject>();

	/**
	 * the outputFiles
	 */
	private final Map<String, IMemoryFileObject> outputFiles = new HashMap<String, IMemoryFileObject>();

	/**
	 * Constructor.
	 * @param next the next file manager to search
	 */
	public MemoryFileManager(final JavaFileManager next) {
		super(next);
	}

	/**
	 * Getter method for the inputFiles.
	 * @return the inputFiles
	 */
	public Map<String, IMemoryFileObject> getInputFiles() {
		return inputFiles;
	}

	/**
	 * Getter method for the outputFiles.
	 * @return the outputFiles
	 */
	public Map<String, IMemoryFileObject> getOutputFiles() {
		return outputFiles;
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location, java.lang.String, java.lang.String)
	 */
	@Override
	public FileObject getFileForInput(final Location location, final String packageName, final String relativeName) throws IOException {
		final String loggingName = "input file; location [" + location + "], package [" + packageName + "], name [" + relativeName + "]";
		if (location == StandardLocation.SOURCE_PATH) {
			logger.trace("searching memory files: " + loggingName);
			final String key = getPackageFileName(packageName, relativeName);
			final FileObject file = inputFiles.get(key);
			logger.trace("result for key [" + key + "]: " + file);
			if (file != null) {
				return file;
			}
		} else {
			logger.trace("skipping memory files for " + loggingName);
		}
		return super.getFileForInput(location, packageName, relativeName);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, java.lang.String, javax.tools.FileObject)
	 */
	@Override
	public FileObject getFileForOutput(final Location location, final String packageName, final String relativeName, final FileObject sibling) throws IOException {
		final String loggingName = "output file; location [" + location + "], package [" + packageName + "], name [" + relativeName + "], sibling [" + sibling + "]";
		if (location == StandardLocation.CLASS_OUTPUT) {
			logger.trace("searching memory files: " + loggingName);
			final String key = getPackageFileName(packageName, relativeName);
			IMemoryFileObject file = outputFiles.get(key);
			if (file == null) {
				logger.trace("key [" + key + "] not found, creating");
				file = new MemoryBlobFileObject(key);
				outputFiles.put(key, file);
			} else {
				logger.trace("key [" + key + "] found");
			}
			return file;
		} else {
			logger.trace("skipping memory files for " + loggingName);
		}
		return super.getFileForOutput(location, packageName, relativeName, sibling);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getJavaFileForInput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind)
	 */
	@Override
	public JavaFileObject getJavaFileForInput(final Location location, final String className, final Kind kind) throws IOException {
		final String loggingName = "java input file; location [" + location + "], class [" + className + "], kind [" + kind + "]";
		if (location == StandardLocation.SOURCE_PATH) {
			logger.trace("searching memory files: " + loggingName);
			final String key = getJavaFileName(className, kind);
			final FileObject file = inputFiles.get(key);
			logger.trace("result for key [" + key + "]: " + file);
			if (file != null) {
				if (file instanceof JavaFileObject) {
					return (JavaFileObject)file;
				} else {
					logger.trace("file is not a JavaFileObject, returning 'not found'");
					return null;
				}
			}
		} else {
			logger.trace("skipping memory files for " + loggingName);
		}
		return super.getJavaFileForInput(location, className, kind);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind, final FileObject sibling) throws IOException {
		final String loggingName = "java output file; location [" + location + "], class [" + className + "], kind [" + kind + "], sibling [" + sibling + "]";
		if (location == StandardLocation.CLASS_OUTPUT) {
			logger.trace("searching memory files: " + loggingName);
			final String key = getJavaFileName(className, kind);
			final FileObject file = outputFiles.get(key);
			if (file instanceof JavaFileObject) {
				logger.trace("key [" + key + "] found");
				return (JavaFileObject)file;
			} else {
				logger.trace("key [" + key + "] " + (file == null ? "not found, creating" : "found but not a java file, replacing"));
				final IMemoryJavaFileObject javaFileObject = (kind == Kind.SOURCE ? new MemoryJavaFileObject(key) : new MemoryClassFileObject(key));
				outputFiles.put(key, javaFileObject);
				return javaFileObject;
			}
		} else {
			logger.trace("skipping memory files for " + loggingName);
		}
		return super.getJavaFileForOutput(location, className, kind, sibling);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#hasLocation(javax.tools.JavaFileManager.Location)
	 */
	@Override
	public boolean hasLocation(final Location location) {
		if ((location == StandardLocation.SOURCE_PATH || location == StandardLocation.CLASS_OUTPUT)) {
			logger.trace("memory file manager has location [" + location + "]");
			return true;
		} else {
			logger.trace("memory file manager doesn't have location [" + location + "], passing to next file manager");
			return super.hasLocation(location);
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#inferBinaryName(javax.tools.JavaFileManager.Location, javax.tools.JavaFileObject)
	 */
	@Override
	public String inferBinaryName(final Location location, final JavaFileObject file) {
		return super.inferBinaryName(location, file);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#isSameFile(javax.tools.FileObject, javax.tools.FileObject)
	 */
	@Override
	public boolean isSameFile(final FileObject a, final FileObject b) {
		logger.trace("checking files for identity: [" + a + "] and [" + b + "]");
		if (a instanceof IMemoryFileObject && b instanceof IMemoryFileObject) {
			logger.trace("both memory files; same: " + (a == b));
			return (a == b);
		} else {
			logger.trace("at least one of them is not a memory file; passing to next file manager");
			return super.isSameFile(a, b);
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#list(javax.tools.JavaFileManager.Location, java.lang.String, java.util.Set, boolean)
	 */
	@Override
	public Iterable<JavaFileObject> list(final Location location, final String packageName, final Set<Kind> kinds, final boolean recurse) throws IOException {
		logger.trace("listing memory files for location [" + location + "], package [" + packageName + "], kinds [" + StringUtils.join(kinds, ", ") + "], recurse [" + recurse + "]");
		final Iterable<JavaFileObject> superIterable = super.list(location, packageName, kinds, recurse);
		final Iterable<JavaFileObject> thisIterable = listThis(location, packageName, kinds, recurse);
		logger.trace("contributed memory files: " + StringUtils.join(thisIterable.iterator(), ", "));
		return new Iterable<JavaFileObject>() {
			@Override
			public Iterator<JavaFileObject> iterator() {
				return GenericTypeUtil.unsafeCast(new IteratorChain(superIterable.iterator(), thisIterable.iterator()));
			}
		};
	}

	/**
	 * 
	 */
	private Iterable<JavaFileObject> listThis(final Location location, final String packageName, final Set<Kind> kinds, final boolean recurse) throws IOException {

		// determine the file map to use
		final Map<String, IMemoryFileObject> fileMap;
		if (location == StandardLocation.SOURCE_PATH && kinds.contains(Kind.SOURCE)) {
			fileMap = inputFiles;
		} else if (location == StandardLocation.CLASS_OUTPUT && kinds.contains(Kind.CLASS)) {
			fileMap = outputFiles;
		} else {
			return new ArrayList<JavaFileObject>();
		}

		// create a filtered iterator
		final Pattern namePattern = Pattern.compile("\\/" + (packageName.replace(".", "\\/")) + "\\/[^\\/]+");
		return new AbstractIterableWrapper<IMemoryFileObject, JavaFileObject>(fileMap.values()) {
			@Override
			protected JavaFileObject handleElement(final IMemoryFileObject element) {
				if (element instanceof JavaFileObject) {
					if (namePattern.matcher(element.getName()).matches()) {
						return (JavaFileObject)element;
					}
				}
				return null;
			}
		};

	}

	/**
	 * Maps the name of a package-local file to the name of a memory file.
	 */
	private static String getPackageFileName(final String packageName, final String localFileName) {
		return "/" + packageName.replace('.', '/') + '/' + localFileName;
	}

	/**
	 * Maps the name of a class to the name of a memory file.
	 */
	private static String getJavaFileName(final String className, final Kind kind) {
		return "/" + className.replace('.', '/') + (kind == Kind.SOURCE ? ".java" : kind == Kind.CLASS ? ".class" : "");
	}

}
