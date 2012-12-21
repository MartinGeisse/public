/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.java.compiler;

import javax.tools.JavaFileObject;

/**
 * This sub-interface combines {@link IMemoryFileObject} and {@link JavaFileObject}.
 */
public interface IMemoryJavaFileObject extends IMemoryFileObject, JavaFileObject {
}
