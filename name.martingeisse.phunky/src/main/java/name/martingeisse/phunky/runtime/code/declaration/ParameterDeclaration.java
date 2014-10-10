/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.phunky.runtime.code.declaration;

import name.martingeisse.phunky.runtime.Environment;
import name.martingeisse.phunky.runtime.PhpRuntime;
import name.martingeisse.phunky.runtime.code.expression.Expression;
import name.martingeisse.phunky.runtime.variable.Variable;

/**
 * Declares a function or method parameter.
 * 
 * A parameter can have an optional default value. The presence
 * of a default value is indicated by the hasDefaultValue flag
 * (null is a valid default value, so that can't be used to indicate
 * the absence of a default value).
 * 
 * A parameter can either be a value parameter or a reference parameter.
 * A value parameter binds to a newly created variable that gets initialized
 * to a caller-supplied value (or the default value). A reference parameter
 * binds to a caller-supplied variable (passing a non-variable value isn't
 * allowed), except if the parameter is omitted and a default value is
 * present, in which case the parameter binds to a newly created variable
 * that gets initialized to the default value.
 */
public final class ParameterDeclaration {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the referenceParameter
	 */
	private boolean referenceParameter;

	/**
	 * the hasDefaultValue
	 */
	private final boolean hasDefaultValue;

	/**
	 * the defaultValue
	 */
	private final Object defaultValue;

	/**
	 * Constructor.
	 * @param name the parameter name
	 * @param referenceParameter whether the parameter is a reference parameter
	 */
	public ParameterDeclaration(String name, boolean referenceParameter) {
		this.name = name;
		this.referenceParameter = referenceParameter;
		this.hasDefaultValue = false;
		this.defaultValue = null;
	}

	/**
	 * Constructor.
	 * @param name the parameter name
	 * @param referenceParameter whether the parameter is a reference parameter
	 * @param defaultValue the default value
	 */
	public ParameterDeclaration(String name, boolean referenceParameter, Object defaultValue) {
		this.name = name;
		this.referenceParameter = referenceParameter;
		this.hasDefaultValue = true;
		this.defaultValue = defaultValue;
	}

	/**
	 * Getter method for the referenceParameter.
	 * @return the referenceParameter
	 */
	public boolean isReferenceParameter() {
		return referenceParameter;
	}

	/**
	 * Setter method for the referenceParameter.
	 * @param referenceParameter the referenceParameter to set
	 */
	public void setReferenceParameter(boolean referenceParameter) {
		this.referenceParameter = referenceParameter;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the hasDefaultValue.
	 * @return the hasDefaultValue
	 */
	public boolean isHasDefaultValue() {
		return hasDefaultValue;
	}

	/**
	 * Getter method for the defaultValue.
	 * @return the defaultValue
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Binds this parameter. This creates a new entry in the callee environment, using values and/or
	 * variables from the caller environment.
	 * 
	 * @param callerEnvironment the caller environment
	 * @param calleeEnvironment the callee environment
	 * @param argumentExpression the expression for the argument that gets passed to the function
	 * (will be evaluated in the caller environment), or null if not provided by the caller.
	 * @param contextDescription a textual description of the context in which the parameter is
	 * bound, used for error reporting
	 */
	public void bind(final Environment callerEnvironment, final Environment calleeEnvironment, final Expression argumentExpression, final String contextDescription) {
		PhpRuntime runtime = callerEnvironment.getRuntime();
		if (argumentExpression == null) {
			if (hasDefaultValue) {
				// using a default value works the same for value/reference parameters.
				calleeEnvironment.put(name, new Variable(defaultValue));
			} else {
				// not providing a value in absence of a default value leaves the variable unbound in the callee environment.
				runtime.triggerError("missing argument " + name + " for " + contextDescription);
			}
		} else {
			if (referenceParameter) {
				// reference parameter
				calleeEnvironment.put(name, argumentExpression.resolveOrCreateVariable(callerEnvironment));
			} else {
				// value parameter
				calleeEnvironment.put(name, new Variable(argumentExpression.evaluate(callerEnvironment)));
			}
		}
	}
	
}
