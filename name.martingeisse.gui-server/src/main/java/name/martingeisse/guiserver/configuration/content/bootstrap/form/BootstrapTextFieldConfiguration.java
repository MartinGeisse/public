/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.bootstrap.form;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.common.terms.IConsumer;
import name.martingeisse.common.terms.Multiplicity;
import name.martingeisse.guiserver.configuration.content.AbstractComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.FormFieldMetadata;
import name.martingeisse.guiserver.configuration.content.basic.form.FormFieldModifier;
import name.martingeisse.guiserver.configuration.content.basic.form.ValidationFormFieldModifier;
import name.martingeisse.guiserver.gui.FieldPathBehavior;
import name.martingeisse.guiserver.gui.FieldPathFeedbackMessageFilter;
import name.martingeisse.guiserver.xml.builder.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.BindElement;
import name.martingeisse.guiserver.xml.result.ConfigurationAssembler;
import name.martingeisse.wicket.component.misc.BootstrapFeedbackPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.IValidator;

/**
 * Represents a text field, including validation errors and the corresponding Bootstrap markup.
 */
@BindElement(localName = "bsTextField", attributes = {
	@BindAttribute(name = "name"), @BindAttribute(name = "label"), @BindAttribute(name = "required", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "true"),
}, childObjectMultiplicity = Multiplicity.ANY, childObjectElementNameFilter = "validation")
public final class BootstrapTextFieldConfiguration extends AbstractComponentGroupConfiguration {

	/**
	 * the name
	 */
	private final String name;

	/**
	 * the label
	 */
	private final String label;

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
	 * @param label the field label
	 * @param required whether this is a required field
	 * @param modifiers field modifiers
	 */
	public BootstrapTextFieldConfiguration(String name, String label, boolean required, List<FormFieldModifier> modifiers) {

		// assign basic fields
		this.name = name;
		this.label = label;
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
		beginFormControlMarkup(assembler);
		assembler.getMarkupWriter().writeStartElement("input");
		assembler.getMarkupWriter().writeAttribute("type", "text");
		assembler.getMarkupWriter().writeAttribute("class", "form-control");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentBaseId());
		endFormControlMarkup(assembler);
	}

	//
	private void beginFormControlMarkup(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {

		// start form group
		assembler.getMarkupWriter().writeStartElement("div");
		assembler.getMarkupWriter().writeAttribute("class", "form-group");

		// label/tool block
		assembler.getMarkupWriter().writeStartElement("div");
		{ // label
			assembler.getMarkupWriter().writeStartElement("label");
			assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentBaseId() + "-label");
			assembler.getMarkupWriter().writeEmptyElement("span");
			assembler.getMarkupWriter().writeAttribute("wicket:id", "labelText");
			assembler.getMarkupWriter().writeEndElement();
		}
		// TODO tools (optional, specified in a child element)
		assembler.getMarkupWriter().writeEndElement();

		// TODO help text block (optional, specified in a child element)

		// error message
		assembler.getMarkupWriter().writeEmptyElement("div");
		assembler.getMarkupWriter().writeAttribute("wicket:id", getComponentBaseId() + "-error");

	}

	//
	private void endFormControlMarkup(ConfigurationAssembler<ComponentGroupConfiguration> assembler) throws XMLStreamException {

		// end form group
		assembler.getMarkupWriter().writeEndElement();

	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration#buildComponents(name.martingeisse.common.terms.IConsumer)
	 */
	@Override
	public void buildComponents(IConsumer<Component> consumer) {

		// build the text field itself
		TextField<?> textField = new TextField<>(getComponentBaseId());
		textField.setRequired(metadata.isRequired());
		textField.add(new FieldPathBehavior(metadata.getName()));
		for (IValidator<?> validator : metadata.getValidators()) {
			addValidator(textField, validator);
		}
		consumer.consume(textField);

		// build the label
		consumer.consume(new FormComponentLabel(getComponentBaseId() + "-label", textField).add(new Label("labelText", label)));

		// build the error message feedback panel
		consumer.consume(new BootstrapFeedbackPanel(getComponentBaseId() + "-error", new FieldPathFeedbackMessageFilter(metadata.getName())));

	}

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	private void addValidator(TextField textField, IValidator validator) {
		textField.add(validator);
	}

}
