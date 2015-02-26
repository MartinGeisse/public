/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.lang.reflect.Constructor;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.EnclosureConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.IncludeBackendConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.LazyLoadContainerConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.LinkConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.PieChartConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.TabPanelConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.CheckboxConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.FieldPathFeedbackPanelConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.FormConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.FormFieldModifier;
import name.martingeisse.guiserver.configuration.content.basic.form.FormFieldModifierBinding;
import name.martingeisse.guiserver.configuration.content.basic.form.SubmitButtonConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.TextFieldConfiguration;
import name.martingeisse.guiserver.configuration.content.bootstrap.navbar.NavigationBarBinding;
import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;
import name.martingeisse.guiserver.xml.attribute.AttributeValueBinding;
import name.martingeisse.guiserver.xml.attribute.DefaultAttributeValueBinding;
import name.martingeisse.guiserver.xml.builder.XmlBindingBuilder;
import name.martingeisse.guiserver.xml.content.XmlContentObjectBinding;
import name.martingeisse.guiserver.xml.element.ElementClassInstanceBinding;
import name.martingeisse.guiserver.xml.result.MarkupContent;
import name.martingeisse.guiserver.xml.value.TextBooleanBinding;
import name.martingeisse.guiserver.xml.value.TextIntegerBinding;
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
		try {
			XmlBindingBuilder<ComponentConfiguration> builder = new XmlBindingBuilder<>();
			
			// known attribute-to-constructor-parameter bindings
			builder.addAttributeTextValueBinding(String.class, TextStringBinding.INSTANCE);
			builder.addAttributeTextValueBinding(Boolean.class, TextBooleanBinding.INSTANCE);
			builder.addAttributeTextValueBinding(Boolean.TYPE, TextBooleanBinding.INSTANCE);
			builder.addAttributeTextValueBinding(Integer.class, TextIntegerBinding.INSTANCE);
			builder.addAttributeTextValueBinding(Integer.TYPE, TextIntegerBinding.INSTANCE);
			
			// known child object classes
			{
				AttributeValueBinding<?>[] attributeBindings = {
					new DefaultAttributeValueBinding<>("title", TextStringBinding.INSTANCE),
					new DefaultAttributeValueBinding<>("selector", TextStringBinding.INSTANCE),
				};
				Constructor<TabPanelConfiguration.TabEntry> constructor = TabPanelConfiguration.TabEntry.class.getConstructor(String.class, String.class, MarkupContent.class);
				builder.addChildElementObjectBinding(TabPanelConfiguration.TabEntry.class, new ElementClassInstanceBinding<>(constructor, attributeBindings, builder.getRecursiveMarkupBinding()));
			}
			builder.addChildElementObjectBinding(FormFieldModifier.class, new FormFieldModifierBinding());
			
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
			builder.addComponentConfigurationClass(TextFieldConfiguration.class);
			builder.addComponentConfigurationClass(CheckboxConfiguration.class);
			builder.addComponentConfigurationBinding("navbar", new NavigationBarBinding(builder));
			
			binding = builder.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public MarkupContent<ComponentConfiguration> parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		return binding.parse(reader);
	}

}
