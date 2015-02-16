/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.martingeisse.common.terms.Multiplicity;
import name.martingeisse.guiserver.xml.ConfigurationAssemblerAcceptor;
import name.martingeisse.guiserver.xml.attribute.AttributeValueBinding;
import name.martingeisse.guiserver.xml.attribute.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xml.attribute.BindAttribute;
import name.martingeisse.guiserver.xml.attribute.DefaultAttributeValueBinding;
import name.martingeisse.guiserver.xml.content.DelegatingXmlContentObjectBinding;
import name.martingeisse.guiserver.xml.content.MarkupContentBinding;
import name.martingeisse.guiserver.xml.content.MultiChildObjectBinding;
import name.martingeisse.guiserver.xml.content.SingleChildObjectBinding;
import name.martingeisse.guiserver.xml.content.XmlContentObjectBinding;
import name.martingeisse.guiserver.xml.element.BindComponentElement;
import name.martingeisse.guiserver.xml.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xml.element.ElementNameSelectedObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementObjectBinding;
import name.martingeisse.guiserver.xml.result.MarkupContent;
import name.martingeisse.guiserver.xml.value.TextValueBinding;

/**
 * This class builds the databinding for the XML format.
 * 
 * Calling code can obtain a recursive markup binding from this class. This binding
 * can be used to parse nested markup content. Note that at the time this builder
 * is used, this binding is not fully functional yet.
 *
 * @param <C> the type of components used in markup content
 */
public final class XmlBindingBuilder<C extends ConfigurationAssemblerAcceptor<C>> {

	/**
	 * the recursiveMarkupBinding
	 */
	private final DelegatingXmlContentObjectBinding<MarkupContent<C>> recursiveMarkupBinding;

	/**
	 * the bindings
	 */
	private final Map<String, ElementObjectBinding<? extends C>> componentBindings;

	/**
	 * the attributeTextValueBindingRegistry
	 */
	private final AttributeTextValueBindingRegistry attributeTextValueBindingRegistry;

	/**
	 * the childElementObjectBindingRegistry
	 */
	private final ChildElementObjectBindingRegistry childElementObjectBindingRegistry;

	/**
	 * Constructor.
	 */
	public XmlBindingBuilder() {
		recursiveMarkupBinding = new DelegatingXmlContentObjectBinding<>();
		componentBindings = new HashMap<>();
		attributeTextValueBindingRegistry = new AttributeTextValueBindingRegistry();
		childElementObjectBindingRegistry = new ChildElementObjectBindingRegistry();
	}

	/**
	 * Adds an attribute-text-to-value binding that can be used during parameter-to-attribute
	 * mapping for {@link BindAttribute} annotations.
	 * 
	 * @param parameterType the constructor parameter type
	 * @param binding the text-to-value binding that parses the attribute value
	 */
	public <T> void addAttributeTextValueBinding(Class<T> parameterType, TextValueBinding<T> binding) {
		attributeTextValueBindingRegistry.addBinding(parameterType, binding);
	}

	/**
	 * Adds a child-element-to-object binding that can be used to map child elements to
	 * child objects.
	 * 
	 * @param childObjectType the constructor parameter child object type
	 * @param binding the element-to-object binding that parses the child element
	 */
	public <T> void addChildElementObjectBinding(Class<T> childObjectType, ElementObjectBinding<T> binding) {
		childElementObjectBindingRegistry.addBinding(childObjectType, binding);
	}

	/**
	 * Getter method for the recursiveMarkupBinding.
	 * @return the recursiveMarkupBinding
	 */
	public DelegatingXmlContentObjectBinding<MarkupContent<C>> getRecursiveMarkupBinding() {
		return recursiveMarkupBinding;
	}

