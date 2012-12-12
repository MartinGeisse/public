/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.java;

import javax.tools.JavaFileObject;

/**
 * This sub-interface combines {@link IMemoryFileObject} and {@link JavaFileObject}.
 */
public interface IMemoryJavaFileObject extends IMemoryFileObject, JavaFileObject {
}
