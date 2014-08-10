/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.servlet;

import name.martingeisse.api.handler.IApiRequestHandler;
import name.martingeisse.api.i18n.LocalizationConfiguration;

/**
 * Contains global settings.
 */
public final class ApiConfiguration {

	/**
	 * the configuration
	 */
	private static ApiConfiguration instance;

	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public static ApiConfiguration getInstance() {
		return instance;
	}
	
	/**
	 * Setter method for the instance.
	 * @param instance the instance to set
	 */
	public static void setInstance(ApiConfiguration instance) {
		ApiConfiguration.instance = instance;
	}
	
	/**
	 * the masterRequestHandler
	 */
	private IApiRequestHandler masterRequestHandler;
	
	/**
	 * the localizationConfiguration
	 */
	private final LocalizationConfiguration localizationConfiguration = new LocalizationConfiguration();
	
	/**
	 * Getter method for the masterRequestHandler.
	 * @return the masterRequestHandler
	 */
	public IApiRequestHandler getMasterRequestHandler() {
		return masterRequestHandler;
	}
	
	/**
	 * Setter method for the masterRequestHandler.
	 * @param masterRequestHandler the masterRequestHandler to set
	 */
	public void setMasterRequestHandler(IApiRequestHandler masterRequestHandler) {
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
