/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.template.basic.EchoTextConfiguration;
import name.martingeisse.guiserver.template.basic.EnclosureConfiguration;
import name.martingeisse.guiserver.template.basic.IfConfiguration;
import name.martingeisse.guiserver.template.basic.IncludeBackendConfiguration;
import name.martingeisse.guiserver.template.basic.LazyLoadContainerConfiguration;
import name.martingeisse.guiserver.template.basic.LinkConfiguration;
import name.martingeisse.guiserver.template.basic.ListViewConfiguration;
import name.martingeisse.guiserver.template.basic.PanelReferenceConfiguration;
import name.martingeisse.guiserver.template.basic.PieChartConfiguration;
import name.martingeisse.guiserver.template.basic.TabPanelConfiguration;
import name.martingeisse.guiserver.template.basic.form.CheckboxConfiguration;
import name.martingeisse.guiserver.template.basic.form.FieldPathFeedbackPanelConfiguration;
import name.martingeisse.guiserver.template.basic.form.FormConfiguration;
import name.martingeisse.guiserver.template.basic.form.SubmitButtonConfiguration;
import name.martingeisse.guiserver.template.basic.form.TextFieldConfiguration;
import name.martingeisse.guiserver.template.basic.form.ValidatorParser;
import name.martingeisse.guiserver.template.bootstrap.form.BootstrapFormConfiguration;
import name.martingeisse.guiserver.template.bootstrap.form.BootstrapTextFieldConfiguration;
import name.martingeisse.guiserver.template.demo.ComponentDemoConfiguration;
import name.martingeisse.guiserver.template.demo.MarkupContentAndSourceCode;
import name.martingeisse.guiserver.template.demo.MarkupContentAndSourceCodeParser;
import name.martingeisse.guiserver.template.model.BackendJsonModelConfiguration;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.content.DelegatingContentParser;
import name.martingeisse.guiserver.xml.value.BooleanValueParser;
import name.martingeisse.guiserver.xml.value.IntegerValueParser;
import name.martingeisse.guiserver.xml.value.StringValueParser;

import org.apache.wicket.validation.IValidator;

/**
 * Parses the templates for user-defined pages, panels, and so on.
 */
public final class TemplateParser implements ContentParser<MarkupContent<ComponentGroupConfiguration>> {

	/**
	 * the INSTANCE
	 */
	public static final TemplateParser INSTANCE = new TemplateParser();

	/**
	 * the parser
	 */
	private final ContentParser<MarkupContent<ComponentGroupConfiguration>> parser;

	/**
	 * Constructor.
	 */
	public TemplateParser() {
		try {
			RecursiveContentParserBuilder<ComponentGroupConfiguration> builder = new RecursiveContentParserBuilder<>();
			
			// this parser will be used to close the recursive loop
			DelegatingContentParser<MarkupContent<ComponentGroupConfiguration>> recursiveMarkupParser = new DelegatingContentParser<>();
			
			// register the builder's own parsers
			@SuppressWarnings("unchecked")
			Class<MarkupContent<ComponentGroupConfiguration>> markupContentClass = (Class<MarkupContent<ComponentGroupConfiguration>>)(Class<?>)MarkupContent.class;
			builder.addContentParser(markupContentClass, recursiveMarkupParser);
			builder.addElementParser(ComponentGroupConfiguration.class, builder.getComponentElementParser());

			// register known value parsers
			builder.addValueParser(String.class, StringValueParser.INSTANCE);
			builder.addValueParser(Boolean.class, BooleanValueParser.INSTANCE);
			builder.addValueParser(Boolean.TYPE, BooleanValueParser.INSTANCE);
			builder.addValueParser(Integer.class, IntegerValueParser.INSTANCE);
			builder.addValueParser(Integer.TYPE, IntegerValueParser.INSTANCE);

			// register known element parsers
			@SuppressWarnings("unchecked")
			Class<IValidator<?>> validatorClass = (Class<IValidator<?>>)(Class<?>)IValidator.class;
			builder.addElementParser(validatorClass, new ValidatorParser(builder));

			// register known content parsers
			builder.addContentParser(MarkupContentAndSourceCode.class, new MarkupContentAndSourceCodeParser(recursiveMarkupParser));

			// known component special tags
			builder.autoAddComponentElementParser(EchoTextConfiguration.class);
			builder.autoAddComponentElementParser(EnclosureConfiguration.class);
			builder.autoAddComponentElementParser(IncludeBackendConfiguration.class);
			builder.autoAddComponentElementParser(LazyLoadContainerConfiguration.class);
			builder.autoAddComponentElementParser(LinkConfiguration.class);
			builder.autoAddComponentElementParser(FieldPathFeedbackPanelConfiguration.class);
			builder.autoAddComponentElementParser(FormConfiguration.class);
			builder.autoAddComponentElementParser(PieChartConfiguration.class);
			builder.autoAddComponentElementParser(SubmitButtonConfiguration.class);
			builder.autoAddComponentElementParser(TabPanelConfiguration.class);
			builder.autoAddComponentElementParser(TextFieldConfiguration.class);
			builder.autoAddComponentElementParser(CheckboxConfiguration.class);
			builder.autoAddComponentElementParser(PanelReferenceConfiguration.class);
			builder.autoAddComponentElementParser(ComponentDemoConfiguration.class);
			builder.autoAddComponentElementParser(ListViewConfiguration.class);
			builder.autoAddComponentElementParser(IfConfiguration.class);

			// Bootstrap-specific tags
			builder.autoAddComponentElementParser(BootstrapFormConfiguration.class);
			builder.autoAddComponentElementParser(BootstrapTextFieldConfiguration.class);
			
			// models
			builder.autoAddComponentElementParser(BackendJsonModelConfiguration.class);

			// close the parsing loop
			recursiveMarkupParser.setDelegate(new MarkupContentParser<>(builder.getComponentElementParser()));
			parser = recursiveMarkupParser;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public MarkupContent<ComponentGroupConfiguration> parse(MyXmlStreamReader reader) throws XMLStreamException {
		return parser.parse(reader);
	}

}
