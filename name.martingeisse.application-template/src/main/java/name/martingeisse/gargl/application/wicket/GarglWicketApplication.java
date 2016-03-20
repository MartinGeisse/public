/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.gargl.application.wicket;

import name.martingeisse.gargl.application.page.AbstractApplicationPage;
import name.martingeisse.gargl.application.specialpage.CassandraResetPage;
import name.martingeisse.gargl.frontend.HelloWorldPage;
import name.martingeisse.wicket.application.AbstractMyWicketApplication;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.internal.HtmlHeaderContainer;
import org.apache.wicket.markup.resolver.HtmlHeaderResolver;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IExceptionMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.IProvider;

/**
 * The Wicket {@link WebApplication} implementation.
 */
public class GarglWicketApplication extends AbstractMyWicketApplication {

	/**
	 * the RENDER_PROFILING
	 */
	public static final boolean RENDER_PROFILING = false;
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		super.init();

		// settings
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setDefaultMarkupEncoding("utf-8");

		// register merged resources ("resource bundles")
		/*
		CssResourceReference[] mergedStylesheets = {
			new CssResourceReference(AbstractFrontendPage.class, "common.css"),
		};
		JavaScriptResourceReference[] mergedJavascripts = {
			JQueryResourceReference.get(),
			(JavaScriptResourceReference)getJavaScriptLibrarySettings().getWicketEventReference(),
			(JavaScriptResourceReference)(getDebugSettings().isAjaxDebugModeEnabled() ? getJavaScriptLibrarySettings().getWicketAjaxDebugReference() : getJavaScriptLibrarySettings().getWicketAjaxReference()),
			new JavaScriptResourceReference(AbstractFrontendPage.class, "fastclick.js"),
			new JavaScriptResourceReference(AbstractFrontendPage.class, "jquery.tools.min.js"),
			new JavaScriptResourceReference(AbstractFrontendPage.class, "jquery.scrollTo.min.js"),
			new JavaScriptResourceReference(AbstractFrontendPage.class, "common.js"),
		};
		getResourceBundles().addCssBundle(AbstractFrontendPage.class, "merged.css", mergedStylesheets);
		getResourceBundles().addJavaScriptBundle(AbstractFrontendPage.class, "merged.js", mergedJavascripts);
		

		// create CSS sprite atlases
		getSpriteRegistry().register(true,
			new PackageResourceReference(PaymentIcons.class, "invoice.png"),
			new PackageResourceReference(PaymentIcons.class, "mastercard.png"),
			new PackageResourceReference(PaymentIcons.class, "paypal.png"),
			new PackageResourceReference(PaymentIcons.class, "payu.png"),
			new PackageResourceReference(PaymentIcons.class, "prepay.png"),
			new PackageResourceReference(PaymentIcons.class, "sue.png"),
			new PackageResourceReference(PaymentIcons.class, "visa.png"),
			new PackageResourceReference(PaymentIcons.class, "cc.png"),
			new PackageResourceReference(PaymentIcons.class, "coll_store.png"),
			new PackageResourceReference(PaymentIcons.class, "cod.png"),
			new PackageResourceReference(PaymentIcons.class, "mcm.png"),
			new PackageResourceReference(PaymentIcons.class, "debit.png")
		);
		*/
//		ApplicationSpriteSupport.initialize(this);
//		ApplicationSpriteSupport.get().getSpriteRegistry().register(true,
//			new PackageResourceReference(Dummy.class, "de.png"),
//			new PackageResourceReference(Dummy.class, "gb.png"),
//			new PackageResourceReference(Dummy.class, "us.png")
//		);

		// --- mount pages ---
		// main pages
//		mountPage("foo", FooPage.class);
//		mountPage("bar/${id}", BarPage.class);
		// internal
		mountPage("cassandra-reset", CassandraResetPage.class);

		// mount Bootstrap fonts
		{
			final String[] bootstrapFontFiles = new String[] {
				"glyphicons-halflings-regular.eot", "glyphicons-halflings-regular.woff", "glyphicons-halflings-regular.ttf", "glyphicons-halflings-regular.svg",
			};
			for (final String fontFile : bootstrapFontFiles) {
				mountResource("/fonts/" + fontFile, new PackageResourceReference(AbstractApplicationPage.class, fontFile));
			}
		}

		// support render profiling
		if (RENDER_PROFILING) {
			replaceWicketComponentResolver(HtmlHeaderResolver.class, new HtmlHeaderResolver() {
				@Override
				protected HtmlHeaderContainer newHtmlHeaderContainer(String id) {
					return new HtmlHeaderContainer(id) {
						@Override
						protected void onRender() {
							long timingNow = System.currentTimeMillis();
							System.out.println("* at " + timingNow + ": HtmlHeaderContainer onRender start");
							super.onRender();
							long timingNewNow = System.currentTimeMillis();
							long timingTime = (timingNewNow - timingNow);
							System.out.println("* at " + timingNewNow + ": HtmlHeaderContainer onRender end, time = " + timingTime);
						}
					};
				}
			});
		}
		
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return HelloWorldPage.class;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.application.AbstractMyWicketApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
	 */
	@Override
	public Session newSession(final Request request, final Response response) {
		GarglWicketSession session = new GarglWicketSession(request);
		return session;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getExceptionMapperProvider()
	 */
	@Override
	public IProvider<IExceptionMapper> getExceptionMapperProvider() {
		return new IProvider<IExceptionMapper>() {
			@Override
			public IExceptionMapper get() {
				return new GarglExceptionMapper();
			}
		};
	}

}
