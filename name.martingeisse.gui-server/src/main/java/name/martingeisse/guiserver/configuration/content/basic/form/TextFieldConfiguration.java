/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic.form;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.common.terms.Multiplicity;
import name.martingeisse.guiserver.configuration.content.AbstractSingleComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.gui.FieldPathBehavior;
import name.martingeisse.guiserver.xml.builder.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.builder.BindPropertyAttribute;
import name.martingeisse.guiserver.xml.builder.BindComponentElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.IValidator;

/**
 * Represents a text field.
 */
@BindComponentElement(localName = "textField", attributes = {
	@BindPropertyAttribute(name = "name"), @BindPropertyAttribute(name = "required", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "true")
}, childObjectMultiplicity = Multiplicity.ANY, childObjectElementNameFilter = "validation")
public final class TextFieldConfiguration extends AbstractSingleComponentConfiguration {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the required
	 */
	private final boolean required;

	/**
	 * the modifiers
	 */
	private final List<FormFieldModifier> modifiers;

	/**
	 * the metadata
	 */
	private final FormFieldMetadata metadata;

	/**
	 * Constructor.
	 * @param name the field name
	 * @param required whether this is a required field
	 * @param modifiers field modifiers
	 */
	public TextFieldConfiguration(String name, boolean required, List<FormFieldModifier> modifiers) {

		// assign basic fields
		this.name = name;
		this.required = required;
		this.modifiers = modifiers;

		// extract validators
		List<IValidator<?>> validators = new ArrayList<IValidator<?>>();
		for (FormFieldModifier modifier : modifiers) {
			if (modifier instanceof ValidationFormFieldModifier) {
				validators.add(((ValidationFormFieldModifier)modifier).getValidator());
			}
		}

		// build the form field meta-data
		this.metadata = new FormFieldMetadata(name, required, validators);

	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter method for the required.
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Getter method for the modifiers.
	 * @return the modifiers
	 */
	public List<FormFieldModifier> getModifiers() {
		return modifiers;
	}

	/**
	 * Getter method for the metadata.
	 * @return the metadata
	 */
	public FormFieldMetadata getMetadata() {
		return metadata;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.AbstractComponentConfiguration#assemble(name.martingeisse.guiserver.xml.result.ConfigurationAssembler)
	 */
	@Override
	public void assemble(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {
		super.assemble(assembler);
		assembler.getMarkupWriter().writeEmptyElement("input");
		assembler.getMarkupWriter().writeAttribute("type", "text");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentId());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentConfiguration#buildComponent()
	 */
	@Override
	public Component buildComponent() {
		TextField<?> textField = new TextField<>(getComponentId());
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
