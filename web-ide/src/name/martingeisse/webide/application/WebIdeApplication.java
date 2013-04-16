/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.application;

import name.martingeisse.common.util.TemporaryFolder;
import name.martingeisse.webide.features.simvm.editor.SimvmAtmosphereRegistrationListener;
import name.martingeisse.webide.workbench.WorkbenchPage;

import org.apache.wicket.Page;
import org.apache.wicket.atmosphere.EventBus;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ResourceReferenceRegistry;

/**
 * The wicket application.
 */
public class WebIdeApplication extends WebApplication {
	
	/**
	 * the instance
	 */
	private volatile static WebIdeApplication instance;
	
	/**
	 * This method allows the caller to obtain the application
	 * instance from any thread, even non-wicket threads.
	 * @return the instance
	 */
	public static WebIdeApplication getCrossThreadInstance() {
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		instance = this;
		super.init();
		new EventBus(this);
		EventBus.get().addRegistrationListener(new SimvmAtmosphereRegistrationListener());
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
