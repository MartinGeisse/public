/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security;

/**
 * This interface allows to discard sensitive data when it becomes irrelevant.
 */
public interface IPurgeable {

	/**
	 * Removes any data that is relevant only for authentication and
	 * authorization within the application, and keeps only
	 * those values that must be available at a later time without
	 * re-entering them.
	 * 
	 * For example, most applications do not require the user's
	 * password after logging in. In such a case, this method would
	 * discard the password.
	 * 
	 * This method is an additional layer of safety against
	 * accidentally exposing such credentials, for example logging
	 * a password in a reflection-based data dump.
	 */
	public void purge();

}
