/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.servlet;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.i18n.LocalizationConfiguration;

/**
 * Contains global settings.
 */
public final class ApiConfiguration {

	/**
	 * the masterRequestHandler
	 */
	private IRequestHandler masterRequestHandler;
	
	/**
	 * the localizationConfiguration
	 */
	private final LocalizationConfiguration localizationConfiguration = new LocalizationConfiguration();

	/**
	 * Getter method for the masterRequestHandler.
	 * @return the masterRequestHandler
	 */
	public IRequestHandler getMasterRequestHandler() {
		return masterRequestHandler;
	}
	
	/**
	 * Setter method for the masterRequestHandler.
	 * @param masterRequestHandler the masterRequestHandler to set
	 */
	public void setMasterRequestHandler(IRequestHandler masterRequestHandler) {
		this.masterRequestHandler = masterRequestHandler;
	}
	
	/**
	 * Getter method for the localizationConfiguration.
	 * @return the localizationConfiguration
	 */
	public LocalizationConfiguration getLocalizationConfiguration() {
		return localizationConfiguration;
	}
	
}
