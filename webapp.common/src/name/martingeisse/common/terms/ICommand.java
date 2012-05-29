/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.terms;

/**
 * This interface represents a command that can be executed.
 * 
 * Any arguments needed for execution must be set by other means,
 * i.e. this interface does not accept any additional arguments.
 * For example, implementations may allow to set arguments as
 * state.
 * 
 * Intended implementations that require arguments but are stateless
 * (i.e. cannot have those arguments set as state) cannot actually
 * implement this interface; instead, they must act as factories for
 * stateful command "instances"  that implement this interface.
 * 
 * Similar restrictions apply to return values.
 */
public interface ICommand {

	/**
	 * Executes this command.
	 */
	public void execute();
	
}
