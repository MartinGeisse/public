/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.panel.simple;

import name.martingeisse.common.terms.IReadOnlyAware;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A simple {@link Panel} that wraps a {@link TextArea}.
 * @param <T> the model type
 */
public class TextAreaPanel<T> extends Panel implements IReadOnlyAware, IFormComponentPanel<T> {

	/**
	 * the textArea
	 */
	private final TextArea<T> textArea;

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 */
	public TextAreaPanel(String id) {
		this(id, new TextArea<T>(getTextAreaId()));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param model the model used by the text area
	 */
	public TextAreaPanel(String id, IModel<T> model) {
		this(id, new TextArea<T>(getTextAreaId(), model));
	}

	/**
	 * Constructor.
	 * @param id the wicket id of this panel
	 * @param textArea the text area to use. Must use the wicket:id returned by getTextAreaId().
	 */
	public TextAreaPanel(String id, TextArea<T> textArea) {
		super(id);
		this.textArea = textArea;
		add(textArea);
	}

	/**
	 * @return the wicket:id that the wrapped text area must use.
	 */
	public static String getTextAreaId() {
		return "wrapped";
	}
	
	/**
	 * Getter method for the textArea.
	 * @return the textArea
	 */
	public final TextArea<T> getTextArea() {
		return textArea;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.terms.IReadOnlyAware#isReadOnly()
	 */
	@Override
	public boolean isReadOnly() {
		return !textArea.isEnabled();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.terms.IReadOnlyAware#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		textArea.setEnabled(!readOnly);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getPanel()
	 */
	@Override
	public Panel getPanel() {
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.panel.simple.IFormComponentPanel#getFormComponent()
	 */
	@Override
	public FormComponent<T> getFormComponent() {
		return textArea;
	}
	
}
