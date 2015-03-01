/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.builder;

import java.util.HashMap;
import java.util.Map;

import name.martingeisse.guiserver.xml.ConfigurationAssemblerAcceptor;
import name.martingeisse.guiserver.xml.content.DelegatingXmlContentObjectBinding;
import name.martingeisse.guiserver.xml.content.MarkupContentBinding;
import name.martingeisse.guiserver.xml.content.XmlContentObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementNameSelectedObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementObjectBinding;
import name.martingeisse.guiserver.xml.result.MarkupContent;
import name.martingeisse.guiserver.xml.value.ValueParserRegistry;
import name.martingeisse.guiserver.xml.value.ValueParser;

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
	private final ValueParserRegistry attributeTextValueBindingRegistry;

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
		attributeTextValueBindingRegistry = new ValueParserRegistry();
		childElementObjectBindingRegistry = new ChildElementObjectBindingRegistry();
	}

	/**
	 * Adds an attribute-text-to-value binding that can be used during parameter-to-attribute
	 * mapping for {@link BindAttribute} annotations.
	 * 
	 * @param parameterType the constructor parameter type
	 * @param binding the text-to-value binding that parses the attribute value
	 */
	public <T> void addAttributeTextValueBinding(Class<T> parameterType, ValueParser<T> binding) {
		attributeTextValueBindingRegistry.addParser(parameterType, binding);
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
	 * Creates a new {@link ElementClassInstanceBindingBuilder} for the specified target class.
	 * 
	 * @param targetClass the target class
	 * @return the builder
	 */
	public <T> ElementClassInstanceBindingBuilder<T> newElementClassInstanceBindingBuilder(Class<? extends T> targetClass) {
		return new ElementClassInstanceBindingBuilder<>(targetClass, attributeTextValueBindingRegistry, childElementObjectBindingRegistry, recursiveMarkupBinding);
	}
	
	/**
	 * Adds a component group configuration class to this builder. The class must be annotated
	 * with {@link BindComponentElement}.
	 * 
	 * @param targetClass the class to add
	 */
	public void addComponentGroupConfigurationClass(Class<? extends C> targetClass) {
		BindComponentElement annotation = targetClass.getAnnotation(BindComponentElement.class);
		if (annotation == null) {
			throw new RuntimeException("class " + targetClass + " is not annotated with @BindComponentElement");
		}
		addComponentGroupConfigurationBinding(annotation.localName(), newElementClassInstanceBindingBuilder(targetClass).build());
	}

	/**
	 * Adds a component group configuration binding to this builder.
	 * 
	 * @param localElementName the local element name
	 * @param binding the binding
	 */
	public void addComponentGroupConfigurationBinding(String localElementName, ElementObjectBinding<? extends C> binding) {
		componentBindings.put(localElementName, binding);
	}
	
	/**
	 * Returns the component binding for the specified local element name.
	 * 
	 * @param localElementName the local element name
	 * @return the binding
	 */
	public ElementObjectBinding<? extends C> getComponentBinding(String localElementName) {
		return componentBindings.get(localElementName);
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
