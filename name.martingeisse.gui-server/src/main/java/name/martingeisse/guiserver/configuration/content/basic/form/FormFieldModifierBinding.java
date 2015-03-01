/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.basic.form;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.attribute.SimpleAttributeParser;
import name.martingeisse.guiserver.xml.builder.XmlBindingBuilder;
import name.martingeisse.guiserver.xml.element.ElementAttributeSelectedObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xml.element.ElementNameSelectedObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementObjectBinding;
import name.martingeisse.guiserver.xml.value.IntegerValueParser;
import name.martingeisse.guiserver.xml.value.StringValueParser;

import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.PatternValidator;
import org.apache.wicket.validation.validator.StringValidator;

/**
 * Binds child elements inside a form field to {@link FormFieldModifier} objects.
 */
public final class FormFieldModifierBinding extends ElementNameSelectedObjectBinding<FormFieldModifier> {

	/**
	 * Constructor.
	 */
	public FormFieldModifierBinding(XmlBindingBuilder<ComponentGroupConfiguration> builder) {
		super(createBindings(builder));
	}

	/**
	 * 
	 */
	private static Map<String, ElementObjectBinding<? extends FormFieldModifier>> createBindings(XmlBindingBuilder<ComponentGroupConfiguration> builder) {
		Map<String, ElementObjectBinding<? extends FormFieldModifier>> bindings = new HashMap<>();
		
		// add validator bindings
		final ElementObjectBinding<IValidator<?>> validatorBindings = createValidatorBinding();
		bindings.put("validation", new ElementObjectBinding<FormFieldModifier>() {
			@Override
			public FormFieldModifier parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
				return new ValidationFormFieldModifier(validatorBindings.parse(reader));
			}
		});
		
		return bindings;
	}
	
	private static ElementAttributeSelectedObjectBinding<IValidator<?>> createValidatorBinding() {
		try {
			Map<String, ElementObjectBinding<? extends IValidator<?>>> bindings = new HashMap<>();
			bindings.put("length", new ElementClassInstanceBinding<IValidator<?>>(
				StringValidator.class.getConstructor(Integer.class, Integer.class),
				new AttributeParser<?>[] {
					new SimpleAttributeParser<Integer>("min", true, IntegerValueParser.INSTANCE),
					new SimpleAttributeParser<Integer>("max", true, IntegerValueParser.INSTANCE),
				}, null));
			bindings.put("pattern", new ElementClassInstanceBinding<IValidator<?>>(
				PatternValidator.class.getConstructor(String.class),
				new AttributeParser<?>[] {
					new SimpleAttributeParser<String>("pattern", StringValueParser.INSTANCE),
				}, null));
			return new ElementAttributeSelectedObjectBinding<IValidator<?>>("type", bindings); 
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}
