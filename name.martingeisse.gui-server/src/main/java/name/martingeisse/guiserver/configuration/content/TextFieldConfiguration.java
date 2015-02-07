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
	 * the name
	 */
	private final String name;
	
	/**
	 * the required
	 */
	private final boolean required;

	/**
	 * Constructor.
	 * @param id the wicket ID
	 * @param name the field name
	 * @param required whether the field is a required field
	 */
	public TextFieldConfiguration(String id, String name, boolean required) {
		super(id);
		this.name = name;
		this.required = required;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		return new TextField<>(getId()).setRequired(required).add(new FieldPathBehavior(name));
	}
	
}
