/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import name.martingeisse.common.terms.Multiplicity;
import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.attribute.SimpleAttributeParser;
import name.martingeisse.guiserver.xml.content.MultiChildParser;
import name.martingeisse.guiserver.xml.content.SingleChildParser;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.element.ClassInstanceElementParser;
import name.martingeisse.guiserver.xml.element.ElementParser;
import name.martingeisse.guiserver.xml.value.ValueParserRegistry;
import name.martingeisse.guiserver.xml.value.ValueParser;

/**
 * Helps build an {@link ClassInstanceElementParser}.
 */
public final class ElementClassInstanceBindingBuilder<T> {

	/**
	 * the targetClass
	 */
	private final Class<? extends T> targetClass;

	/**
	 * the attributeTextValueBindingRegistry
	 */
	private final ValueParserRegistry attributeTextValueBindingRegistry;

	/**
	 * the childElementObjectBindingRegistry
	 */
	private final ChildElementObjectBindingRegistry childElementObjectBindingRegistry;
	
	/**
	 * the markupContentBinding
	 */
	private final ContentParser<?> markupContentBinding;

	/**
	 * the constructor
	 */
	private Constructor<? extends T> constructor;
	
	/**
	 * Constructor.
	 * @param targetClass the target class
	 * @param attributeTextValueBindingRegistry the attribute-text-to-value binding registry
	 * @param childElementObjectBindingRegistry the child-element-to-object binding registry
	 * @param markupContentBinding the binding for the element content
	 */
	public ElementClassInstanceBindingBuilder(Class<? extends T> targetClass, ValueParserRegistry attributeTextValueBindingRegistry, ChildElementObjectBindingRegistry childElementObjectBindingRegistry, ContentParser<?> markupContentBinding) {
		this.targetClass = targetClass;
		this.attributeTextValueBindingRegistry = attributeTextValueBindingRegistry;
		this.childElementObjectBindingRegistry = childElementObjectBindingRegistry;
		this.markupContentBinding = markupContentBinding;
	}
	
	/**
	 * Getter method for the constructor.
	 * @return the constructor
	 */
	public Constructor<? extends T> getConstructor() {
		return constructor;
	}

	/**
	 * Setter method for the constructor.
	 * @param constructor the constructor to set
	 */
	public void setConstructor(Constructor<? extends T> constructor) {
		this.constructor = constructor;
	}
	
