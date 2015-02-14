/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.builder;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.guiserver.xmlbind.value.TextValueBinding;

/**
 * This class keeps the known text-to-value bindings for attributes
 * based on constructor parameter types. It is used for
 * attributes with the default binding class {@link TextValueBinding}.class
 * to find the actual binding to use.
 */
public final class AttributeTextValueBindingRegistry {

	/**
	 * the bindings
	 */
	@SuppressWarnings("rawtypes")
	private final Map<Class, TextValueBinding> bindings = new HashMap<>();

	/**
	 * Adds a binding to this registry.
	 * 
	 * @param parameterType the constructor parameter type that selects this binding
	 * @param binding the binding
	 */
	public <T> void addBinding(Class<T> parameterType, TextValueBinding<T> binding) {
		bindings.put(parameterType, binding);
	}

	/**
	 * Obtains a binding based on parameter type.
	 * 
	 * @param parameterType the constructor parameter type
	 * @return the binding, or null if no binding exists for that type
	 */
	@SuppressWarnings("unchecked")
	public <T> TextValueBinding<T> getBinding(Class<T> parameterType) {
		return bindings.get(parameterType);
	}

}
