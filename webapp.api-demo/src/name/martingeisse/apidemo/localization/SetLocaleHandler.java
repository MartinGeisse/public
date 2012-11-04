/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo.localization;

import java.util.Locale;

import name.martingeisse.api.i18n.LocalizationUtil;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;

/**
 * Localization tests.
 */
public class SetLocaleHandler extends LocalizationHandlerBaseClass {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		String language = requestCycle.getParameters().getString("language", false);
		String country = requestCycle.getParameters().getString("country", false);
		Locale locale;
		if (language != null && country != null) {
			locale = new Locale(language, country);
		} else {
			locale = Locale.US;
		}
		LocalizationUtil.setLocale(locale);
	}
	
}
