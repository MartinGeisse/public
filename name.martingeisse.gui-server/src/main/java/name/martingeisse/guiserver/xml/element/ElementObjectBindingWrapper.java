/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.element;

import javax.xml.stream.XMLStreamException;

import name.martingeisse.guiserver.xml.DatabindingXmlStreamReader;

/**
 * Delegates parsing to another {@link ElementObjectBinding}, but allows to wrap/transform
 * the result.
 * 
 * @param <ORIG> the original type
 * @param <WRAP> the wrapped type
 */
public abstract class ElementObjectBindingWrapper<ORIG, WRAP> implements ElementObjectBinding<WRAP> {

	/**
	 * the originalBinding
	 */
	private final ElementObjectBinding<? extends ORIG> originalBinding;

	/**
	 * Constructor.
	 * @param originalBinding the original binding
	 */
	public ElementObjectBindingWrapper(ElementObjectBinding<? extends ORIG> originalBinding) {
		this.originalBinding = originalBinding;
	}

	/**
	 * Wraps the result.
	 * 
	 * @param original the original result
	 * @return the wrapped result
	 */
	protected abstract WRAP wrapResult(ORIG original);

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.element.ElementObjectBinding#parse(name.martingeisse.guiserver.xml.DatabindingXmlStreamReader)
	 */
	@Override
	public final WRAP parse(DatabindingXmlStreamReader reader) throws XMLStreamException {
		return wrapResult(originalBinding.parse(reader));
	}

}
