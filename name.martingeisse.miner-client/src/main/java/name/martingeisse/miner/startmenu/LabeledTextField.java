/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.stackd.client.gui.control.Control;
import name.martingeisse.stackd.client.gui.control.TextField;
import name.martingeisse.stackd.client.gui.element.TextLine;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import name.martingeisse.stackd.client.gui.util.Color;
import name.martingeisse.stackd.client.gui.util.HorizontalAlignment;

/**
 * A text field with a label.
 */
public final class LabeledTextField extends Control {

	/**
	 * the label
	 */
	private final TextLine label;

	/**
	 * the textField
	 */
	private final TextField textField;

	/**
	 * Constructor.
	 * @param labelText the label text
	 */
	public LabeledTextField(final String labelText) {
		this.label = new TextLine().setColor(Color.WHITE).setText(labelText);
		this.textField = new TextField();
		setControlRootElement(new VerticalLayout().setAlignment(HorizontalAlignment.LEFT).addElement(label).addElement(textField));
	}

	/**
	 * Getter method for the label.
	 * @return the label
	 */
	public TextLine getLabel() {
		return label;
	}

	/**
	 * Getter method for the textField.
	 * @return the textField
	 */
	public TextField getTextField() {
		return textField;
	}

}
