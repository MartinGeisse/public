/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.api.i18n.formattable;

import java.util.Formattable;
import java.util.Formatter;

/**
 * Formattable for prices. The price values are integers that specify the
 * amount in the smallest unit for the respective currency.
 */
public final class PriceFormattable implements Formattable {

	/**
	 * the amount
	 */
	private final long amount;
	
	/**
	 * Constructor.
	 */
	public PriceFormattable(final long amount) {
		this.amount = amount;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Formattable#formatTo(java.util.Formatter, int, int, int)
	 */
	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision) {
		// TODO currencies
		formatter.format("%.2f", amount / 100.0);
	}

}
