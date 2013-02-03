/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import name.martingeisse.webide.resources.ResourcePath;

import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * A Wicket resource reference for plugin bundle JARs and files contained in them.
 */
public final class PluginBundleWicketResourceReference extends ResourceReference {

	/**
	 * Constructor.
	 * @param referenceName the reference name including the plugin bundle id and
	 * optionally a local path
	 */
	public PluginBundleWicketResourceReference(String referenceName) {
		super(PluginBundleWicketResourceReference.class, referenceName);
		validateReferenceName(referenceName);
	}
	
	/**
	 * Constructor that selects the JAR file itself.
	 * @param pluginBundleId the plugin bundle ID
	 */
	public PluginBundleWicketResourceReference(final long pluginBundleId) {
		this(pluginBundleId, null);
	}

	/**
	 * Constructor.
	 * @param pluginBundleId the plugin bundle ID
	 * @param path the path inside the plugin bundle files. May be null to select the
	 * JAR file itself, otherwise it must be an absolute path.
	 */
	public PluginBundleWicketResourceReference(final long pluginBundleId, ResourcePath path) {
		super(PluginBundleWicketResourceReference.class, referenceToString(pluginBundleId, path));
	}

	/**
	 * 
	 */
	private static void validateReferenceName(String referenceName) {
		int firstSlashIndex = referenceName.indexOf('/');
		String pluginBundleIdText, localPathText;
		if (firstSlashIndex == -1) {
			pluginBundleIdText = referenceName;
			localPathText = null;
		} else {
			pluginBundleIdText = referenceName.substring(0, firstSlashIndex);
			localPathText = referenceName.substring(firstSlashIndex);
			new ResourcePath(localPathText);
		}
		Long.parseLong(pluginBundleIdText);
	}
	
	/**
	 * 
	 */
	private static String referenceToString(final long pluginBundleId, ResourcePath path) {
		StringBuilder builder = new StringBuilder();
		builder.append(pluginBundleId);
		if (path != null) {
			if (!path.isLeadingSeparator()) {
				throw new IllegalArgumentException("Cannot build a PluginBundleWicketResourceReference from a relative path: " + path);
			}
			builder.append(path);
		}
		return builder.toString();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.ResourceReference#getResource()
	 */
	@Override
	public IResource getResource() {
		String referenceName = getName();
		int firstSlashIndex = referenceName.indexOf('/');
		String pluginBundleIdText, localPathText;
		if (firstSlashIndex == -1) {
			pluginBundleIdText = referenceName;
			localPathText = null;
		} else {
			pluginBundleIdText = referenceName.substring(0, firstSlashIndex);
			localPathText = referenceName.substring(firstSlashIndex);
		}
		long pluginBundleId = Long.parseLong(pluginBundleIdText);
		ResourcePath localPath = (localPathText == null ? null : new ResourcePath(localPathText));
		return new PluginBundleWicketResource(pluginBundleId, localPath);
	}

}
