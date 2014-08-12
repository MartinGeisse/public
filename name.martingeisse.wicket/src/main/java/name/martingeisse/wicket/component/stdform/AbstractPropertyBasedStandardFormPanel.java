/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.stdform;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Base class for all standard forms whose models are created using "properties"
 * of some kind that are defined by a single string. Subclasses must implement
 * {@link #createPropertyModel(String)} to map those strings to Wicket models.
 * Based on that method, this class provides factory methods for the form
 * components.
 * 
 * @param <T> the model type
 */
public abstract class AbstractPropertyBasedStandardFormPanel<T> extends StandardFormPanel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the bean model
	 * @param stateless whether to use a stateless form
	 */
	public AbstractPropertyBasedStandardFormPanel(final String id, final IModel<T> model, final boolean stateless) {
		super(id, model, stateless);
	}

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	@SuppressWarnings("unchecked")
	public final IModel<T> getModel() {
		return (IModel<T>)getDefaultModel();
	}
	
	/**
	 * Creates a model for a property being edited
	 * @param propertyName the name of the property
	 * @return the property model
	 */
	public abstract <P> PropertyModel<P> createPropertyModel(String propertyName);
	
	/**
	 * Adds a string-typed text field for a bean property.
	 * @param label the label for the text field
	 * @param propertyName the name of the bean property
	 * @return a configurator that can be used to further configure the added components
	 */
	public final SingleFormComponentElementConfigurator addTextField(final String label, final String propertyName) {
		return addTextField(label, this.<String>createPropertyModel(propertyName));
	}

	/**
	 * Adds a text field using the specified type and model.
	 * @param label the label for the text field
	 * @param type the model type
	 * @param propertyName the name of the bean property
	 * @return a configurator that can be used to further configure the added components
	 */
	public final <P> SingleFormComponentElementConfigurator addTextField(final String label, final Class<P> type, final String propertyName) {
		return addTextField(label, type, this.<P>createPropertyModel(propertyName));
	}

	/**
	 * Adds an email text field using the specified model.
	 * @param label the label for the text field
	 * @param propertyName the name of the bean property
	 * @return a configurator that can be used to further configure the added components
	 */
	public final SingleFormComponentElementConfigurator addEmailTextField(final String label, final String propertyName) {
		return addEmailTextField(label, this.<String>createPropertyModel(propertyName));
	}

	/**
	 * Adds a password field using the specified model.
	 * @param label the label for the password field
	 * @param propertyName the name of the bean property
	 * @return a configurator that can be used to further configure the added components
	 */
	public final SingleFormComponentElementConfigurator addPasswordTextField(final String label, final String propertyName) {
		return addPasswordTextField(label, this.<String>createPropertyModel(propertyName));
	}

	/**
	 * Adds a text area using the specified model.
	 * @param label the label for the text area
	 * @param propertyName the name of the bean property
	 * @param rows number of visible rows
	 * @return a configurator that can be used to further configure the added components
	 */
	public final SingleFormComponentElementConfigurator addTextArea(final String label, final String propertyName, final int rows) {
		return addTextArea(label, this.<String>createPropertyModel(propertyName), rows);
	}

}
