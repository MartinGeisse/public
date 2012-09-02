/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.reference;

/**
 * Defines the multiplicity of an entity reference at one of
 * its endpoints.
 */
public enum EntityReferenceEndpointMultiplicity {

	/**
	 * There are zero or one endpoint instances.
	 */
	ZERO_OR_ONE,
	
	/**
	 * There is exactly one endpoint instance.
	 */
	ONE,
	
	/**
	 * There are zero, one, or more endpoint instances.
	 */
	ANY,
	
	/**
	 * There are one or more endpoint instances.
	 */
	AT_LEAST_ONE;
	
	/**
	 * @return true if and only if this multiplicity can have zero endpoint instances
	 */
	public boolean isOptional() {
		return (this == ZERO_OR_ONE || this == ANY);
	}
	
	/**
	 * @return true if and only if this multiplicity can have two or more endpoint instances
	 */
	public boolean isMultiple() {
		return (this == ANY || this == AT_LEAST_ONE);
	}
	
}
