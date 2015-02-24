/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.xml.content;

import java.util.List;

import name.martingeisse.guiserver.xml.element.ElementObjectBinding;

/**
 * Parses (potentially) multiple child elements by mapping each child to an object,
 * and returns those objects in a list.
 *
 * @param <T> the child object type
 */
public final class MultiChildObjectBinding<T> extends AbstractMultiChildObjectBinding<T, List<T>> {

	/**
	 * Constructor.
	 * @param optional whether the child object is optional
	 * @param childObjectElementNameFilter the name filter for child object elements
	 * @param childElementObjectBinding the element-to-object binding for the child object 
	 */
	public MultiChildObjectBinding(boolean optional, String[] childObjectElementNameFilter, ElementObjectBinding<T> childElementObjectBinding) {
		super(optional, childObjectElementNameFilter, childElementObjectBinding);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.xml.content.AbstractMultiChildObjectBinding#mapChildrenToResult(java.util.List)
	 */
	@Override
	protected List<T> mapChildrenToResult(List<T> children) {
		return children;
	}

}
