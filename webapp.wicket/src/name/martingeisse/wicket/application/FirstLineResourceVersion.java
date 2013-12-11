/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.application;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import org.apache.wicket.request.resource.caching.IStaticCacheableResource;
import org.apache.wicket.request.resource.caching.version.IResourceVersion;
import org.apache.wicket.util.resource.IResourceStream;

/**
 * Determines the version of a resource from an SVN keyword in the
 * first line, provided that the content type of the resource is
 * accepted.
 */
public class FirstLineResourceVersion implements IResourceVersion {

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.resource.caching.version.IResourceVersion#getVersion(org.apache.wicket.request.resource.caching.IStaticCacheableResource)
	 */
	@Override
	public String getVersion(IStaticCacheableResource resource) {
		return getVersionFrom(resource);
	}

	/**
	 * Static method to get the version from a resource.
	 * @param resource the resource
	 * @return the version
	 */
	public static String getVersionFrom(IStaticCacheableResource resource) {
		try (IResourceStream stream = resource.getCacheableResourceStream()) {

			// load the resource and bail out early if we don't like the content type
			final String contentType = stream.getContentType();
			if (!contentType.startsWith("text/") && !contentType.equals("application/x-javascript")) {
				return null;
			}
			
			// read the first line
			final LineNumberReader reader = new LineNumberReader(new InputStreamReader(stream.getInputStream()));
			final String firstLine = reader.readLine();

			// look for the SVN keyword
			final int firstDollarPosition = firstLine.indexOf('$', 0);
			final int colonPosition = firstLine.indexOf(':', firstDollarPosition + 1);
			final int secondDollarPosition = firstLine.indexOf('$', colonPosition + 1);
			if (firstDollarPosition == -1 || colonPosition == -1 || secondDollarPosition == -1) {
				return null;
			} else {
				return firstLine.substring(colonPosition + 1, secondDollarPosition).trim();
			}

		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
