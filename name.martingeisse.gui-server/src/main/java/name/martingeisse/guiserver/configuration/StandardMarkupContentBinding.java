/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import java.lang.reflect.Constructor;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
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
import name.martingeisse.guiserver.configuration.content.bootstrap.form.BootstrapFormConfiguration;
import name.martingeisse.guiserver.configuration.content.bootstrap.form.BootstrapTextFieldConfiguration;
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
public final class StandardMarkupContentBinding implements XmlContentObjectBinding<MarkupContent<ComponentGroupConfiguration>> {

	/**
	 * the INSTANCE
	 */
	public static final StandardMarkupContentBinding INSTANCE = new StandardMarkupContentBinding();

	/**
	 * the binding
	 */
	private final XmlContentObjectBinding<MarkupContent<ComponentGroupConfiguration>> binding;

	/**
	 * Constructor.
	 */
	public StandardMarkupContentBinding() {
		try {
			XmlBindingBuilder<ComponentGroupConfiguration> builder = new XmlBindingBuilder<>();
			
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
			builder.addChildElementObjectBinding(FormFieldModifier.class, new FormFieldModifierBinding(builder));
			
			// known component special tags
			builder.addComponentGroupConfigurationClass(EnclosureConfiguration.class);
			builder.addComponentGroupConfigurationClass(IncludeBackendConfiguration.class);
			builder.addComponentGroupConfigurationClass(LazyLoadContainerConfiguration.class);
			builder.addComponentGroupConfigurationClass(LinkConfiguration.class);
			builder.addComponentGroupConfigurationClass(FieldPathFeedbackPanelConfiguration.class);
			builder.addComponentGroupConfigurationClass(FormConfiguration.class);
			builder.addComponentGroupConfigurationClass(PieChartConfiguration.class);
			builder.addComponentGroupConfigurationClass(SubmitButtonConfiguration.class);
			builder.addComponentGroupConfigurationClass(TabPanelConfiguration.class);
			builder.addComponentGroupConfigurationClass(TextFieldConfiguration.class);
			builder.addComponentGroupConfigurationClass(CheckboxConfiguration.class);
			builder.addComponentGroupConfigurationBinding("navbar", new NavigationBarBinding(builder));
			
			// Bootstrap-specific tags
			builder.addComponentGroupConfigurationClass(BootstrapFormConfiguration.class);
			builder.addComponentGroupConfigurationClass(BootstrapTextFieldConfiguration.class);
			
			binding = builder.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public MarkupContent<ComponentGroupConfiguration> parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		return binding.parse(reader);
	}

}
