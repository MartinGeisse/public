/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.codegen.template.test;

import name.martingeisse.codegen.dataclass.DataClassTemplate;
import name.martingeisse.codegen.dataclass.DataClassType;

/**
 * A template for the various 2d/3d vector types.
 */
public class EulerAnglesTemplate extends DataClassTemplate {
	
	/**
	 * Constructor.
	 * @param dataClassType selects mutability and similar properties
	 */
	public EulerAnglesTemplate(DataClassType dataClassType) {
		super("EulerAngles", dataClassType);
		setPackageName("name.martingeisse.stackd.common.geometry");
	}

}
