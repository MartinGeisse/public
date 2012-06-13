/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.conversion;

import org.apache.wicket.model.IModel;

/**
 * This is the base class for all liberal conversion models.
 * 
 * A wrapped IModel connects to a model of type T. This class provides
 * conversion between type T and String to use the model in a context
 * where a string model is expected, bypassing Wicket's converter
 * system.
 * 
 * This class is liberal in the sense that if conversion from String
 * fails, the underlying model is simply left alone (not updated).
 * Conversion to String must succeed.
 * 
 * @param <T> the type of the wrapped model
 */
public abstract class AbstractLiberalConversionModel<T> implements IModel<String> {

	/**
	 * the wrappedModel
	 */
	private IModel<T> wrappedModel;
	
	/**
	 * Constructor.
	 */
	public AbstractLiberalConversionModel() {
	}

	/**
	 * Getter method for the wrappedModel.
	 * @return the wrappedModel
	 */
	public final IModel<T> getWrappedModel() {
		return wrappedModel;
	}

	/**
	 * Setter method for the wrappedModel.
	 * @param wrappedModel the wrappedModel to set
	 */
	public final void setWrappedModel(IModel<T> wrappedModel) {
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
	public final String getObject() {
		return convertToString(wrappedModel.getObject());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
	 */
	@Override
	public final void setObject(String value) {
		T convertedValue;
		if (value == null) {
			convertedValue = null;
		} else {
			try {
				convertedValue = convertFromString(value);
			} catch (IllegalArgumentException e) {
				return;
			}
		}
		wrappedModel.setObject(convertedValue);
	}

	/**
	 * Converts the specified textual representation of a domain value to the
	 * actual domain value. This method should throw an {@link IllegalArgumentException}
	 * if conversion fails.
	 * @param value the value to convert
	 * @return returns the converted value
	 */
	protected abstract T convertFromString(String value) throws IllegalArgumentException;
	
	/**
	 * Converts the specified domain value to a string. The default implementation
	 * returns value.toString().
	 * @param value the value to convert
	 * @return returns the converted value
	 */
	protected String convertToString(T value) {
		return (value == null) ? null : value.toString();
	}

}
