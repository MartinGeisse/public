/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.EnclosureConfiguration;
import name.martingeisse.guiserver.configuration.content.FieldPathFeedbackPanelConfiguration;
import name.martingeisse.guiserver.configuration.content.FormConfiguration;
import name.martingeisse.guiserver.configuration.content.IncludeBackendConfiguration;
import name.martingeisse.guiserver.configuration.content.LazyLoadContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.LinkConfiguration;
import name.martingeisse.guiserver.configuration.content.PieChartConfiguration;
import name.martingeisse.guiserver.configuration.content.SubmitButtonConfiguration;
import name.martingeisse.guiserver.configuration.content.TabPanelConfiguration;
import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.AttributeValueBinding;
import name.martingeisse.guiserver.xml.attribute.DefaultAttributeValueBinding;
import name.martingeisse.guiserver.xml.builder.XmlBindingBuilder;
import name.martingeisse.guiserver.xml.content.XmlContentObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xml.result.MarkupContent;
import name.martingeisse.guiserver.xml.value.TextBooleanBinding;
import name.martingeisse.guiserver.xml.value.TextStringBinding;

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
	 * the binding
	 */
	private final XmlContentObjectBinding<MarkupContent<ComponentConfiguration>> binding;

	/**
	 * Constructor.
	 */
	public StandardMarkupContentBinding() {
		XmlBindingBuilder<ComponentConfiguration> builder = new XmlBindingBuilder<>();

		// known attribute-to-constructor-parameter bindings
		builder.addAttributeTextValueBinding(String.class, TextStringBinding.INSTANCE);
		builder.addAttributeTextValueBinding(Boolean.class, TextBooleanBinding.INSTANCE);
		builder.addAttributeTextValueBinding(Boolean.TYPE, TextBooleanBinding.INSTANCE);
		
		// known child object classes
		AttributeValueBinding<?>[] attributeBindings = {
			new DefaultAttributeValueBinding<>("title", TextStringBinding.INSTANCE),
			new DefaultAttributeValueBinding<>("selector", TextStringBinding.INSTANCE),
		};
		builder.addChildElementObjectBinding(TabPanelConfiguration.TabEntry.class, new ElementClassInstanceBinding<>(TabPanelConfiguration.TabEntry.class, attributeBindings, builder.getRecursiveMarkupBinding()));
		
		// known component special tags
		builder.addComponentConfigurationClass(EnclosureConfiguration.class);
		builder.addComponentConfigurationClass(IncludeBackendConfiguration.class);
		builder.addComponentConfigurationClass(LazyLoadContainerConfiguration.class);
		builder.addComponentConfigurationClass(LinkConfiguration.class);
		builder.addComponentConfigurationClass(FieldPathFeedbackPanelConfiguration.class);
		builder.addComponentConfigurationClass(FormConfiguration.class);
		builder.addComponentConfigurationClass(PieChartConfiguration.class);
		builder.addComponentConfigurationClass(SubmitButtonConfiguration.class);
		builder.addComponentConfigurationClass(TabPanelConfiguration.class);
		
		binding = builder.build();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public MarkupContent<ComponentConfiguration> parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		return binding.parse(reader);
	}

}
