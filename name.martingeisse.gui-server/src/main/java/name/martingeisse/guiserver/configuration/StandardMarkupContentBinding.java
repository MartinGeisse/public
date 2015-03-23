/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

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
import name.martingeisse.guiserver.configuration.content.basic.form.SubmitButtonConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.TextFieldConfiguration;
import name.martingeisse.guiserver.configuration.content.basic.form.VaildatorParser;
import name.martingeisse.guiserver.configuration.content.bootstrap.form.BootstrapFormConfiguration;
import name.martingeisse.guiserver.configuration.content.bootstrap.form.BootstrapTextFieldConfiguration;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.builder.XmlParserBuilder;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.result.MarkupContent;
import name.martingeisse.guiserver.xml.value.BooleanValueParser;
import name.martingeisse.guiserver.xml.value.IntegerValueParser;
import name.martingeisse.guiserver.xml.value.StringValueParser;

import org.apache.wicket.validation.IValidator;

/**
 * For now, the markup content binding is fixed, and expressed as
 * this singleton class.
 */
public final class StandardMarkupContentBinding implements ContentParser<MarkupContent<ComponentGroupConfiguration>> {

	/**
	 * the INSTANCE
	 */
	public static final StandardMarkupContentBinding INSTANCE = new StandardMarkupContentBinding();

	/**
	 * the binding
	 */
	private final ContentParser<MarkupContent<ComponentGroupConfiguration>> binding;

	/**
	 * Constructor.
	 */
	public StandardMarkupContentBinding() {
		try {
			XmlParserBuilder<ComponentGroupConfiguration> builder = new XmlParserBuilder<>();
			
			// register known value parsers
			builder.addValueParser(String.class, StringValueParser.INSTANCE);
			builder.addValueParser(Boolean.class, BooleanValueParser.INSTANCE);
			builder.addValueParser(Boolean.TYPE, BooleanValueParser.INSTANCE);
			builder.addValueParser(Integer.class, IntegerValueParser.INSTANCE);
			builder.addValueParser(Integer.TYPE, IntegerValueParser.INSTANCE);
			
			// register known element parsers
			@SuppressWarnings("unchecked")
			Class<IValidator<?>> validatorClass = (Class<IValidator<?>>)(Class<?>)IValidator.class;
			builder.addElementParser(validatorClass, new VaildatorParser(builder));
			
			// register known content parsers
			@SuppressWarnings("unchecked")
			Class<MarkupContent<ComponentGroupConfiguration>> markupContentClass = (Class<MarkupContent<ComponentGroupConfiguration>>)(Class<?>)MarkupContent.class;
			builder.addContentParser(markupContentClass, builder.getRecursiveMarkupParser());

			
			
			
			
			// known child object classes
//			{
//				Constructor<TabPanelConfiguration.TabEntry> constructor = TabPanelConfiguration.TabEntry.class.getConstructor(String.class, String.class, MarkupContent.class);
//				builder.addChildElementObjectBinding(TabPanelConfiguration.TabEntry.class, new ClassInstanceElementParser<>(constructor, attributeBindings, builder.getRecursiveMarkupBinding()));
//			}
			
			// known component special tags
			builder.autoAddComponentGroupConfigurationClass(EnclosureConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(IncludeBackendConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(LazyLoadContainerConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(LinkConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(FieldPathFeedbackPanelConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(FormConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(PieChartConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(SubmitButtonConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(TabPanelConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(TextFieldConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(CheckboxConfiguration.class);
//			builder.addComponentGroupConfigurationBinding("navbar", new NavigationBarBinding(builder));
			
			// Bootstrap-specific tags
			builder.autoAddComponentGroupConfigurationClass(BootstrapFormConfiguration.class);
			builder.autoAddComponentGroupConfigurationClass(BootstrapTextFieldConfiguration.class);
			
			binding = builder.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public MarkupContent<ComponentGroupConfiguration> parse(MyXmlStreamReader reader) throws XMLStreamException {
		return binding.parse(reader);
	}

}
