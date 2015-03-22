/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.attribute.SimpleAttributeParser;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.content.ContentParserRegistry;
import name.martingeisse.guiserver.xml.element.ClassInstanceElementParser;
import name.martingeisse.guiserver.xml.element.ElementParser;
import name.martingeisse.guiserver.xml.element.ElementParserRegistry;
import name.martingeisse.guiserver.xml.properties.ParserToMethodBinding;
import name.martingeisse.guiserver.xml.properties.PropertiesBinding;
import name.martingeisse.guiserver.xml.value.ValueParser;
import name.martingeisse.guiserver.xml.value.ValueParserRegistry;

/**
 * Helps build an {@link ClassInstanceElementParser}.
 */
public final class ElementParserBuilder<T> {

	/**
	 * the targetClass
	 */
	private final Class<? extends T> targetClass;

	/**
	 * the valueParserRegistry
	 */
	private final ValueParserRegistry valueParserRegistry;

	/**
	 * the childElementParserRegistry
	 */
	private final ElementParserRegistry childElementParserRegistry;
	
	/**
	 * the contentParserRegistry
	 */
	private final ContentParserRegistry contentParserRegistry;
	
	/**
	 * Constructor.
	 * TODO remove the targetClass and make it a parameter to build(), so this builder can be re-used
	 * 
	 * @param targetClass the target class
	 * @param valueParserRegistry the value parser registry
	 * @param childElementParserRegistry the child-element parser registry
	 * @param contentParserRegistry the content parser registry
	 */
	public ElementParserBuilder(Class<? extends T> targetClass, ValueParserRegistry valueParserRegistry, ElementParserRegistry childElementParserRegistry, ContentParserRegistry contentParserRegistry) {
		this.targetClass = targetClass;
		this.valueParserRegistry = valueParserRegistry;
		this.childElementParserRegistry = childElementParserRegistry;
		this.contentParserRegistry = contentParserRegistry;
	}

