/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.model;

import name.martingeisse.guiserver.component.model.ModelProvider;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.model.PropertyModel;

/**
 * Implements the model logic for specifying a reference to
 * a named model within a special tag. This logic is implemented
 * as a behavior because the model needs to be resolved after
 * construction.
 */
public final class NamedModelReferenceBehavior extends Behavior {

	/**
	 * the specification
	 */
	private final String specification;

	/**
	 * Constructor.
	 * @param specification the specification
	 */
	public NamedModelReferenceBehavior(String specification) {
		this.specification = specification;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.behavior.Behavior#onConfigure(org.apache.wicket.Component)
	 */
	@Override
	public void onConfigure(Component component) {
		super.onConfigure(component);
		int dotIndex = specification.indexOf(".");
		if (dotIndex == -1) {
			component.setDefaultModel(ModelProvider.resolveModel(component, specification));
		} else {
			String modelName = specification.substring(0, dotIndex);
			String expression = specification.substring(dotIndex + 1);
			component.setDefaultModel(new PropertyModel<>(ModelProvider.resolveModel(component, modelName), expression));
		}
	}
	
}
