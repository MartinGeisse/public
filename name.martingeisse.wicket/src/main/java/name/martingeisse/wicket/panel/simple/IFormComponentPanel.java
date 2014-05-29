/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Implemented by the various form component panels.
 * 
 * @param <T> the model type of the form components
 */
public interface IFormComponentPanel<T> {

	/**
	 * Returns the root (outermost) component, typically a {@link Panel}, {@link WebMarkupContainer}
	 * or similar. This is the component that must be added to a page or other container.
	 * @return the root component
	 */
	public Component getRootComponent();
	
	/**
	 * Returns the actual form component for validation.
	 * @return the form component
	 */
	public FormComponent<T> getFormComponent();
	
	/**
	 * Connect the specified validation error acceptor to all form components in this panel.
	 * Most panels contain only a single form component -- the one returned by getFormComponent() --
	 * and would connect that one. However, implementations using multiple form components
	 * (typically using a {@link FormComponentPanel}) would connect each one.
	 * @param validationErrorAcceptor the validation error acceptor
	 */
	public void connectValidationErrorAcceptor(IValidationErrorAcceptor validationErrorAcceptor);
	
}
