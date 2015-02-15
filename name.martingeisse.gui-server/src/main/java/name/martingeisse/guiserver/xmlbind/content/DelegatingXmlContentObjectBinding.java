/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.content;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader;

/**
 * This implementation of {@link XmlContentObjectBinding} just delegates to
 * another implementation, but allows to replace that implementation. This is
 * needed since we cannot create a structure of all-immutable binding objects
 * that contains a cycle.
 */
public final class DelegatingXmlContentObjectBinding<T> implements XmlContentObjectBinding<T> {

	/**
	 * the delegate
	 */
	private XmlContentObjectBinding<T> delegate;

	/**
	 * Getter method for the delegate.
	 * @return the delegate
	 */
	public XmlContentObjectBinding<T> getDelegate() {
		return delegate;
	}

	/**
	 * Setter method for the delegate.
	 * @param delegate the delegate to set
	 */
	public void setDelegate(XmlContentObjectBinding<T> delegate) {
		this.delegate = delegate;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xmlbind.content.XmlContentObjectBinding#parse(name.martingeisse.guiserver.xmlbind.DatabindingXmlStreamReader)
	 */
	@Override
	public T parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		return delegate.parse(reader);
	}

}
