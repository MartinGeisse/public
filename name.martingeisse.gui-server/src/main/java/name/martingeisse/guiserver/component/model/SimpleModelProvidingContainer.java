/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.component.model;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

/**
 * Simple implementation of {@link ModelProvider} based on a {@link WebMarkupContainer}
 * that has no other effect than providing a model.
 */
public class SimpleModelProvidingContainer extends WebMarkupContainer implements ModelProvider {

	/**
	 * the providedModelName
	 */
	private String providedModelName;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 * @param providedModelName the name used by inner components to access the model
	 */
	public SimpleModelProvidingContainer(String id, IModel<?> model, String providedModelName) {
		super(id, model);
		if (providedModelName == null) {
			throw new IllegalArgumentException("providedModelName cannot be null");
		}
		this.providedModelName = providedModelName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.component.model.ModelProvider#getProvidedModelName()
	 */
	@Override
	public String getProvidedModelName() {
		return providedModelName;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.component.model.ModelProvider#getProvidedModel()
	 */
	@Override
	public IModel<?> getProvidedModel() {
		return getDefaultModel();
	}

}
