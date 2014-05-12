/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.texturedude;

import java.io.IOException;

/**
 * This interface is implemented by utility functions that
 * work with textures, layers etc.
 * 
 * The interface is designed such that function instances
 * can be shared between virtual machines, by passing the
 * VM's {@link IFunctionHost} instance when calling a function.
 * 
 * Functions should not access mutable variables nor have side
 * effects, except for the virtual machine itself. This allows
 * to run texture scripts at any time.
 */
public interface IFunction {

	/**
	 * Calls this function for the specified function host.
	 * @param host the virtual machine's function host
	 * @throws IOException on I/O errors
	 */
	public void call(IFunctionHost host) throws IOException;
	
}
