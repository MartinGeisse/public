/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.javascript.jsonbuilder;

/**
 * Implemented by objects that can convert themselves to JSON
 * using a {@link JsonValueBuilder}.
 */
public interface IJsonBuildable {

	/**
	 * Converts this object to JSON using the specified builder.
	 * 
	 * @param builder the builder used to build JSON
	 */
	public void toJson(JsonValueBuilder<?> builder);

}