	/**
	 * Adds a component configuration class to this builder. The class must be annotated
	 * with {@link BindComponentElement}.
	 * 
	 * @param theClass the class to add
	 */
	public void addComponentConfigurationClass(Class<? extends C> theClass) {

		// obtain the class annotation
		BindComponentElement classAnnotation = theClass.getAnnotation(BindComponentElement.class);
		if (classAnnotation == null) {
			throw new IllegalArgumentException("class is not annotated with @BindComponentElement: " + theClass);
		}

		// handle attribute bindings
		AttributeValueBinding<?>[] attributeBindings = new AttributeValueBinding<?>[classAnnotation.attributes().length];
		Constructor<?> constructor = ElementClassInstanceBinding.chooseConstructor(theClass);
		if (constructor.getParameterCount() < attributeBindings.length) {
			throw new RuntimeException("constructor for class " + theClass + " has too few arguments for the attribute bindings specified in @BindComponentElement");
		}
		for (int i = 0; i < attributeBindings.length; i++) {
			Class<?> parameterType = constructor.getParameterTypes()[i];
			BindAttribute attributeAnnotation = classAnnotation.attributes()[i];
			attributeBindings[i] = createAttributeBinding(parameterType, attributeAnnotation);
		}

		// handle either child objects or markup content
		XmlContentObjectBinding<?> contentBinding;
		if (classAnnotation.childObjectMultiplicity() != Multiplicity.ZERO && classAnnotation.acceptsMarkupContent()) {
			throw new RuntimeException("@BindComponentElement doesn't support child objects AND markup content at the same time: " + theClass);
		} else if (classAnnotation.childObjectMultiplicity() != Multiplicity.ZERO) {
			contentBinding = createContentChildObjectBinding(classAnnotation, constructor);
		} else if (classAnnotation.acceptsMarkupContent()) {
			contentBinding = recursiveMarkupBinding;
		} else {
			contentBinding = null;
		}

		// build the binding for the component
		ElementClassInstanceBinding<? extends C> componentBinding = new ElementClassInstanceBinding<>(theClass, attributeBindings, contentBinding);
		addComponentConfigurationBinding(classAnnotation.localName(), componentBinding);

	}

	/**
	 * Creates a binding between an attribute and a constructor parameter. This is a separate
	 * method to allow T (the type of the constructor parameter) to be used as a static
	 * type variable.
	 */
	private <T> AttributeValueBinding<T> createAttributeBinding(Class<T> parameterType, BindAttribute attributeAnnotation) {
		String name = attributeAnnotation.name();
		boolean optional = (attributeAnnotation.optionality() != AttributeValueBindingOptionality.MANDATORY);
		String defaultValue = (attributeAnnotation.optionality() == AttributeValueBindingOptionality.OPTIONAL_WITH_DEFAULT ? attributeAnnotation.defaultValue() : null);
		TextValueBinding<T> textValueBinding = attributeTextValueBindingRegistry.getBinding(parameterType);
		if (textValueBinding == null) {
			throw new RuntimeException("no attribute text-to-value binding registered for type " + parameterType);
		}
		if (attributeAnnotation.optionality() == AttributeValueBindingOptionality.OPTIONAL && parameterType.isPrimitive()) {
			throw new RuntimeException("cannot bind an optional attribute without default value to a parameter of primitive type. Attribute name: " + name);
		}
		return new DefaultAttributeValueBinding<T>(name, optional, defaultValue, textValueBinding);
	}

	/**
	 * Creates a binding from content to child object(s).
	 */
	private XmlContentObjectBinding<?> createContentChildObjectBinding(BindComponentElement classAnnotation, Constructor<?> constructor) {
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
		ElementObjectBinding<?> childElementObjectBinding = childElementObjectBindingRegistry.getBinding(childObjectClass);
		if (childElementObjectBinding == null) {
			throw new RuntimeException("no child-element-to-object binding registered for child object class " + childObjectClass);
		}
		boolean optional = classAnnotation.childObjectMultiplicity().optional();
		String[] elementNameFilter = (classAnnotation.childObjectElementNameFilter().length == 0 ? null : classAnnotation.childObjectElementNameFilter());
		if (multiplicityIndicatesList) {
			return new MultiChildObjectBinding<>(optional, elementNameFilter, childElementObjectBinding);
		} else {
			return new SingleChildObjectBinding<>(optional, elementNameFilter, childElementObjectBinding);
		}
	}

	/**
	 * Adds a component configuration binding to this builder.
	 * 
	 * @param localElementName the local element name
	 * @param binding the binding
	 */
	public void addComponentConfigurationBinding(String localElementName, ElementObjectBinding<? extends C> binding) {
		componentBindings.put(localElementName, binding);
	}

	/**
	 * Builds the databinding for the XML format.
	 * 
	 * @return the databinding
	 */
	public XmlContentObjectBinding<MarkupContent<C>> build() {
		ElementNameSelectedObjectBinding<C> elementObjectBinding = new ElementNameSelectedObjectBinding<>(componentBindings);
		MarkupContentBinding<C> markupContentBinding = new MarkupContentBinding<C>(elementObjectBinding);
		recursiveMarkupBinding.setDelegate(markupContentBinding);
		return markupContentBinding;
	}

}
