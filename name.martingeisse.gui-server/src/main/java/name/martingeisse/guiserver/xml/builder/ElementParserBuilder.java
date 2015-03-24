/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import name.martingeisse.guiserver.xml.attribute.AttributeParser;
import name.martingeisse.guiserver.xml.attribute.SimpleAttributeParser;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.content.ContentParserRegistry;
import name.martingeisse.guiserver.xml.element.ClassInstanceElementParser;
import name.martingeisse.guiserver.xml.element.ContentParserWrapper;
import name.martingeisse.guiserver.xml.element.ElementParser;
import name.martingeisse.guiserver.xml.element.ElementParserRegistry;
import name.martingeisse.guiserver.xml.properties.ContentPropertiesBinding;
import name.martingeisse.guiserver.xml.properties.NameSelectedPropertiesBinding;
import name.martingeisse.guiserver.xml.properties.ParserToMethodBinding;
import name.martingeisse.guiserver.xml.properties.PropertiesBinding;
import name.martingeisse.guiserver.xml.value.ValueParser;
import name.martingeisse.guiserver.xml.value.ValueParserRegistry;

/**
 * Helps build an {@link ClassInstanceElementParser}.
 * 
 * This object is constructed with reference to existing attribute/element/parser registries. Changes to those registries
 * (such as newly registered parsers) will be visible through this builder!
 */
public final class ElementParserBuilder {

	/**
	 * the valueParserRegistry
	 */
	private final ValueParserRegistry valueParserRegistry;

	/**
	 * the elementParserRegistry
	 */
	private final ElementParserRegistry elementParserRegistry;
	
	/**
	 * the contentParserRegistry
	 */
	private final ContentParserRegistry contentParserRegistry;
	
	/**
	 * Constructor.
	 * @param valueParserRegistry the value parser registry
	 * @param elementParserRegistry the child-element parser registry
	 * @param contentParserRegistry the content parser registry
	 */
	public ElementParserBuilder(ValueParserRegistry valueParserRegistry, ElementParserRegistry elementParserRegistry, ContentParserRegistry contentParserRegistry) {
		this.valueParserRegistry = valueParserRegistry;
		this.elementParserRegistry = elementParserRegistry;
		this.contentParserRegistry = contentParserRegistry;
	}

