/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.application;

import name.martingeisse.wicket.component.sprite.SpriteRegistry;

import org.apache.log4j.Logger;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.caching.FilenameWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.CachingResourceVersion;
import org.apache.wicket.request.resource.caching.version.LastModifiedResourceVersion;
import org.apache.wicket.util.lang.Bytes;

/**
 * Wicket {@link WebApplication} implementation for this application.
 */
public abstract class AbstractMyWicketApplication extends WebApplication {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(AbstractMyWicketApplication.class);

	/**
	 * the spriteRegistry
	 */
	private final SpriteRegistry spriteRegistry;

	/**
	 * Constructor.
	 */
	public AbstractMyWicketApplication() {
		this.spriteRegistry = new SpriteRegistry(this);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#init()
	 */
	@Override
	protected void init() {
		super.init();

		/* Configure wicket.
		 * 
		 * We disable the javascript compressor here since (a) it fails for the "minified" JQuery --
		 * producing invalid output code -- and (b) strips licenses, which would certainly get us
		 * into legal trouble.
		 * 
		 * We also disable GZIP compression here, and instead rely on a centralized GZIP filter.
		 * This is because only JavascriptPackageResource uses Wicket's built-in compression;
		 * CSS resources for example do not. Wicket also doesn't set the Vary: HTTP header.
		 * Centralized GZIP handling (and caching) should be more manageable.
		 */
		getApplicationSettings().setDefaultMaximumUploadSize(Bytes.megabytes(16));
		getResourceSettings().setJavaScriptCompressor(null);
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setStripComments(true);
		getMarkupSettings().setDefaultMarkupEncoding("utf-8");
		getMarkupSettings().setCompressWhitespace(true);

		// mount package resources using their revision number
		// NOTE: FirstLineResourceVersion is nice but only works when the file is *committed* on changes.
		// LastModifiedResourceVersion should do as long as the last-modified-date is preserved on deployment.
		getResourceSettings().setCachingStrategy(new FilenameWithVersionResourceCachingStrategy("-nocache-", new CachingResourceVersion(new LastModifiedResourceVersion())));

	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
	 */
	@Override
	public Session newSession(final Request request, final Response response) {
		return new MyWicketSession(request);
	}

	/**
	 * Getter method for the spriteRegistry.
	 * @return the spriteRegistry
	 */
	public SpriteRegistry getSpriteRegistry() {
		return spriteRegistry;
	}
	
}
