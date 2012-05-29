/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.servlet.gzip;

/**
 * A cache key for the GZIP cache.
 */
public final class CannedResponseCacheKey {

	/**
	 * the url
	 */
	private final String url;
	
	/**
	 * Constructor.
	 * @param url the URL
	 */
	public CannedResponseCacheKey(String url) {
		this.url = url;
	}

	/**
	 * Getter method for the url.
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CannedResponseCacheKey) {
			CannedResponseCacheKey other = (CannedResponseCacheKey)obj;
			return (url.equals(other.url));
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return url.hashCode();
	}
	
}
