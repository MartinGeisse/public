/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.util;

import java.util.Collection;

import org.apache.wicket.Page;
import org.apache.wicket.atmosphere.PageKey;
import org.apache.wicket.atmosphere.ResourceRegistrationListener;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereResourceFactory;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * This class listens to {@link AtmosphereResource} generation events
 * and, for each registered resource, kills all old resources for the
 * same page, such that each page only has a single resource. Note
 * that this causes browser tabs using the old resource to stop working,
 * but they never fully worked anyway since the page could not properly
 * store per-resource data (e.g. for delta-rendered components).
 * 
 * Also *unused* old resources filled up memory and causes unnecessary but
 * expensive push-render cycles (the pages were rendered but never
 * displayed, since the AtmosphereResource wasn't used anymore).
 */
public class OldAtmosphereResourceCleaner implements ResourceRegistrationListener {

	/**
	 * the pageResourceUuids
	 */
	private static final Multimap<PageKey, String> pageResourceUuids = HashMultimap.create(); 
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.atmosphere.ResourceRegistrationListener#resourceRegistered(java.lang.String, org.apache.wicket.Page)
	 */
	@Override
	public void resourceRegistered(String newUuid, Page page) {
		PageKey pageKey = new PageKey(page.getPageId(), page.getSession().getId());
		Collection<String> oldUuids;
		synchronized(this) {
			oldUuids = pageResourceUuids.removeAll(pageKey);
			pageResourceUuids.put(pageKey, newUuid);
		}
		for (String oldUuid : oldUuids) {
			System.out.println("* removing connection for: " + oldUuid);
			AtmosphereResourceFactory.getDefault().remove(oldUuid);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.atmosphere.ResourceRegistrationListener#resourceUnregistered(java.lang.String)
	 */
	@Override
	public void resourceUnregistered(String uuid) {
	}
	
}
