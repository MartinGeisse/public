/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * The base class for "simple" form component panels. Such panels
 * contain only a single form component and no special behavior.
 * 
 * The wrapped from component always uses the Wicket id "wrapped"
 * (also available as class constant FORM_COMPONENT_ID).
 * 
 * This class has no constructor that takes a model since the
 * panel model is irrelevant for "simple" form component panels;
 * only the form component model is important.
 * 
 * @param <T> the model type
 * @param <C> the form component type
 */
public class AbstractSimpleFormComponentPanel<T, C extends FormComponent<T>> extends Panel implements IFormComponentPanel<T> {

	/**
	 * The Wicket id of the wrapped form component.
	 */
	public static final String FORM_COMPONENT_ID = "wrapped";
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public AbstractSimpleFormComponentPanel(String id) {
		super(id);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getRootComponent()
	 */
	@Override
	public Component getRootComponent() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getFormComponent()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public C getFormComponent() {
		return (C)get(FORM_COMPONENT_ID);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#connectValidationErrorAcceptor(name.martingeisse.wicket.autoform.validation.IValidationErrorAcceptor)
	 */
	@Override
	public void connectValidationErrorAcceptor(IValidationErrorAcceptor validationErrorAcceptor) {
		validationErrorAcceptor.acceptValidationErrorsFrom(getFormComponent());
	}

}
