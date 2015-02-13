/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.FormFieldMetadata;
import name.martingeisse.guiserver.xml.ContentStreams;
import name.martingeisse.guiserver.xml.IElementParser;
import name.martingeisse.guiserver.xml.InvalidSpecialElementException;
import name.martingeisse.guiserver.xml.ObjectListParser;
import name.martingeisse.guiserver.xml.XmlReflectionUtil;
import name.martingeisse.guiserver.xml.attribute.AttributeSpecification;
import name.martingeisse.guiserver.xml.attribute.BooleanAttributeParser;
import name.martingeisse.guiserver.xml.attribute.TextAttributeParser;

import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * The base class for parses for all kinds of form fields. This
 * class handles parsing the validation rules.
 */
public class FormFieldParser implements IElementParser<ComponentConfiguration> {

	/**
	 * the nameAttributeSpecification
	 */
	private static final AttributeSpecification nameAttributeSpecification = new AttributeSpecification("name", TextAttributeParser.INSTANCE);

	/**
	 * the requiredAttributeSpecification
	 */
	private static final AttributeSpecification requiredAttributeSpecification = new AttributeSpecification("required", true, true, BooleanAttributeParser.INSTANCE);

	/**
	 * the htmlTypeAttribute
	 */
	private final String htmlTypeAttribute;
	
	/**
	 * the configurationClass
	 */
	private final Class<? extends ComponentConfiguration> configurationClass;

	/**
	 * Constructor.
	 * @param htmlTypeAttribute the value of the HTML "type" attribute
	 */
	public FormFieldParser(String htmlTypeAttribute, Class<? extends ComponentConfiguration> configurationClass) {
		this.htmlTypeAttribute = htmlTypeAttribute;
		this.configurationClass = configurationClass;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.IElementParser#parse(name.martingeisse.guiserver.xml.ContentStreams)
	 */
	@Override
	public void parse(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
		XMLStreamReader reader = streams.getReader();
		XMLStreamWriter writer = streams.getWriter();

		// parse the header of the definition
		final String componentId = ("field" + streams.getComponentAccumulatorSize());
		String name = (String)nameAttributeSpecification.parse(reader);
		boolean required = (Boolean)requiredAttributeSpecification.parse(reader);

		// parse the body of the definition
		streams.next();
		final List<IValidator<?>> validators = new ArrayList<IValidator<?>>();
		new ObjectListParser<ComponentConfiguration>("validation") {
			@Override
			protected void handleSpecialElement(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
				switch (streams.getReader().getLocalName()) {
				
				case "validation": {
					IValidator<?> validator = parseValidator(streams);
					if (validator != null) {
						validators.add(validator);
					}
					break;
				}
					
				default:
					throw new InvalidSpecialElementException("invalid special element in form field: " + streams.getReader().getLocalName());
					
				}

			}
		}.parse(streams);
		streams.next();

		// build the form field
		FormFieldMetadata metadata = new FormFieldMetadata(name, required, validators);
		writer.writeEmptyElement("input");
		writer.writeAttribute("type", htmlTypeAttribute);
		writer.writeAttribute("wicket:id", componentId);
		streams.addComponent(XmlReflectionUtil.invokeSuitableConstructor(configurationClass, new Object[] {componentId, metadata}));

	}

	/**
	 * 
	 */
	private IValidator<?> parseValidator(ContentStreams<ComponentConfiguration> streams) throws XMLStreamException {
		String type = streams.getMandatoryAttribute("type");
		switch (type) {

		case "length": {
			IValidator<?> validator = new StringValidator(parseOptionalInteger(streams, "min"), parseOptionalInteger(streams, "max"));
			skipValidatorElement(streams);
			return validator;
		}
		
		case "pattern": {
			IValidator<?> validator =  new PatternValidator(streams.getMandatoryAttribute("pattern"));
			skipValidatorElement(streams);
			return validator;
		}
		
		default:
			throw new RuntimeException("unknown validator type: " + type);
			
		}
	}
	
	private Integer parseOptionalInteger(ContentStreams<?> streams, String attributeName) {
		String attributeValue = streams.getOptionalAttribute(attributeName);
		return (attributeValue == null ? null : Integer.valueOf(attributeValue));
	}

	private void skipValidatorElement(ContentStreams<?> streams) throws XMLStreamException {
		XMLStreamReader reader = streams.getReader();
		if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
			throw new IllegalStateException("this method must be called while at a START_ELEMENT");
		}
		reader.next();
		streams.skipWhitespace();
		if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
			throw new RuntimeException("validator element should be empty");
		}
		reader.next();
	}

}
