/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.gui.FieldPathBehavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;

/**
 * Represents a text field.
 */
public final class TextFieldConfiguration extends AbstractComponentConfiguration {

	/**
	 * the metadata
	 */
	private final FormFieldMetadata metadata;

	/**
	 * Constructor.
	 * @param id the wicket ID
	 * @param metadata the meta-data for this form field
	 */
	public TextFieldConfiguration(String id, FormFieldMetadata metadata) {
		super(id);
		this.metadata = metadata;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		TextField<?> textField = new TextField<>(getId());
		textField.setRequired(metadata.isRequired());
		textField.add(new FieldPathBehavior(metadata.getName()));
		return textField;
	}
	
}
