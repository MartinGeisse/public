/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.stdform;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Specialized configurator for form elements that contain a
 * single form component. This configurator allows to obtain that
 * form component an add validation rules.
 */
public class SingleFormComponentElementConfigurator extends WrappedElementConfigurator {

	/**
	 * the formComponent
	 */
	private final FormComponent<?> formComponent;
	
	/**
	 * Constructor.
	 * @param rawElementComponent the component that represents the raw form element
	 * @param formComponent the form component
	 */
	public SingleFormComponentElementConfigurator(Component rawElementComponent, FormComponent<?> formComponent) {
		super(rawElementComponent);
		this.formComponent = formComponent;
	}

	/**
	 * Getter method for the formComponent.
	 * @return the formComponent
	 */
	public FormComponent<?> getFormComponent() {
		return formComponent;
	}
	
	/**
	 * Makes the form component a required field.
	 * @return this
	 */
	public final SingleFormComponentElementConfigurator setRequired() {
		return setRequired(true);
	}
	
	/**
	 * Makes the form component either a required or not-required field, depending
	 * on the argument.
	 * @param required whether the form component is a required field
	 * @return this
	 */
	public final SingleFormComponentElementConfigurator setRequired(boolean required) {
		formComponent.setRequired(required);
		return this;
	}
	
	/**
	 * This method is only applicable to text components.
	 * See {@link AbstractTextComponent#setConvertEmptyInputStringToNull(boolean)}.
	 * 
	 * @param convertEmptyInputStringToNull the setting to apply
	 * @return this
	 */
	public final SingleFormComponentElementConfigurator setConvertEmptyInputStringToNull(boolean convertEmptyInputStringToNull) {
		AbstractTextComponent<?> c = (AbstractTextComponent<?>)formComponent;
		c.setConvertEmptyInputStringToNull(convertEmptyInputStringToNull);
		return this;
	}
	
	/**
	 * Adds a behavior to the form component.
	 * 
	 * @param behavior the behavior to add
	 * @return this
	 */
	public final SingleFormComponentElementConfigurator addFormComponentBehaviour(Behavior behavior) {
		formComponent.add(behavior);
		return this;
	}
	
}
