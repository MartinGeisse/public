/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.guiserver.xml.element.ElementParser;

/**
 * This class keeps the known element-to-object bindings for child
 * elements based on expected class.
 */
public final class ChildElementObjectBindingRegistry {

	/**
	 * the bindings
	 */
	@SuppressWarnings("rawtypes")
	private final Map<Class, ElementParser> bindings = new HashMap<>();

	/**
	 * Adds a binding to this registry.
	 * 
	 * @param childObjectType the child object type that selects this binding
	 * @param binding the binding
	 */
	public <T> void addBinding(Class<T> childObjectType, ElementParser<T> binding) {
		bindings.put(childObjectType, binding);
	}

	/**
	 * Obtains a binding based on child object type.
	 * 
	 * @param childObjectType the child object type
	 * @return the binding, or null if no binding exists for that type
	 */
	@SuppressWarnings("unchecked")
	public <T> ElementParser<T> getBinding(Class<T> childObjectType) {
		return bindings.get(childObjectType);
	}

}
