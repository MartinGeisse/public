/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.api.i18n.formattable;

import java.io.IOException;
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
	 * the currencySymbol
	 */
	private final String currencySymbol;
	
	/**
	 * Constructor.
	 * @param amount the amount
	 * @param currencySymbol the currency symbol
	 */
	public PriceFormattable(final long amount, String currencySymbol) {
		this.amount = amount;
		this.currencySymbol = currencySymbol;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Formattable#formatTo(java.util.Formatter, int, int, int)
	 */
	@Override
	public void formatTo(Formatter formatter, int flags, int width, int precision) {
		try {
			formatter.out().append(currencySymbol).append(' ');
			formatter.format("%.2f", amount / 100.0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
