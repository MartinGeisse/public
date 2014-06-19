/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.builtin.string.hash;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * The built-in "md5" function.
 */
public final class Md5Function extends AbstractHashFunction {

	/* (non-Javadoc)
	 * @see name.martingeisse.phunky.runtime.builtin.string.hash.AbstractHashFunction#hash(byte[])
	 */
	@Override
	protected byte[] hash(final byte[] data) {
		return DigestUtils.md5(data);
	}

}