	/**
	 * Builds the binding.
	 */
	public <T> ElementParser<T> build(Class<? extends T> targetClass) {
		if (targetClass.getAnnotation(StructuredElement.class) == null) {
			throw new IllegalArgumentException("class is not annotated with " + StructuredElement.class.getSimpleName() + ": " + targetClass);
		}
		try {
			Constructor<? extends T> constructor = targetClass.getConstructor();
			List<PropertiesBinding<T, ? extends AttributeParser<?>>> attributeBindings = new ArrayList<>();
			NameSelectedPropertiesBinding<T, ElementParser<?>> elementBinding = new NameSelectedPropertiesBinding<T, ElementParser<?>>();
			boolean hasElementBinding = false;
			PropertiesBinding<T, ? extends ContentParser<?>> contentBinding = null;
			for (Method method : targetClass.getMethods()) {
				BindAttribute attributeAnnotation = method.getAnnotation(BindAttribute.class);
				BindElement elementAnnotation = method.getAnnotation(BindElement.class);
				BindContent contentAnnotation = method.getAnnotation(BindContent.class);
				if (count(attributeAnnotation) + count(elementAnnotation) + count(contentAnnotation) > 1) {
					throw new RuntimeException("cannot use more than one of " + BindAttribute.class + ", " + BindElement.class + ", " + BindContent.class + " for method " + method.getName() + " of class " + targetClass);
				} else if (attributeAnnotation != null) {
					attributeBindings.add(createAttributeBinding(method));
				} else if (elementAnnotation != null) {
					elementBinding.addBinding(elementAnnotation.localName(), createElementBinding(method));
					hasElementBinding = true;
				} else if (contentAnnotation != null) {
					if (contentBinding != null) {
						throw new RuntimeException("multiple content bindings for class " + targetClass);
					}
					contentBinding = createContentBinding(method);
				}
			}
			@SuppressWarnings("unchecked")
			PropertiesBinding<T, ? extends AttributeParser<?>>[] attributeBindingsArray = (PropertiesBinding<T, ? extends AttributeParser<?>>[])(new PropertiesBinding<?, ?>[attributeBindings.size()]);
			attributeBindingsArray = attributeBindings.toArray(attributeBindingsArray);
			if (hasElementBinding) {
				if (contentBinding != null) {
					throw new RuntimeException("class " + targetClass + " has both child-element-to-property-binding(s) and a content-to-property-binding");
				} else {
					contentBinding = new ContentPropertiesBinding<>(elementBinding);
				}
			}
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
	private <T, P> PropertiesBinding<T, AttributeParser<P>> createAttributeBinding(Method method) throws Exception {
		Class<?> parameterType = determineParameterType(BindAttribute.class, method);
		
		// extract data from the annotation
		BindAttribute annotation = method.getAnnotation(BindAttribute.class);
		String name = annotation.name();
		boolean optional = (annotation.optionality() != AttributeValueBindingOptionality.MANDATORY);
		String defaultValue = (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT ? annotation.defaultValue() : null);
		if (annotation.optionality() == AttributeValueBindingOptionality.OPTIONAL && parameterType.isPrimitive()) {
			throw new RuntimeException("cannot bind an optional attribute without default value to a parameter of primitive type. Attribute name: " + name);
		}
		
		// determine the value parser
		ValueParser<?> untypedValueParser = determineParser(method, annotation.type(), parameterType, t -> valueParserRegistry.getParser(t));
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
	private <T, P> PropertiesBinding<T, ElementParser<P>> createElementBinding(Method method) throws Exception {
		Class<?> parameterType = determineParameterType(BindElement.class, method);
		
		// extract data from the annotation
		BindElement annotation = method.getAnnotation(BindElement.class);
		
		// determine the element parser
		Function<Class<?>, ? extends ElementParser<P>> parserProvider = this::getOrCreateElementParser;
		ElementParser<?> untypedElementParser = determineParser(method, annotation.type(), parameterType, parserProvider);
		@SuppressWarnings("unchecked")
		ElementParser<P> elementParser = (ElementParser<P>)untypedElementParser;

		// build the binding
		PropertiesBinding<T, ElementParser<P>> binding = new ParserToMethodBinding<T, P, ElementParser<P>>(elementParser, method);
		return binding;
		
	}
	
	/**
	 * 
	 */
	private <T> ElementParser<T> getOrCreateElementParser(Class<?> targetClass) {
		@SuppressWarnings("unchecked")
		Class<T> targetClassTyped = (Class<T>)targetClass;
		
		// check for a pre-registered or previously created parser
		ElementParser<T> parser = elementParserRegistry.getParser(targetClassTyped);
		if (parser != null) {
			return parser;
		}
		
		// check if a structured-element parser can be created automatically
		if (targetClass.getAnnotation(StructuredElement.class) != null) {
			parser = build(targetClassTyped);
			elementParserRegistry.addParser(targetClassTyped, parser);
			return parser;
		}
		
		// check if we can create a parser by wrapping a content parser
		ContentParser<T> contentParser = contentParserRegistry.getParser(targetClassTyped);
		if (contentParser != null) {
			parser = new ContentParserWrapper<>(contentParser);
			elementParserRegistry.addParser(targetClassTyped, parser);
			return parser;
		}
		
		throw new RuntimeException("cannot find parser for this class, and no @StructuredElement annotation is present: " + targetClass);
	}

	/**
	 * Creates a binding between element content and a method. This is a separate
	 * method to allow P (the type of the constructor parameter) to be used as a static
	 * type variable.
	 * 
	 * @throws Exception on errors
	 */
	private <T, P> PropertiesBinding<T, ContentParser<P>> createContentBinding(Method method) throws Exception {
		Class<?> parameterType = determineParameterType(BindContent.class, method);
		
		// extract data from the annotation
		BindContent annotation = method.getAnnotation(BindContent.class);
		
		// determine the content parser
		ContentParser<?> untypedContentParser = determineParser(method, annotation.type(), parameterType, t -> contentParserRegistry.getParser(t));
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
	private <P> P determineParser(Method method, Class<?> specifiedType, Class<?> parameterType, Function<Class<?>, ? extends P> parserRegistry) throws Exception {
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
