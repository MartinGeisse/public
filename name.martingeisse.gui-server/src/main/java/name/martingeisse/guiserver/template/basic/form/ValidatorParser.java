/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.template.basic.form;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.template.ComponentGroupConfiguration;
import name.martingeisse.guiserver.template.RecursiveContentParserBuilder;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.SimpleAttributeParser;
import name.martingeisse.guiserver.xml.element.AbstractEmptyElementParser;
import name.martingeisse.guiserver.xml.element.AttributeSelectedElementParser;
import name.martingeisse.guiserver.xml.value.IntegerValueParser;
import name.martingeisse.guiserver.xml.value.StringValueParser;

import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Parses {@link IValidator} objects.
 */
public final class ValidatorParser extends AttributeSelectedElementParser<IValidator<?>> {

	/**
	 * Constructor.
	 */
	public ValidatorParser(RecursiveContentParserBuilder<ComponentGroupConfiguration> builder) {
		setAttributeName("type");
		addParser("length", new AbstractEmptyElementParser<IValidator<?>>() {
			private final SimpleAttributeParser<Integer> minParser = new SimpleAttributeParser<Integer>("min", true, IntegerValueParser.INSTANCE);
			private final SimpleAttributeParser<Integer> maxParser = new SimpleAttributeParser<Integer>("max", true, IntegerValueParser.INSTANCE);
			@Override
			protected IValidator<?> createResult(MyXmlStreamReader reader) throws XMLStreamException {
				return new StringValidator(minParser.parse(reader), maxParser.parse(reader));
			}
		});
		addParser("pattern", new AbstractEmptyElementParser<IValidator<?>>() {
			private final SimpleAttributeParser<String> patternParser = new SimpleAttributeParser<String>("pattern", StringValueParser.INSTANCE);
			@Override
			protected IValidator<?> createResult(MyXmlStreamReader reader) throws XMLStreamException {
				return new PatternValidator(patternParser.parse(reader));
			}
		});
	}

}
