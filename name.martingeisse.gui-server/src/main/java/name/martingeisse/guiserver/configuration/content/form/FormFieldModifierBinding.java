/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.form;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.AttributeValueBinding;
import name.martingeisse.guiserver.xml.attribute.DefaultAttributeValueBinding;
import name.martingeisse.guiserver.xml.element.ElementAttributeSelectedObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xml.element.ElementNameSelectedObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementObjectBinding;
import name.martingeisse.guiserver.xml.value.TextIntegerBinding;
import name.martingeisse.guiserver.xml.value.TextStringBinding;

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
	public FormFieldModifierBinding() {
		super(createBindings());
	}

	/**
	 * 
	 */
	private static Map<String, ElementObjectBinding<? extends FormFieldModifier>> createBindings() {
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
				new AttributeValueBinding<?>[] {
					new DefaultAttributeValueBinding<Integer>("min", true, TextIntegerBinding.INSTANCE),
					new DefaultAttributeValueBinding<Integer>("max", true, TextIntegerBinding.INSTANCE),
				}, null));
			bindings.put("pattern", new ElementClassInstanceBinding<IValidator<?>>(
				PatternValidator.class.getConstructor(String.class),
				new AttributeValueBinding<?>[] {
					new DefaultAttributeValueBinding<String>("pattern", TextStringBinding.INSTANCE),
				}, null));
			return new ElementAttributeSelectedObjectBinding<IValidator<?>>("type", bindings); 
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

}
