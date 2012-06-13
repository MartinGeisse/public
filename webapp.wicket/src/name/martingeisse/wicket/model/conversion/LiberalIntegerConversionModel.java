/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.conversion;

import org.apache.wicket.model.IModel;

/**
 * An {@link AbstractLiberalConversionModel} implementation for type {@link Integer}.
 */
public class LiberalIntegerConversionModel extends AbstractLiberalConversionModel<Integer> {

	/**
	 * Constructor.
	 */
	public LiberalIntegerConversionModel() {
	}
	
	/**
	 * Constructor.
	 * @param wrappedModel the wrapped model
	 */
	public LiberalIntegerConversionModel(IModel<Integer> wrappedModel) {
		setWrappedModel(wrappedModel);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.model.AbstractLiberalConversionModel#convertFromString(java.lang.String)
	 */
	@Override
	protected Integer convertFromString(String value) {
		return new Integer(value);
	}

}
