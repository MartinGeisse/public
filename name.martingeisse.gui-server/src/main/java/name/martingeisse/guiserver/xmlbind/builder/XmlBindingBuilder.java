/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.builder;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import name.martingeisse.common.terms.Multiplicity;
import name.martingeisse.guiserver.xmlbind.ConfigurationAssemblerAcceptor;
import name.martingeisse.guiserver.xmlbind.attribute.AttributeValueBinding;
import name.martingeisse.guiserver.xmlbind.attribute.AttributeValueBindingOptionality;
import name.martingeisse.guiserver.xmlbind.attribute.BindAttribute;
import name.martingeisse.guiserver.xmlbind.attribute.DefaultAttributeValueBinding;
import name.martingeisse.guiserver.xmlbind.content.DelegatingXmlContentObjectBinding;
import name.martingeisse.guiserver.xmlbind.content.MarkupContentBinding;
import name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding;
import name.martingeisse.guiserver.xmlbind.element.BindComponentElement;
import name.martingeisse.guiserver.xmlbind.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementNameSelectedObjectBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding;
import name.martingeisse.guiserver.xmlbind.result.MarkupContent;
import name.martingeisse.guiserver.xmlbind.value.TextValueBinding;

import org.apache.commons.lang3.NotImplementedException;

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
	 * Constructor.
	 */
	public XmlBindingBuilder() {
		recursiveMarkupBinding = new DelegatingXmlContentObjectBinding<>();
		componentBindings = new HashMap<>();
		attributeTextValueBindingRegistry = new AttributeTextValueBindingRegistry();
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
		if (constructor.getParameterTypes().length < attributeBindings.length) {
			throw new RuntimeException("constructor for class " + theClass + " has too few arguments for the attribute bindings specified in @BindComponentElement");
		}
		for (int i=0; i<attributeBindings.length; i++) {
			Class<?> parameterType = constructor.getParameterTypes()[i];
			BindAttribute attributeAnnotation = classAnnotation.attributes()[i];
			attributeBindings[i] = createAttributeBinding(parameterType, attributeAnnotation);
		}
		
		// handle either child objects or markup content
		XmlContentObjectBinding<?> contentBinding;
		if (classAnnotation.childObjectMultiplicity() != Multiplicity.ZERO && classAnnotation.acceptsMarkupContent()) {
			throw new RuntimeException("@BindComponentElement doesn't support child objects AND markup content at the same time: " + theClass);
		} else if (classAnnotation.childObjectMultiplicity() != Multiplicity.ZERO) {
			throw new NotImplementedException("child objects not yet implemented");
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
		return new DefaultAttributeValueBinding<T>(name, optional, defaultValue, textValueBinding);
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
