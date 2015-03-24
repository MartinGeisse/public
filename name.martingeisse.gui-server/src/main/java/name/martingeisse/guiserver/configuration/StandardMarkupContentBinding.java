/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.configuration.content.ComponentGroupConfiguration;
import name.martingeisse.guiserver.configuration.content.MarkupContent;
import name.martingeisse.guiserver.configuration.content.MarkupContentParser;
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
import name.martingeisse.guiserver.configuration.content.basic.form.ValidatorParser;
import name.martingeisse.guiserver.configuration.content.bootstrap.NavigationBarConfiguration;
import name.martingeisse.guiserver.configuration.content.bootstrap.form.BootstrapFormConfiguration;
import name.martingeisse.guiserver.configuration.content.bootstrap.form.BootstrapTextFieldConfiguration;
import name.martingeisse.guiserver.xml.MyXmlStreamReader;
import name.martingeisse.guiserver.xml.builder.RecursiveContentParserBuilder;
import name.martingeisse.guiserver.xml.content.ContentParser;
import name.martingeisse.guiserver.xml.content.DelegatingContentParser;
import name.martingeisse.guiserver.xml.element.ElementParser;
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
	 * the parser
	 */
	private final ContentParser<MarkupContent<ComponentGroupConfiguration>> parser;

	/**
	 * Constructor.
	 */
	public StandardMarkupContentBinding() {
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

			// TODO couldn't specify the brand link parser class directly in the annotation since then we'd only have a
			// class, not an instance. The instance would need references to other objects from the parser builder. Thus,
			// specifying the parser class in the annotation isn't useful except maybe for value parsers.
			builder.addElementParser(NavigationBarConfiguration.BrandLinkWrapper.class, new BrandLinkParser(builder.getComponentElementParser()));

			// register known content parsers
			// ...

			// known component special tags
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
			builder.autoAddComponentElementParser(NavigationBarConfiguration.class);

			// Bootstrap-specific tags
			builder.autoAddComponentElementParser(BootstrapFormConfiguration.class);
			builder.autoAddComponentElementParser(BootstrapTextFieldConfiguration.class);

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

	/**
	 *
	 */
	public static class BrandLinkParser implements ElementParser<NavigationBarConfiguration.BrandLinkWrapper> {

		/**
		 * the componentElementParser
		 */
		private final ElementParser<ComponentGroupConfiguration> componentElementParser;

		/**
		 * Constructor.
		 * @param componentElementParser the parser for component elements
		 */
		public BrandLinkParser(ElementParser<ComponentGroupConfiguration> componentElementParser) {
			this.componentElementParser = componentElementParser;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xml.element.ElementParser#parse(name.martingeisse.guiserver.xml.MyXmlStreamReader)
		 */
		@Override
		public NavigationBarConfiguration.BrandLinkWrapper parse(MyXmlStreamReader reader) throws XMLStreamException {
			reader.next();
			reader.skipWhitespace();
			if (reader.getEventType() != XMLStreamConstants.START_ELEMENT) {
				throw new RuntimeException("brandLink content must be a single child element");
			}
			ComponentGroupConfiguration configuration = componentElementParser.parse(reader);
			reader.skipWhitespace();
			if (reader.getEventType() != XMLStreamConstants.END_ELEMENT) {
				throw new RuntimeException("brandLink content must be a single child element");
			}
			reader.next();
			return new NavigationBarConfiguration.BrandLinkWrapper(configuration);
		}

	}

}
