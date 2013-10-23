/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.security.authorization;

import name.martingeisse.wicket.security.authorization.IPermissionRequest;

/**
 * This permission request is used for core actions such as "log in".
 */
public final class CorePermissionRequest implements IPermissionRequest {

	/**
	 * the type
	 */
	private final Type type;
	
	/**
	 * Constructor.
	 * @param type the permission type
	 */
	public CorePermissionRequest(final Type type) {
		this.type = type;
	}
	
	/**
	 * Getter method for the type.
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{CorePermissionRequest: " + type + "}";
	}
	
	/**
	 * This enum represents the different core permission types.
	 */
	public static enum Type {

		/**
		 * requests permission to log in
		 */
		LOGIN;
		
	}
	
}
