/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.codegen.template.test;

import java.util.Map;

import name.martingeisse.codegen.dataclass.DataClassTemplate;
import name.martingeisse.codegen.dataclass.DataClassType;

/**
 * A template for the various 2d/3d vector types.
 */
public class VectorTemplate extends DataClassTemplate {
	
	/**
	 * the dimension
	 */
	private final int dimension;

	/**
	 * the integer
	 */
	private final boolean integer;

	/**
	 * Constructor.
	 * @param dimension the vector dimension (2 or 3)
	 * @param integer true for integer fields, false for double-precision floating point fields
	 * @param dataClassType selects mutability and similar properties
	 */
	public VectorTemplate(int dimension, boolean integer, DataClassType dataClassType) {
		super("Vector" + dimension + (integer ? 'i' : 'd'), dataClassType);
		this.dimension = dimension;
		this.integer = integer;
		setPackageName("name.martingeisse.stackd.common.geometry");
	}

	@Override
	protected void buildDataModel(Map<String, Object> dataModel) {
		super.buildDataModel(dataModel);
		dataModel.put("three", dimension == 3);
		dataModel.put("type", integer ? "int" : "double");
	}

}
