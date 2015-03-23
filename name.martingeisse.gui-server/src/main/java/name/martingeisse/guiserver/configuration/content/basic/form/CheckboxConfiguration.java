/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic.form;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.gui.FieldPathBehavior;
import name.martingeisse.guiserver.xml.builder.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.builder.BindComponentElement;
import name.martingeisse.guiserver.xml.builder.BindPropertyAttribute;
import name.martingeisse.guiserver.xml.builder.BindPropertyElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.validation.IValidator;

/**
 * Represents a checkbox.
 */
@BindComponentElement(localName = "checkbox")
public final class CheckboxConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the required
	 */
	private boolean required;

	/**
	 * the validators
	 */
	private List<IValidator<?>> validators = new ArrayList<>();
	
	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	@BindPropertyAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Getter method for the required.
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Setter method for the required.
	 * @param required the required to set
	 */
	@BindPropertyAttribute(name = "required", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "true")
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	/**
	 * Adds a validator to this form field.
	 * @param validator the validator to add
	 */
	@BindPropertyElement(localName = "validation")
	public void addValidator(IValidator<?> validator) {
		validators.add(validator);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xml.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("input");
		assembler.getMarkupWriter().writeAttribute("type", "checkbox");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		CheckBox checkbox = new CheckBox(getComponentId());
		checkbox.setRequired(required);
		checkbox.add(new FieldPathBehavior(name));
		for (IValidator<?> validator : validators) {
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
