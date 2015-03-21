/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic.form;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.attribute.SimpleAttributeParser;
import name.martingeisse.guiserver.xml.builder.XmlBindingBuilder;
import name.martingeisse.guiserver.xml.element.AttributeSelectedElementParser;
import name.martingeisse.guiserver.xml.element.ClassInstanceElementParser;
import name.martingeisse.guiserver.xml.element.NameSelectedElementParser;
import name.martingeisse.guiserver.xml.element.ElementParser;
import name.martingeisse.guiserver.xml.value.IntegerValueParser;
import name.martingeisse.guiserver.xml.value.StringValueParser;

import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Binds child elements inside a form field to {@link FormFieldModifier} objects.
 */
public final class FormFieldModifierBinding extends NameSelectedElementParser<FormFieldModifier> {

	/**
	 * Constructor.
	 */
	public FormFieldModifierBinding(XmlBindingBuilder<ComponentGroupConfiguration> builder) {
		super(createBindings(builder));
	}

	/**
	 * 
	 */
	private static Map<String, ElementParser<? extends FormFieldModifier>> createBindings(XmlBindingBuilder<ComponentGroupConfiguration> builder) {
		Map<String, ElementParser<? extends FormFieldModifier>> bindings = new HashMap<>();
		
		// add validator bindings
		final ElementParser<IValidator<?>> validatorBindings = createValidatorBinding();
		bindings.put("validation", new ElementParser<FormFieldModifier>() {
			@Override
			public FormFieldModifier parse(MyXmlStreamReader reader) throws XMLStreamException {
				return new ValidationFormFieldModifier(validatorBindings.parse(reader));
			}
		});
		
		return bindings;
	}
	
	private static AttributeSelectedElementParser<IValidator<?>> createValidatorBinding() {
		try {
			Map<String, ElementParser<? extends IValidator<?>>> bindings = new HashMap<>();
			bindings.put("length", new ClassInstanceElementParser<IValidator<?>>(
				StringValidator.class.getConstructor(Integer.class, Integer.class),
				new AttributeParser<?>[] {
					new SimpleAttributeParser<Integer>("min", true, IntegerValueParser.INSTANCE),
					new SimpleAttributeParser<Integer>("max", true, IntegerValueParser.INSTANCE),
				}, null));
			bindings.put("pattern", new ClassInstanceElementParser<IValidator<?>>(
				PatternValidator.class.getConstructor(String.class),
				new AttributeParser<?>[] {
					new SimpleAttributeParser<String>("pattern", StringValueParser.INSTANCE),
				}, null));
			return new AttributeSelectedElementParser<IValidator<?>>("type", bindings); 
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}
