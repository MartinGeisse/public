/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.wicket;

import org.apache.wicket.protocol.http.WebApplication;

/**
 * Implementations are invoked when the wicket {@link WebApplication}
 * is being initialized.
 */
public interface IWebApplicationInitializationContributor {

	/**
	 * @param webApplication the web application
	 */
	public void onInitializeWebApplication(WebApplication webApplication);

}
