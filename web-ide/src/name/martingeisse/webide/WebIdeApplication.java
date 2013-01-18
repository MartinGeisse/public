/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide;

import name.martingeisse.common.util.TemporaryFolder;
import name.martingeisse.webide.util.MyHeaderResponseDecorator;
import name.martingeisse.webide.util.MyResourceReferenceRegistry;
import name.martingeisse.webide.workbench.WorkbenchPage;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ResourceReferenceRegistry;

/**
 * The wicket application.
 */
public class WebIdeApplication extends WebApplication {

	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		super.init();
		TemporaryFolder.initialize("web-ide");
		setHeaderResponseDecorator(new MyHeaderResponseDecorator());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#newResourceReferenceRegistry()
	 */
	@Override
	protected ResourceReferenceRegistry newResourceReferenceRegistry() {
		return new MyResourceReferenceRegistry();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return WorkbenchPage.class;
		// return PushTestPage.class;
	}

}
