/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.phunky.runtime.code.statement;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.code.CodeDumper;
import name.martingeisse.phunky.runtime.code.CodeLocation;
import name.martingeisse.phunky.runtime.json.JsonValueBuilder;

/**
 * A statement is an object that can be executed using an
 * {@link Environment}, for side effects on the environment
 * and on other objects.
 */
public interface Statement {

	/**
	 * Getter method for the location.
	 * @return the location
	 */
	public CodeLocation getLocation();
	
	/**
	 * Setter method for the location.
	 * @param location the location to set
	 */
	public void setLocation(CodeLocation location);
	
	/**
	 * Executes this statement.
	 * @param environment the environment
	 */
	public void execute(Environment environment);

	/**
	 * Dumps this statement using the specified code dumper.
	 * @param dumper the code dumper
	 */
	public void dump(CodeDumper dumper);

	/**
	 * Converts this statement to a JSON representation of the code, by using
	 * the provided JSON builder to generate a JSON value that represents this
	 * statement.
	 * 
	 * @param builder the JSON builder to use for the conversion
	 */
	public void toJson(JsonValueBuilder<?> builder);
	
}
