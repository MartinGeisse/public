/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.java.compiler.memfile;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

/**
 * In-memory implementation of {@link JavaFileObject} for Java source files.
 */
public class MemoryJavaFileObject extends MemoryStringFileObject implements IMemoryJavaFileObject {

	/**
	 * Constructor.
	 * @param name the file name
	 */
	public MemoryJavaFileObject(final String name) {
		super(name);
	}

	/**
	 * Constructor.
	 * @param name the file name
	 * @param contents the contents
	 */
	public MemoryJavaFileObject(final String name, final byte[] contents) {
		super(name, contents);
	}
	
	/**
	 * Constructor.
	 * @param name the file name
	 * @param contents the contents
	 */
	public MemoryJavaFileObject(final String name, final String contents) {
		super(name, contents);
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
		return Kind.SOURCE;
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
