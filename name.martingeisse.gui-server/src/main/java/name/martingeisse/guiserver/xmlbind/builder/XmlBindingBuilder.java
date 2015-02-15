/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xmlbind.builder;

import name.martingeisse.guiserver.xmlbind.content.MarkupContentBinding;
import name.martingeisse.guiserver.xmlbind.element.ElementNameSelectedObjectBinding;

/**
 * This class builds the databinding for the XML format.
 *
 * @param <C> the type of components used in markup content
 */
public final class XmlBindingBuilder<C> {

	/**
	 * the elementComponentBinding
	 */
	private final ElementNameSelectedObjectBinding<C> elementComponentBinding;

	/**
	 * the attributeTextValueBindingRegistry
	 */
	private final AttributeTextValueBindingRegistry attributeTextValueBindingRegistry;

	/**
	 * the childElementObjectBindingRegistry
	 */
	private final ChildElementObjectBindingRegistry childElementObjectBindingRegistry;

	/**
	 * Constructor.
	 * @param elementComponentBinding the element-to-component binding
	 * @param attributeTextValueBindingRegistry the text-to-value binding registry for attributes
	 * @param childElementObjectBindingRegistry the element-to-object binding registry for child elements
	 */
	public XmlBindingBuilder(ElementNameSelectedObjectBinding<C> elementComponentBinding, AttributeTextValueBindingRegistry attributeTextValueBindingRegistry,
		ChildElementObjectBindingRegistry childElementObjectBindingRegistry) {

		this.elementComponentBinding = elementComponentBinding;
		this.attributeTextValueBindingRegistry = attributeTextValueBindingRegistry;
		this.childElementObjectBindingRegistry = childElementObjectBindingRegistry;
	}

	/**
	 * Builds the databinding for the XML format.
	 * 
	 * @return the databinding
	 */
	public MarkupContentBinding<C> build() {
		// TODO
		return null;
	}

}
