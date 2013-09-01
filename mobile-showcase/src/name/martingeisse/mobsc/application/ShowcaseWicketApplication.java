/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.mobsc.application;

import name.martingeisse.mobsc.pages.HomePage;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.resource.JQueryResourceReference;

/**
 * The Wicket {@link WebApplication} implementation.
 */
public class ShowcaseWicketApplication extends WebApplication {

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
		CssResourceReference[] mergedStylesheets = {
			new CssResourceReference(HomePage.class, "common.css"),
		};
		JavaScriptResourceReference[] mergedJavascripts = {
			JQueryResourceReference.get(),
			(JavaScriptResourceReference)getJavaScriptLibrarySettings().getWicketEventReference(),
			(JavaScriptResourceReference)(getDebugSettings().isAjaxDebugModeEnabled() ? getJavaScriptLibrarySettings().getWicketAjaxDebugReference() : getJavaScriptLibrarySettings().getWicketAjaxReference()),
			new JavaScriptResourceReference(HomePage.class, "fastclick.js"),
			new JavaScriptResourceReference(HomePage.class, "common.js"),
		};
		getResourceBundles().addCssBundle(HomePage.class, "merged.css", mergedStylesheets);
		getResourceBundles().addJavaScriptBundle(HomePage.class, "merged.js", mergedJavascripts);
		
		// mount pages
//		mountPage("pages/#{pageSelector}", CustomPage.class);
//		mountPage("category/${categoryId}", CategoryPage.class);
//		mountPage("item/${shopItemId}", ItemPage.class);
//		mountPage("cart", CartPage.class);
//		mountPage("checkout", CheckoutPage.class);

	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

}
