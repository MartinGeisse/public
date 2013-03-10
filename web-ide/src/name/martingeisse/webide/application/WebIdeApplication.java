/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.application;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import name.martingeisse.common.util.TemporaryFolder;
import name.martingeisse.webide.workbench.WorkbenchPage;

import org.apache.wicket.IRequestCycleProvider;
import org.apache.wicket.Page;
import org.apache.wicket.atmosphere.EventBus;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.cycle.RequestCycleContext;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.ResourceReferenceRegistry;
import org.atmosphere.cpr.AsyncIOWriter;
import org.atmosphere.cpr.AtmosphereRequest;
import org.atmosphere.cpr.AtmosphereResponse;

/**
 * The wicket application.
 */
public class WebIdeApplication extends WebApplication {
	
	/**
	 * the crippledAtmosphereHttpServletResponse
	 */
	private static final HttpServletResponse crippledAtmosphereHttpServletResponse;

	/**
	 * the instance
	 */
	private volatile static WebIdeApplication instance;

	// find the crippled response object which Atmosphere is using so we can ignore it later
	static {
		AtmosphereResponse dummyResponse = new AtmosphereResponse((AsyncIOWriter)null, (AtmosphereRequest)null, false);
		crippledAtmosphereHttpServletResponse = (HttpServletResponse)dummyResponse.getResponse();
	}
	
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
		TemporaryFolder.initialize("web-ide");
		setHeaderResponseDecorator(new MyHeaderResponseDecorator());
		setRequestCycleProvider(new IRequestCycleProvider() {
			@Override
			public RequestCycle get(RequestCycleContext context) {
				if (context.getResponse() instanceof WebResponse) {
					WebResponse webResponse = (WebResponse)context.getResponse();
					HttpServletResponse httpServletResponse = (HttpServletResponse)webResponse.getContainerResponse();
					if (httpServletResponse instanceof AtmosphereResponse) {
						AtmosphereResponse atmosphereResponse = (AtmosphereResponse)httpServletResponse;
						ServletResponse atmosphereInternalResponse = atmosphereResponse.getResponse();
						if (atmosphereInternalResponse == crippledAtmosphereHttpServletResponse) {
							System.err.println("--- found crippled response object ---");
						}
					}
				}
				return new RequestCycle(context);
			}
		});
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
