/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xmlbind.attribute.AttributeValueBinding;
import name.martingeisse.guiserver.xmlbind.attribute.DefaultAttributeValueBinding;
import name.martingeisse.guiserver.xmlbind.content.DelegatingXmlContentObjectBinding;
import name.martingeisse.guiserver.xmlbind.content.MarkupContentBinding;
import name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementNameSelectedObjectBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementObjectBinding;
import name.martingeisse.guiserver.xmlbind.result.MarkupContent;
import name.martingeisse.guiserver.xmlbind.value.TextStringBinding;

/**
 * For now, the markup content binding is fixed, and expressed as
 * this singleton class.
 */
public final class StandardMarkupContentBinding implements XmlContentObjectBinding<MarkupContent<ComponentConfiguration>> {

	/**
	 * the INSTANCE
	 */
	public static final StandardMarkupContentBinding INSTANCE = new StandardMarkupContentBinding();

	/**
	 * the markupContentBinding
	 */
	private final MarkupContentBinding<ComponentConfiguration> markupContentBinding;

	/**
	 * Constructor.
	 */
	public StandardMarkupContentBinding() {
		DelegatingXmlContentObjectBinding<MarkupContent<ComponentConfiguration>> delegatingXmlContentObjectBinding = new DelegatingXmlContentObjectBinding<>();

		AttributeValueBinding<?> linkHrefBinding = new DefaultAttributeValueBinding<>("href", TextStringBinding.INSTANCE);
		AttributeValueBinding<?>[] linkAttributeBindings = new AttributeValueBinding<?>[] {linkHrefBinding};
		ElementClassInstanceBinding<? extends ComponentConfiguration> linkBinding = new ElementClassInstanceBinding<>(LinkConfiguration.class, linkAttributeBindings, delegatingXmlContentObjectBinding);
		
		Map<String, ElementObjectBinding<? extends ComponentConfiguration>> bindings = new HashMap<>();
		bindings.put("link", linkBinding);
		ElementNameSelectedObjectBinding<ComponentConfiguration> elementObjectBinding = new ElementNameSelectedObjectBinding<>(bindings); 
		markupContentBinding = new MarkupContentBinding<>(elementObjectBinding);
		
		delegatingXmlContentObjectBinding.setDelegate(markupContentBinding);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public MarkupContent<ComponentConfiguration> parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		return markupContentBinding.parse(reader);
	}

}
