/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple {@link Panel} that wraps a {@link TextField}.
 * @param <T> the model type
 */
public class TextFieldPanel<T> extends Panel {

	/**
	 * the textField
	 */
	private final TextField<T> textField;

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public TextFieldPanel(String id) {
		this(id, new TextField<T>(getTextFieldId()));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the text field
	 */
	public TextFieldPanel(String id, IModel<T> model) {
		this(id, new TextField<T>(getTextFieldId(), model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param textField the text field to use. Must use the wicket:id returned by getTextFieldId().
	 */
	public TextFieldPanel(String id, TextField<T> textField) {
		super(id);
		this.textField = textField;
		add(textField);
	}

	/**
	 * @return the wicket:id that the wrapped text field must use.
	 */
	public static String getTextFieldId() {
		return "wrapped";
	}
	
	/**
	 * Getter method for the textField.
	 * @return the textField
	 */
	public final TextField<T> getTextField() {
		return textField;
	}

}
