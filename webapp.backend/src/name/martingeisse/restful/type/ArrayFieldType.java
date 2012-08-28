/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.restful.type;


/**
 * Represents arrays of uniform values. This type specifies the element
 * type (using another {@link IFieldType}), but not the size of the
 * array.
 */
public class ArrayFieldType implements IFieldType {

	/**
	 * the elementType
	 */
	private final IFieldType elementType;
	
	/**
	 * Constructor.
	 * @param elementType the element type
	 */
	public ArrayFieldType(final IFieldType elementType) {
		this.elementType = elementType;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#normalize(java.lang.Object)
	 */
	@Override
	public Object[] normalize(Object value) {
		if (value instanceof Object[]) {
			return createCloneWithNormalizedElements((Object[])value);
		}
		throw new InvalidValueForTypeException(value, this);
	}

	/**
	 * @param elements
	 */
	private Object[] createCloneWithNormalizedElements(Object[] elements) {
		elements = elements.clone();
		normalizeElementsInPlace(elements);
		return elements;
	}

	/**
	 * @param elements
	 */
	private void normalizeElementsInPlace(Object[] elements) {
		for (int i=0; i<elements.length; i++) {
			elements[i] = elementType.normalize(elements[i]);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#convertToText(java.lang.Object)
	 */
	@Override
	public String convertToText(Object value) {
		Object[] elements = normalize(value);
		boolean first = true;
		StringBuilder builder = new StringBuilder();
		for (Object element : elements) {
			if (first) {
				first = false;
			} else {
				builder.append(',');
			}
			builder.append(elementType.convertToText(element));
		}
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.restful.type.IFieldType#convertToJson(java.lang.Object)
	 */
	@Override
	public String convertToJson(Object value) {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		boolean first = true;
		for (Object element : normalize(value)) {
			if (first) {
				first = false;
			} else {
				builder.append(", ");
			}
			builder.append(elementType.convertToJson(element));
		}
		builder.append(']');
		return builder.toString();
	}

}
