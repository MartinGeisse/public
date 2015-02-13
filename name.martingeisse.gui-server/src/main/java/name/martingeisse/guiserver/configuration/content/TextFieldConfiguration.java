/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.gui.FieldPathBehavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.IValidator;

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
		for (IValidator<?> validator : metadata.getValidators()) {
			addValidator(textField, validator);
		}
		return textField;
	}
	
	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	private void addValidator(TextField textField, IValidator validator) {
		textField.add(validator);
	}
	
}