	/**
	 * Builds the binding.
	 */
	public ElementParser<T> build() {
		try {
			Constructor<? extends T> constructor = targetClass.getConstructor();
			List<PropertiesBinding<T, ? extends AttributeParser<?>>> attributeBindings = new ArrayList<>();
			Map<String, PropertiesBinding<T, ? extends ElementParser<?>>> childElementBindings = new HashMap<>();
			PropertiesBinding<T, ? extends ContentParser<?>> contentBinding = null;
			for (Method method : targetClass.getMethods()) {
				BindPropertyAttribute attributeAnnotation = method.getAnnotation(BindPropertyAttribute.class);
				BindPropertyElement elementAnnotation = method.getAnnotation(BindPropertyElement.class);
				BindPropertyContent contentAnnotation = method.getAnnotation(BindPropertyContent.class);
				if (count(attributeAnnotation) + count(elementAnnotation) + count(contentAnnotation) > 1) {
					throw new RuntimeException("cannot use more than one of " + BindPropertyAttribute.class + ", " + BindPropertyElement.class + ", " + BindPropertyContent.class + " for method " + method.getName() + " of class " + targetClass);
				} else if (attributeAnnotation != null) {
					attributeBindings.add(createAttributeBinding(method));
				} else if (elementAnnotation != null) {
					childElementBindings.put(elementAnnotation.localName(), createElementBinding(method));
				} else if (contentAnnotation != null) {
					if (contentBinding != null) {
						throw new RuntimeException("multiple content bindings for class " + targetClass);
					}
					contentBinding = createContentBinding(method);
				}
			}
			if (!childElementBindings.isEmpty() && contentBinding != null) {
				throw new RuntimeException("class " + targetClass + " has both child-element-to-property-binding(s) and a content-to-property-binding");
			}
			@SuppressWarnings("unchecked")
			PropertiesBinding<T, ? extends AttributeParser<?>>[] attributeBindingsArray = (PropertiesBinding<T, ? extends AttributeParser<?>>[])(new PropertiesBinding<?, ?>[attributeBindings.size()]);
			attributeBindingsArray = attributeBindings.toArray(attributeBindingsArray);
			return new ClassInstanceElementParser<T>(constructor, attributeBindingsArray, contentBinding);			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//
	private int count(Object o) {
		return (o == null ? 0 : 1);
	}

	/**
	 * Creates a binding between an attribute and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 * 
	 * @throws Exception on errors
	 */
	private <P> PropertiesBinding<T, AttributeParser<P>> createAttributeBinding(Method method) throws Exception {
		Class<?> parameterType = determineParameterType(BindPropertyAttribute.class, method);
		
		// extract data from the annotation
		BindPropertyAttribute annotation = method.getAnnotation(BindPropertyAttribute.class);
		String name = annotation.name();
		boolean optional = (annotation.optionality() != AttributeValueBindingOptionality.MANDATORY);
		String defaultValue = (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT ? annotation.defaultValue() : null);
		if (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL && parameterType.isPrimitive()) {
			throw new RuntimeException("cannot bind an optional attribute without default value to a parameter of primitive type. Attribute name: " + name);
		}
		
		// determine the value parser
		ValueParser<?> untypedValueParser = determineParser(method, annotation.valueParserClass(), ValueParser.class, annotation.type(), parameterType, t -> valueParserRegistry.getParser(t));
		@SuppressWarnings("unchecked")
		ValueParser<P> valueParser = (ValueParser<P>)untypedValueParser;

		// build the binding
		AttributeParser<P> attributeParser = new SimpleAttributeParser<>(name, optional, defaultValue, valueParser);
		PropertiesBinding<T, AttributeParser<P>> binding = new ParserToMethodBinding<T, P, AttributeParser<P>>(attributeParser, method);
		return binding;
		
	}

	/**
	 * Creates a binding between an element and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 * 
	 * @throws Exception on errors
	 */
	private <P> PropertiesBinding<T, ElementParser<P>> createElementBinding(Method method) throws Exception {
		Class<?> parameterType = determineParameterType(BindPropertyElement.class, method);
		
		// extract data from the annotation
		BindPropertyElement annotation = method.getAnnotation(BindPropertyElement.class);
		
		// determine the element parser
		ElementParser<?> untypedElementParser = determineParser(method, annotation.elementParserClass(), ElementParser.class, annotation.type(), parameterType, t -> childElementParserRegistry.getParser(t));
		@SuppressWarnings("unchecked")
		ElementParser<P> elementParser = (ElementParser<P>)untypedElementParser;

		// build the binding
		PropertiesBinding<T, ElementParser<P>> binding = new ParserToMethodBinding<T, P, ElementParser<P>>(elementParser, method);
		return binding;
		
	}

	/**
	 * Creates a binding between element content and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 * 
	 * @throws Exception on errors
	 */
	private <P> PropertiesBinding<T, ContentParser<P>> createContentBinding(Method method) throws Exception {
		Class<?> parameterType = determineParameterType(BindPropertyContent.class, method);
		
		// extract data from the annotation
		BindPropertyContent annotation = method.getAnnotation(BindPropertyContent.class);
		
		// determine the content parser
		ContentParser<?> untypedContentParser = determineParser(method, annotation.contentParserClass(), ContentParser.class, annotation.type(), parameterType, t -> contentParserRegistry.getParser(t));
		@SuppressWarnings("unchecked")
		ContentParser<P> contentParser = (ContentParser<P>)untypedContentParser;

		// build the binding
		PropertiesBinding<T, ContentParser<P>> binding = new ParserToMethodBinding<T, P, ContentParser<P>>(contentParser, method);
		return binding;
		
	}

	//
	private Class<?> determineParameterType(Class<? extends Annotation> annotationClass, Method method) {
		if (method.getParameterCount() != 1) {
			throw new RuntimeException("@" + annotationClass.getSimpleName() + " used for method with wrong number of parameters: " + method);
		}
		return method.getParameterTypes()[0];
	}
	
	//
	private <P> P determineParser(Method method, Class<? extends P> specifiedParserClass, Class<? extends P> dummyParserClass, Class<?> specifiedType, Class<?> parameterType, Function<Class<?>, ? extends P> parserRegistry) throws Exception {
		if (specifiedParserClass != dummyParserClass) {
			return specifiedParserClass.newInstance();
		}
		Class<?> conversionType = (specifiedType == void.class ? parameterType : specifiedType);
		if (!parameterType.isAssignableFrom(conversionType)) {
			throw new RuntimeException("incompatible conversion type " + conversionType + " for method " + method);
		}
		P parser = parserRegistry.apply(conversionType);
		if (parser == null) {
			throw new RuntimeException("no parser available for conversion type " + conversionType + " for method " + method);
		}
		return parser;
	}
	
}
