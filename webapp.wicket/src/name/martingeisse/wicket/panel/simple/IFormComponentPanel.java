/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Implemented by the various form component panels.
 * @param <T> the model type of the form components
 */
public interface IFormComponentPanel<T> {

	/**
	 * Getter method for the panel.
	 * @return the panel
	 */
	public Panel getPanel();
	
	/**
	 * Getter method for the formComponent.
	 * @return the formComponent
	 */
	public FormComponent<T> getFormComponent();
	
}
