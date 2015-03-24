/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.content;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.MyXmlStreamReader;

/**
 * This implementation of {@link ContentParser} just delegates to
 * another implementation, but allows to replace that implementation.
 */
public final class DelegatingContentParser<T> implements ContentParser<T> {

	/**
	 * the delegate
	 */
	private ContentParser<T> delegate;

	/**
	 * Getter method for the delegate.
	 * @return the delegate
	 */
	public ContentParser<T> getDelegate() {
		return delegate;
	}

	/**
	 * Setter method for the delegate.
	 * @param delegate the delegate to set
	 */
	public void setDelegate(ContentParser<T> delegate) {
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public T parse(MyXmlStreamReader reader) throws XMLStreamException {
		return delegate.parse(reader);
	}

}
