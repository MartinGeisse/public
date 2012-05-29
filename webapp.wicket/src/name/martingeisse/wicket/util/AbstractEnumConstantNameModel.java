/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.model.IModel;

/**
 * This model wraps a model of an enum type and gets the
 * enum constant name / sets the enum constant by name.
 * 
 * Trying to set a string as the model value that does not
 * correspond to any enum constant results in setting null
 * as the value for the underlying model.
 * 
 * Subclasses must implement a wrapper around the valueOf()
 * method of the enum class. This avoids storing the class
 * object of the enum class to reduce session size.
 * 
 * @param <E> the enum type
 */
public abstract class AbstractEnumConstantNameModel<E extends Enum<E>> implements IModel<String> {

	/**
	 * the wrappedModel
	 */
	private IModel<E> wrappedModel;
	
	/**
	 * Constructor.
	 * @param wrappedModel the wrapped model
	 */
	public AbstractEnumConstantNameModel(IModel<E> wrappedModel) {
		this.wrappedModel = wrappedModel;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
		wrappedModel.detach();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#getObject()
	 */
	@Override
	public String getObject() {
		E value = wrappedModel.getObject();
		return (value == null ? null : value.name());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	@Override
	public void setObject(String s) {
		E value;
		try {
			value = valueOf(s);
		} catch (Exception e) {
			value = null;
		}
		wrappedModel.setObject(value);
	}

	/**
	 * Returns the enum constant with the specified name. Throws a {@link NullPointerException}
	 * if s is null. Throws an {@link IllegalArgumentException} if the argument does not
	 * correspond to any enum constant.
	 * 
	 * This method should simply call E.valueOf(s), which already behaves exactly as expected.
	 * 
	 * @param s the constant name
	 * @return the enum constant
	 */
	protected abstract E valueOf(String s);
	
}
