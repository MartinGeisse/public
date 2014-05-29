/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.codegen.template.test;

import java.io.File;

import name.martingeisse.codegen.dataclass.DataClassType;
import name.martingeisse.codegen.template.JavaTemplateContext;

/**
 * Main class for code generation.
 */
public final class CodegenMain {

	/**
	 * Prevent instantiation.
	 */
	private CodegenMain() {
	}
	
	/**
	 * Code generation main method.
	 * @param args command-like arguments (ignored)
	 * @throws Exception on errors
	 */
	public static void main(String[] args) throws Exception {

		JavaTemplateContext context = new JavaTemplateContext();
		context.setSourcePath(new File("gen"));
		
		int[] dimensionArray = {2, 3};
		boolean[] integerArray = {false, true};
		DataClassType[] dataClassTypes = DataClassType.values();

		for (DataClassType dataClassType : dataClassTypes) {
			for (int dimension : dimensionArray) {
				for (boolean integer : integerArray) {
					VectorTemplate vectorTemplate = new VectorTemplate(dimension, integer, dataClassType);
					vectorTemplate.renderToSourcePath(context);
				}
			}
			EulerAnglesTemplate eulerAnglesTemplate = new EulerAnglesTemplate(dataClassType);
			eulerAnglesTemplate.renderToSourcePath(context);
		}

	}
	
}
