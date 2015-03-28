/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template.bootstrap.form;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.common.terms.IConsumer;
import name.martingeisse.guiserver.component.FieldPathBehavior;
import name.martingeisse.guiserver.component.FieldPathFeedbackMessageFilter;
import name.martingeisse.guiserver.template.AbstractComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.ConfigurationAssembler;
import name.martingeisse.guiserver.xml.builder.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.builder.BindAttribute;
import name.martingeisse.guiserver.xml.builder.BindElement;
import name.martingeisse.guiserver.xml.builder.RegisterComponentElement;
import name.martingeisse.guiserver.xml.builder.StructuredElement;
import name.martingeisse.wicket.component.misc.BootstrapFeedbackPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.IValidator;

/**
 * Represents a text field, including validation errors and the corresponding Bootstrap markup.
 */
@StructuredElement
@RegisterComponentElement(localName = "bsTextField")
public final class BootstrapTextFieldConfiguration extends AbstractComponentGroupConfiguration {

	/**
	 * the name
	 */
	private String name;

	/**
	 * the label
	 */
	private String label;

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
	@BindAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the label.
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Setter method for the label.
	 * @param label the label to set
	 */
	@BindAttribute(name = "label")
	public void setLabel(String label) {
		this.label = label;
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
	@BindAttribute(name = "required", optionality = AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT, defaultValue = "true")
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * Adds a validator to this form field.
	 * @param validator the validator to add
	 */
	@BindElement(localName = "validation")
	public void addValidator(IValidator<?> validator) {
		validators.add(validator);
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
		textField.setRequired(required);
		textField.add(new FieldPathBehavior(name));
		for (IValidator<?> validator : validators) {
			addValidator(textField, validator);
		}
		consumer.consume(textField);

		// build the label
		consumer.consume(new FormComponentLabel(getComponentBaseId() + "-label", textField).add(new Label("labelText", label)));

		// build the error message feedback panel
		consumer.consume(new BootstrapFeedbackPanel(getComponentBaseId() + "-error", new FieldPathFeedbackMessageFilter(name)));

	}

	@SuppressWarnings({
		"unchecked", "rawtypes"
	})
	private void addValidator(TextField textField, IValidator validator) {
		textField.add(validator);
	}

}
