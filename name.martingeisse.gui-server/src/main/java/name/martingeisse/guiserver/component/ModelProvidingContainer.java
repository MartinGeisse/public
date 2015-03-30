/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Provides a named model to inner components.
 */
public class ModelProvidingContainer extends WebMarkupContainer {

	/**
	 * the modelName
	 */
	private String modelName;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 * @param modelName the name used by inner components to access the model
	 */
	public ModelProvidingContainer(String id, IModel<?> model, String modelName) {
		super(id, model);
		if (modelName == null) {
			throw new IllegalArgumentException("modelName cannot be null");
		}
		this.modelName = modelName;
	}

	/**
	 * Getter method for the modelName.
	 * @return the modelName
	 */
	public String getModelName() {
		return modelName;
	}

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
			if (origin instanceof ModelProvidingContainer) {
				ModelProvidingContainer modelProvidingContainer = (ModelProvidingContainer)origin;
				if (modelProvidingContainer.getModelName().equals(name)) {
					return modelProvidingContainer.getDefaultModel();
				}
			}
			origin = origin.getParent();
		}
		return null;
	}
	
}
