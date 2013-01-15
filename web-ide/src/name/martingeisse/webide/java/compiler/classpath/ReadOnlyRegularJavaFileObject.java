/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler.classpath;

import java.io.File;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/**
 * Read-only {@link JavaFileObject} implementation based on regular files.
 */
public class ReadOnlyRegularJavaFileObject extends ReadOnlyRegularFileObject implements JavaFileObject {

	/**
	 * Constructor.
	 * @param file the file
	 */
	public ReadOnlyRegularJavaFileObject(File file) {
		super(file);
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileObject#getAccessLevel()
	 */
	@Override
	public Modifier getAccessLevel() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileObject#getKind()
	 */
	@Override
	public Kind getKind() {
		return Kind.CLASS;
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileObject#getNestingKind()
	 */
	@Override
	public NestingKind getNestingKind() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.tools.JavaFileObject#isNameCompatible(java.lang.String, javax.tools.JavaFileObject.Kind)
	 */
	@Override
	public boolean isNameCompatible(final String simpleName, final Kind kind) {
		return true;
	}

}
