/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.component.model;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

/**
 * Provides a named model to inner components.
 */
public interface ModelProvider {

	/**
	 * Getter method for the name of the provided model.
	 * @return the name of the provided model
	 */
	public String getProvidedModelName();
	
	/**
	 * Getter method for the provided model.
	 * @return the provided model
	 */
	public IModel<?> getProvidedModel();

	/**
	 * Resolves the model with the specified name, starting at the origin component,
	 * throwing an exception if the model cannot be found.
	 * 
	 * @param origin the origin component (the component that wants to use the model)
	 * @param name the model name
	 * @return the model
	 */
	public static IModel<?> resolveModel(Component origin, String name) {
		IModel<?> model = tryResolveModel(origin, name);
		if (model == null) {
			throw new RuntimeException("unknown model: " + name);
		}
		return model;
	}

	/**
	 * Resolves the model with the specified name, starting at the origin component.
	 * 
	 * @param origin the origin component (the component that wants to use the model)
	 * @param name the model name
	 * @return the model, or null if not found
	 */
	public static IModel<?> tryResolveModel(Component origin, String name) {
		while (origin != null) {
			if (origin instanceof ModelProvider) {
				ModelProvider modelProvider = (ModelProvider)origin;
				if (modelProvider.getProvidedModelName().equals(name)) {
					return modelProvider.getProvidedModel();
				}
			}
			origin = origin.getParent();
		}
		return null;
	}
	
}
