/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.compiler.classpath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.tools.FileObject;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;

import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.string.EmptyIterable;

import org.apache.commons.collections.iterators.IteratorChain;
import org.apache.log4j.Logger;

/**
 * Library {@link JavaFileManager} implementation based on a folder with class files.
 */
public class ClassFolderLibraryFileManager extends AbstractLibraryFileManager {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ClassFolderLibraryFileManager.class);

	/**
	 * the folder
	 */
	private final File folder;

	/**
	 * the canonicalFolder
	 */
	private final File canonicalFolder;

	/**
	 * Constructor.
	 * @param folder the folder
	 * @param next the next file manager to search
	 */
	public ClassFolderLibraryFileManager(final File folder, final JavaFileManager next) {
		super(folder.getPath(), next);
		this.folder = ParameterUtil.ensureNotNull(folder, "folder");
		try {
			this.canonicalFolder = folder.getCanonicalFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Getter method for the folder.
	 * @return the folder
	 */
	public File getFolder() {
		return folder;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.java.compiler.classpath.AbstractLibraryFileManager#getLibraryFile(java.lang.String, java.lang.String)
	 */
	@Override
	protected FileObject getLibraryFile(final String packageName, final String relativeName) throws IOException {
		final String path = (packageName.isEmpty() ? relativeName : (packageName.replace('.', '/') + '/' + relativeName));
		logger.trace("looking for file [" + path + "] in library [" + getLibraryNameForLogging() + "]");
		final File file = new File(folder, path);
		if (!file.exists()) {
			return null;
		} else {
			return new ReadOnlyRegularFileObject(file);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.java.compiler.classpath.AbstractLibraryFileManager#getLibraryClassFile(java.lang.String)
	 */
	@Override
	protected JavaFileObject getLibraryClassFile(final String className) throws IOException {
		final String path = className.replace('.', '/') + ".class";
		logger.trace("looking for file [" + path + "] in library [" + getLibraryNameForLogging() + "]");
		final File file = new File(folder, path);
		if (!file.exists()) {
			return null;
		} else {
			return new ReadOnlyRegularJavaFileObject(file);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.java.compiler.classpath.AbstractLibraryFileManager#listLibraryClassFiles(java.lang.String, boolean)
	 */
	@Override
	protected Iterable<JavaFileObject> listLibraryClassFiles(final String packageName, final boolean recurse) throws IOException {
		final File packageFolder = (packageName.isEmpty() ? folder : new File(folder, packageName.replace('.', '/')));
		logger.trace("listing library class files in folder [" + packageFolder.getPath() + "]");
		return listClassFilesInternal(packageFolder, recurse);
	}

	/**
	 * 
	 */
	protected Iterable<JavaFileObject> listClassFilesInternal(final File folder, final boolean recurse) throws IOException {
		ParameterUtil.ensureNotNull(folder, "folder");
		File[] folderFiles = folder.listFiles();
		if (folderFiles == null) {
			return new EmptyIterable<JavaFileObject>();
		}
		final List<JavaFileObject> files = new ArrayList<JavaFileObject>();
		final List<Iterable<JavaFileObject>> iterables = new ArrayList<Iterable<JavaFileObject>>();
		for (final File file : folderFiles) {
			if (file.isFile() && file.getName().endsWith(".class")) {
				files.add(new ReadOnlyRegularJavaFileObject(file));
			} else if (file.isDirectory() && recurse) {
				iterables.add(listClassFilesInternal(file, true));
			}
		}
		if (iterables.isEmpty()) {
			return files;
		} else {
			iterables.add(files);
			return new Iterable<JavaFileObject>() {
				@Override
				public Iterator<JavaFileObject> iterator() {
					return GenericTypeUtil.unsafeCast(new IteratorChain(iterables));
				}
			};
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.features.java.compiler.classpath.AbstractLibraryFileManager#inferLibraryBinaryName(javax.tools.JavaFileObject)
	 */
	@Override
	public String inferLibraryBinaryName(JavaFileObject file) {
		if (file instanceof ReadOnlyRegularJavaFileObject) {
			try {
				ReadOnlyRegularJavaFileObject readOnlyRegularJavaFileObject = (ReadOnlyRegularJavaFileObject)file;
				String packagePrefix = inferPackagePrefix(readOnlyRegularJavaFileObject.getFile().getParentFile().getCanonicalFile());
				if (packagePrefix == null) {
					return null;
				}
				String fileName = readOnlyRegularJavaFileObject.getFile().getName();
				if (fileName.endsWith(".class")) {
					fileName = fileName.substring(0, fileName.length() - ".class".length());
				}
				return (packagePrefix + fileName);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Infers the package prefix (package name followed by a dot except if it is the default package) of the
	 * specified package folder, or null if not a package folder in this file manager.
	 */
	private String inferPackagePrefix(File folder) {
		if (folder.equals(this.canonicalFolder)) {
			return "";
		}
		if (folder.getParentFile() == null) {
			return null;
		}
		String parent = inferPackagePrefix(folder.getParentFile());
		if (parent == null) {
			return null;
		}
		return (parent.isEmpty() ? folder.getName() : (parent + folder.getName())) + '.';
	}

	/* (non-Javadoc)
	 * @see javax.tools.ForwardingJavaFileManager#isSameFile(javax.tools.FileObject, javax.tools.FileObject)
	 */
	@Override
	public boolean isSameFile(final FileObject a, final FileObject b) {
		if (a instanceof ReadOnlyRegularFileObject && b instanceof ReadOnlyRegularFileObject) {
			final ReadOnlyRegularFileObject a2 = (ReadOnlyRegularFileObject)a;
			final ReadOnlyRegularFileObject b2 = (ReadOnlyRegularFileObject)b;
			return (a2.getFile().getAbsolutePath().equals(b2.getFile().getAbsolutePath()));
		}
		return super.isSameFile(a, b);
	}

}
