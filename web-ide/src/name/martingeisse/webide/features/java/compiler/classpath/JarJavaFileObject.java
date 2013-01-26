/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.compiler.classpath;

import java.util.jar.JarEntry;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/**
 * {@link JavaFileObject} implementation based on JAR entries.
 */
public class JarJavaFileObject extends JarFileObject implements JavaFileObject {

	/**
	 * Constructor.
	 * @param fileManager the file manager that owns this file
	 * @param jarEntry the JAR entry to wrap
	 */
	public JarJavaFileObject(final JarFileManager fileManager, final JarEntry jarEntry) {
		super(fileManager, jarEntry);
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
