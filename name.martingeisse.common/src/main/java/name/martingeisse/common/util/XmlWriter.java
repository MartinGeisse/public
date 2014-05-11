/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.io.PrintWriter;
import java.io.Writer;

/**
 * This is a utility class to generate XML syntax.
 */
public class XmlWriter {

	/**
	 * the outputWriter
	 */
	private PrintWriter outputWriter;
	
	/**
	 * Constructor.
	 * @param outputWriter the output writer used to actually write something
	 */
	public XmlWriter(PrintWriter outputWriter) {
		this.outputWriter = outputWriter;
	}
	
	/**
	 * Constructor.
	 * @param outputWriterToWrap the output writer used to actually write something.
	 * Will be wrapped in a {@link PrintWriter}.
	 */
	public XmlWriter(Writer outputWriterToWrap) {
		this(new PrintWriter(outputWriterToWrap));
	}
	
	/**
	 * This method allows subclasses to apply final transformations to all XML written. This is,
	 * for example, useful in cases where the XML is embedded in another language and certain
	 * characters must be escaped due to that. The default implementation returns the argument.
	 * 
	 * This method is called exactly on the XML generated, in the order generated. However, the
	 * generated XML may be split into arbitrarily sized chunks (which are fed into this method
	 * in the correct order). This method may also be called with empty strings.
	 * 
	 * @param s the string to transform
	 * @return the transformed string
	 */
	protected String applyFinalOutputTransformation(String s) {
		return s;
	}
	
	/**
	 * Writes an XML declaration for utf-8 encoding. Note that this method does not in any way
	 * ensure that utf-8 encoding is actually used, only that it is declared in the XML declaration.
	 */
	public final void writeXmlDeclaration() {
		writeXmlDeclaration("utf-8");
	}
	
	/**
	 * Writes an XML declaration for the specified encoding. Note that this method does not in any way
	 * ensure that the encoding is actually used, only that it is declared in the XML declaration.
	 * @param encoding the encoding the declare
	 */
	public final void writeXmlDeclaration(String encoding) {
		writeMarkup("<?xml version=\"1.0\" encoding=\"" + encoding + "\" ?>");
	}
	
	/**
	 * Writes a piece of text without any treatment. Markup characters in the text will
	 * be transferred unaltered to the output, that is, they become markup characters
	 * in the output.
	 * @param markup the piece of markup to write
	 */
	public final void writeMarkup(String markup) {
		outputWriter.write(markup);
	}
	
	/**
	 * Writes a piece of non-markup text. Any characters with a syntactic meaning to XML
	 * will be escaped. 
	 * @param text the text to write
	 */
	public final void writeText(String text) {
		writeMarkup(escapeMarkupCharacters(text));
	}
	
	/**
	 * Returns the argument string with any markup characters escaped.
	 * @param s the input string
	 * @return the argument with escaped markup characters.
	 */
	public static String escapeMarkupCharacters(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;");
	}
	
	/**
	 * Writes the beginning sequence of an opening tag with the specified tag name.
	 * This method is intended for opening tags that include attributes. For opening
	 * tags without attributes, use writeOpeningTag(elementName) or writeEmptyElement(elementName).
	 * @param elementName the name of the element
	 */
	public final void beginOpeningTag(String elementName) {
		writeMarkup("<" + elementName);
	}
	
	/**
	 * Writes an attribute assignment. This method must only be used between beginOpeningTag()
	 * and finishOpeningTag().
	 * @param name the attribute name
	 * @param value the attribute value
	 */
	public final void writeAttribute(String name, String value) {
		writeMarkup(" " + name + "=\"");
		writeText(value);
		writeMarkup("\"");
	}

	/**
	 * Writes an XML namespace assignment attribute. This method must only be used between beginOpeningTag()
	 * and finishOpeningTag().
	 * @param namespacePrefix the namespace prefix to bind
	 * @param namespaceUrl the URL to bind the prefix to
	 */
	public final void writeNamespaceAttribute(String namespacePrefix, String namespaceUrl) {
		writeAttribute("xmlns:" + namespacePrefix, namespaceUrl);
	}
	
	/**
	 * Writes a namespace attribtue that binds the "xsi" prefix to the XSI namespace. This method must only be used between beginOpeningTag()
	 * and finishOpeningTag().
	 */
	public final void writeXsiNamespaceAttribute() {
		writeNamespaceAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	}
	
	/**
	 * Writes an xsi:nil attribute. This method must only be used between beginOpeningTag()
	 * and finishOpeningTag().
	 */
	public final void writeXsiNilAttribute() {
		writeAttribute("xsi:nil", "true");
	}
	
	/**
	 * Finishes an opening tag. This method must be used only after beginOpeningTag() and zero or
	 * more calls to writeAttribute().
	 */
	public final void finishOpeningTag() {
		writeMarkup(">");
	}
	
	/**
	 * Finishes an opening tag using the empty-element shortcut notation. The element must be considered
	 * closed after calling this method.
	 */
	public final void finishOpeningTagAsEmpty() {
		writeMarkup(" />");
	}

	/**
	 * Writes a finished opening tag for an element with the specified name without attributes.
	 * @param name the element name
	 */
	public final void writeOpeningTag(String name) {
		writeMarkup("<" + name + ">");
	}

	/**
	 * Writes a closing tag for an element with the specified name.
	 * @param name the element name
	 */
	public final void writeClosingTag(String name) {
		writeMarkup("</" + name + ">");
	}

	/**
	 * Writes a tag for an element with the specified name without attributes and using the empty-element shortcut
	 * notation. The element must be considered closed after calling this method.
	 * @param name the element name
	 */
	public final void writeEmptyElement(String name) {
		writeMarkup("<" + name + " />");
	}

	/**
	 * Writes a tag for an element with the specified name with a single xsi:nil attribute and using the empty-element shortcut
	 * notation. The element must be considered closed after calling this method.
	 * @param name the element name
	 */
	public final void writeNilElement(String name) {
		beginOpeningTag(name);
		writeXsiNilAttribute();
		finishOpeningTagAsEmpty();
	}

	/**
	 * Writes an element with no attributes whose text content is the specified value. If the value is
	 * null, this method throws an {@link IllegalArgumentException}.
	 * @param name the element name
	 * @param value the value of the element (must not be null)
	 */
	public final void writeValueElement(String name, String value) {
		if (value == null) {
			throw new IllegalArgumentException("value argument is null");
		} else {
			writeOpeningTag(name);
			writeText(value);
			writeClosingTag(name);
		}
	}
	
	/**
	 * Writes an element with no attributes whose text content is the specified value. If the value is
	 * null, leaves the element empty and attaches an xsi:nil attribute.
	 * @param name the element name
	 * @param value the value of the element, or null to generate a nil element
	 */
	public final void writeValueElementOrNil(String name, String value) {
		if (value == null) {
			writeNilElement(name);
		} else {
			writeOpeningTag(name);
			writeText(value);
			writeClosingTag(name);
		}
	}

	/**
	 * Writes an element with no attributes whose text content is the specified value. If the value is
	 * null, nothing is written at all.
	 * @param name the element name
	 * @param value the value of the element, or null to omit the element
	 */
	public final void writeValueElementOrNothing(String name, String value) {
		if (value != null) {
			writeOpeningTag(name);
			writeText(value);
			writeClosingTag(name);
		}
	}

}
