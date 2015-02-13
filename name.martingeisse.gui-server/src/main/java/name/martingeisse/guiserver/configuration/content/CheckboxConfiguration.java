/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

import name.martingeisse.guiserver.gui.FieldPathBehavior;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.validation.IValidator;

/**
 * Represents a checkbox.
 */
public final class CheckboxConfiguration extends AbstractComponentConfiguration {

	/**
	 * the metadata
	 */
	private final FormFieldMetadata metadata;

	/**
	 * Constructor.
	 * @param id the wicket ID
	 * @param metadata the meta-data for this form field
	 */
	public CheckboxConfiguration(String id, FormFieldMetadata metadata) {
		super(id);
		this.metadata = metadata;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		CheckBox checkbox = new CheckBox(getId());
		checkbox.setRequired(metadata.isRequired());
		checkbox.add(new FieldPathBehavior(metadata.getName()));
		for (IValidator<?> validator : metadata.getValidators()) {
			addValidator(checkbox, validator);
		}
		return checkbox;
	}

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	private void addValidator(CheckBox checkbox, IValidator validator) {
		checkbox.add(validator);
	}

}