	/**
	 * Automatically chooses a constructor.
	 */
	public void chooseConstructor() {
		if (targetClass.getConstructors().length != 1) {
			throw new RuntimeException("expecting exactly one constructor in class " + targetClass);
		}
		this.constructor = castConstructor(targetClass.getConstructors()[0]);
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private static <T> Constructor<T> castConstructor(Constructor<?> constructor) {
		return (Constructor<T>)constructor;
	}

	/**
	 * Builds the binding.
	 */
	public ElementParser<T> build() {
		
		// automatically choose all non-overridden parameters
		if (constructor == null) {
			chooseConstructor();
		}

		// obtain the class annotation
		BindElement classAnnotation = targetClass.getAnnotation(BindElement.class);
		if (classAnnotation == null) {
			throw new IllegalArgumentException("class is not annotated with @BindComponentElement: " + targetClass);
		}

		// handle attribute bindings
		AttributeParser<?>[] attributeBindings = new AttributeParser<?>[classAnnotation.attributes().length];
		if (constructor.getParameterCount() < attributeBindings.length) {
			throw new RuntimeException("constructor for class " + targetClass + " has too few arguments for the attribute bindings specified in @BindComponentElement");
		}
		for (int i = 0; i < attributeBindings.length; i++) {
			Class<?> parameterType = constructor.getParameterTypes()[i];
			BindAttribute attributeAnnotation = classAnnotation.attributes()[i];
			attributeBindings[i] = createAttributeBinding(parameterType, attributeAnnotation);
		}

		// handle either child objects or markup content
		ContentParser<?> contentBinding;
		if (classAnnotation.childObjectMultiplicity() != Multiplicity.ZERO && classAnnotation.acceptsMarkupContent()) {
			throw new RuntimeException("@BindComponentElement doesn't support child objects AND markup content at the same time: " + targetClass);
		} else if (classAnnotation.childObjectMultiplicity() != Multiplicity.ZERO) {
			contentBinding = createContentChildObjectBinding(classAnnotation, constructor);
		} else if (classAnnotation.acceptsMarkupContent()) {
			contentBinding = markupContentBinding;
		} else {
			contentBinding = null;
		}

		// build the binding for the component
		return new ClassInstanceElementParser<>(constructor, attributeBindings, contentBinding);

	}

	/**
	 * Creates a binding between an attribute and a constructor parameter. This is a separate
	 * method to allow T (the type of the constructor parameter) to be used as a static
	 * type variable.
	 */
	private <X> AttributeParser<X> createAttributeBinding(Class<X> parameterType, BindAttribute attributeAnnotation) {
		String name = attributeAnnotation.name();
		boolean optional = (attributeAnnotation.optionality() != AttributeValueBindingOptionality.MANDATORY);
		String defaultValue = (attributeAnnotation.optionality() == AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT ? attributeAnnotation.defaultValue() : null);
		ValueParser<X> textValueBinding = attributeTextValueBindingRegistry.getParser(parameterType);
		if (textValueBinding == null) {
			throw new RuntimeException("no attribute text-to-value binding registered for type " + parameterType);
		}
		if (attributeAnnotation.optionality() == AttributeValueBindingOptionality.OPTIONAL && parameterType.isPrimitive()) {
			throw new RuntimeException("cannot bind an optional attribute without default value to a parameter of primitive type. Attribute name: " + name);
		}
		return new SimpleAttributeParser<X>(name, optional, defaultValue, textValueBinding);
	}

	/**
	 * Creates a binding from content to child object(s).
	 */
	private ContentParser<?> createContentChildObjectBinding(BindElement classAnnotation, Constructor<?> constructor) {
		if (constructor.getParameterCount() != classAnnotation.attributes().length + 1) {
			throw new RuntimeException("expected 1 more constructor parameter to class " + constructor.getDeclaringClass() + " than bound attribtues to handle the content argument");
		}
		Parameter contentParameter = constructor.getParameters()[constructor.getParameterCount() - 1];
		Class<?> contentParameterClass = constructor.getParameterTypes()[constructor.getParameterCount() - 1];
		boolean parameterTypeIndicatesList = (contentParameter.getType() == List.class);
		boolean multiplicityIndicatesList = classAnnotation.childObjectMultiplicity().indicatesList();
		if (parameterTypeIndicatesList != multiplicityIndicatesList) {
			throw new RuntimeException("content parameter list-ness different in parameter type (" + parameterTypeIndicatesList + ") and annotation (" + multiplicityIndicatesList + ")");
		}
		Class<?> childObjectClass;
		if (multiplicityIndicatesList) {
			ParameterizedType contentParameterType = (ParameterizedType)contentParameter.getParameterizedType();
			Type listElementType = contentParameterType.getActualTypeArguments()[0];
			if (listElementType instanceof Class<?>) {
				childObjectClass = (Class<?>)listElementType;
			} else if (listElementType instanceof ParameterizedType) {
				ParameterizedType parameterizedListelementType = (ParameterizedType)listElementType;
				childObjectClass = (Class<?>)parameterizedListelementType.getRawType();
			} else {
				throw new RuntimeException("invalid list element type: " + listElementType);
			}
		} else {
			childObjectClass = contentParameterClass;
		}
		ElementParser<?> childElementObjectBinding = childElementObjectBindingRegistry.getBinding(childObjectClass);
		if (childElementObjectBinding == null) {
			throw new RuntimeException("no child-element-to-object binding registered for child object class " + childObjectClass);
		}
		boolean optional = classAnnotation.childObjectMultiplicity().optional();
		String[] elementNameFilter = (classAnnotation.childObjectElementNameFilter().length == 0 ? null : classAnnotation.childObjectElementNameFilter());
		if (multiplicityIndicatesList) {
			return new MultiChildParser<>(optional, elementNameFilter, childElementObjectBinding);
		} else {
			return new SingleChildParser<>(optional, elementNameFilter, childElementObjectBinding);
		}
	}	

}
