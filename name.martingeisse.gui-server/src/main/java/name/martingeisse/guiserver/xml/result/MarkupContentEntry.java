/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.result;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import name.martingeisse.guiserver.xml.ConfigurationAssemblerAcceptor;

/**
 * Used to represent the actual contents of a {@link MarkupContent} object.
 *
 * @param <C> the component type
 */
public abstract class MarkupContentEntry<C extends ConfigurationAssemblerAcceptor<C>> {

	/**
	 * Assembles the configuration.
	 * 
	 * @param assembler the configuration assembler
	 * @throws XMLStreamException on XML stream processing errors
	 */
	public abstract void assemble(ConfigurationAssembler<C> assembler) throws XMLStreamException;

	/**
	 * Represents an attribute for an element.
	 */
	public static final class Attribute {

		/**
		 * the localName
		 */
		private final String localName;

		/**
		 * the value
		 */
		private final String value;

		/**
		 * Constructor.
		 * @param localName
		 * @param value
		 */
		public Attribute(String localName, String value) {
			this.localName = localName;
			this.value = value;
		}

		/**
		 * Writes this attribute to the specified writer.
		 * @param writer the writer
		 * @throws XMLStreamException on XML stream processing errors
		 */
		public void writeTo(XMLStreamWriter writer) throws XMLStreamException {
			writer.writeAttribute(localName, value);
		}

	}

	/**
	 * Represents an opening tag of a raw (non-special) element.
	 *
	 * @param <C> the component type
	 */
	public static final class RawOpeningTag<C extends ConfigurationAssemblerAcceptor<C>> extends MarkupContentEntry<C> {

		/**
		 * the localName
		 */
		private final String localName;

		/**
		 * the attributes
		 */
		private final Attribute[] attributes;

		/**
		 * Constructor.
		 * @param localName the local element name
		 * @param attributes the attributes
		 */
		public RawOpeningTag(String localName, Attribute[] attributes) {
			this.localName = localName;
			this.attributes = attributes;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xmlbind.result.MarkupContentEntry#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
		 */
		@Override
		public void assemble(ConfigurationAssembler<C> assembler) throws XMLStreamException {
			assembler.getMarkupWriter().writeStartElement(localName);
			for (Attribute attribute : attributes) {
				attribute.writeTo(assembler.getMarkupWriter());
			}
		}
	}

	/**
	 * Represents a closing tag of a raw (non-special) element.
	 *
	 * @param <C> the component type
	 */
	public static final class RawClosingTag<C extends ConfigurationAssemblerAcceptor<C>> extends MarkupContentEntry<C> {

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xmlbind.result.MarkupContentEntry#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
		 */
		@Override
		public void assemble(ConfigurationAssembler<C> assembler) throws XMLStreamException {
			assembler.getMarkupWriter().writeEndElement();
		}

	}

	/**
	 * Represents character content.
	 *
	 * @param <C> the component type
	 */
	public static final class Characters<C extends ConfigurationAssemblerAcceptor<C>> extends MarkupContentEntry<C> {

		/**
		 * the text
		 */
		private final String text;

		/**
		 * Constructor.
		 * @param text the text
		 */
		public Characters(String text) {
			this.text = text;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xmlbind.result.MarkupContentEntry#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
		 */
		@Override
		public void assemble(ConfigurationAssembler<C> assembler) throws XMLStreamException {
			assembler.getMarkupWriter().writeCharacters(text);
		}

	}

	/**
	 * Represents a component configuration.
	 *
	 * @param <C> the component type
	 */
	public static final class Component<C extends ConfigurationAssemblerAcceptor<C>> extends MarkupContentEntry<C> {

		/**
		 * the configuration
		 */
		private final C configuration;

		/**
		 * Constructor.
		 * @param configuration the component configuration
		 */
		public Component(C configuration) {
			this.configuration = configuration;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.guiserver.xmlbind.result.MarkupContentEntry#assemble(name.martingeisse.guiserver.xmlbind.result.ConfigurationAssembler)
		 */
		@Override
		public void assemble(ConfigurationAssembler<C> assembler) throws XMLStreamException {
			configuration.assemble(assembler);
		}

	}

}
