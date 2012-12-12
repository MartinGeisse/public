/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.JavaFileObject.Kind;
import javax.tools.StandardLocation;

import name.martingeisse.common.util.TypeFilteredIterable;

/**
 * In-memory file manager.
 */
public class MemoryFileManager implements JavaFileManager {

	/**
	 * the standardFileManager
	 */
	private final StandardJavaFileManager standardFileManager;
	
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
	 * @param standardFileManager the standard file manager, needed to access
	 * the boot classpath
	 */
	public MemoryFileManager(StandardJavaFileManager standardFileManager) {
		this.standardFileManager = standardFileManager;
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
	 * @see javax.tools.OptionChecker#isSupportedOption(java.lang.String)
	 */
	@Override
	public int isSupportedOption(final String option) {
		return standardFileManager.isSupportedOption(option);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#close()
	 */
	@Override
	public void close() throws IOException {
		standardFileManager.close();
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#flush()
	 */
	@Override
	public void flush() throws IOException {
		standardFileManager.flush();
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getClassLoader(javax.tools.JavaFileManager.Location)
	 */
	@Override
	public ClassLoader getClassLoader(final Location location) {
		return standardFileManager.getClassLoader(location);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location, java.lang.String, java.lang.String)
	 */
	@Override
	public FileObject getFileForInput(final Location location, final String packageName, final String relativeName) throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			return standardFileManager.getFileForInput(location, packageName, relativeName);
		}
		if (location != StandardLocation.SOURCE_PATH) {
			return null;
		}
		String key = packageName + '.' + relativeName;
		return inputFiles.get(key);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, java.lang.String, javax.tools.FileObject)
	 */
	@Override
	public FileObject getFileForOutput(final Location location, final String packageName, final String relativeName, final FileObject sibling) throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			return standardFileManager.getFileForOutput(location, packageName, relativeName, sibling);
		}
		if (location != StandardLocation.CLASS_OUTPUT) {
			return null;
		}
		String key = packageName + '.' + relativeName;
		IMemoryFileObject fileObject = outputFiles.get(key);
		if (fileObject == null) {
			fileObject = new MemoryBlobFileObject(key);
			outputFiles.put(key, fileObject);
		}
		return fileObject;
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getJavaFileForInput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind)
	 */
	@Override
	public JavaFileObject getJavaFileForInput(final Location location, final String className, final Kind kind) throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			return standardFileManager.getJavaFileForInput(location, className, kind);
		}
		if (location != StandardLocation.SOURCE_PATH) {
			return null;
		}
		String key = className + (kind == Kind.SOURCE ? ".java" : kind == Kind.CLASS ? ".class" : "");
		IMemoryFileObject fileObject = inputFiles.get(key);
		if (fileObject instanceof JavaFileObject) {
			return (JavaFileObject)fileObject;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location, java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
	 */
	@Override
	public JavaFileObject getJavaFileForOutput(final Location location, final String className, final Kind kind, final FileObject sibling) throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			return standardFileManager.getJavaFileForOutput(location, className, kind, sibling);
		}
		if (location != StandardLocation.CLASS_OUTPUT) {
			return null;
		}
		String key = className + (kind == Kind.SOURCE ? ".java" : kind == Kind.CLASS ? ".class" : "");
		IMemoryFileObject fileObject = outputFiles.get(key);
		if (fileObject instanceof JavaFileObject) {
			return (JavaFileObject)fileObject;
		} else {
			IMemoryJavaFileObject javaFileObject = (kind == Kind.SOURCE ? new MemoryJavaFileObject(key) : new MemoryClassFileObject(key));
			outputFiles.put(key, javaFileObject);
			return javaFileObject;
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#handleOption(java.lang.String, java.util.Iterator)
	 */
	@Override
	public boolean handleOption(final String current, final Iterator<String> remaining) {
		return standardFileManager.handleOption(current, remaining);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#hasLocation(javax.tools.JavaFileManager.Location)
	 */
	@Override
	public boolean hasLocation(final Location location) {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			return standardFileManager.hasLocation(location);
		} else {
			return (location == StandardLocation.SOURCE_PATH || location == StandardLocation.CLASS_OUTPUT);
		}
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#inferBinaryName(javax.tools.JavaFileManager.Location, javax.tools.JavaFileObject)
	 */
	@Override
	public String inferBinaryName(final Location location, final JavaFileObject file) {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			return standardFileManager.inferBinaryName(location, file);
		}
		String filename = file.getName();
		if (filename.endsWith(".java")) {
			filename = filename.substring(0, filename.length() - 5); 
		}
		return filename + ".class";
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#isSameFile(javax.tools.FileObject, javax.tools.FileObject)
	 */
	@Override
	public boolean isSameFile(final FileObject a, final FileObject b) {
		return (a == b);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileManager#list(javax.tools.JavaFileManager.Location, java.lang.String, java.util.Set, boolean)
	 */
	@Override
	public Iterable<JavaFileObject> list(final Location location, final String packageName, final Set<Kind> kinds, final boolean recurse) throws IOException {
		if (location == StandardLocation.PLATFORM_CLASS_PATH) {
			return standardFileManager.list(location, packageName, kinds, recurse);
		}
		if (packageName.isEmpty()) {
			if (location == StandardLocation.SOURCE_PATH && kinds.contains(Kind.SOURCE)) {
				return new TypeFilteredIterable<JavaFileObject>(inputFiles.values(), JavaFileObject.class);
			}
			if (location == StandardLocation.CLASS_OUTPUT && kinds.contains(Kind.CLASS)) {
				return new TypeFilteredIterable<JavaFileObject>(outputFiles.values(), JavaFileObject.class);
			}
		}
		return new ArrayList<JavaFileObject>();
	}

}
