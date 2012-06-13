/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.conversion;

import java.math.BigDecimal;

import org.apache.wicket.model.IModel;

/**
 * An {@link AbstractLiberalConversionModel} implementation for type {@link BigDecimal}.
 */
public class LiberalBigDecimalConversionModel extends AbstractLiberalConversionModel<BigDecimal> {

	/**
	 * Constructor.
	 */
	public LiberalBigDecimalConversionModel() {
	}
	
	/**
	 * Constructor.
	 * @param wrappedModel the wrapped model
	 */
	public LiberalBigDecimalConversionModel(IModel<BigDecimal> wrappedModel) {
		setWrappedModel(wrappedModel);
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.model.AbstractLiberalConversionModel#convertFromString(java.lang.String)
	 */
	@Override
	protected BigDecimal convertFromString(String value) {
		return new BigDecimal(value.replace(',', '.'));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.terra.wicket.model.AbstractLiberalConversionModel#convertToString(java.lang.Object)
	 */
	@Override
	protected String convertToString(BigDecimal value) {
		return (value == null) ? null : value.toString().replace('.', ',');
	}
	
}
