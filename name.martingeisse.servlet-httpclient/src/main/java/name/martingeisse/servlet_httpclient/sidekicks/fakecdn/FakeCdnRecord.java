/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.servlet_httpclient.sidekicks.fakecdn;

/**
 * A record that describes a file cached in the fake CDN.
 */
public final class FakeCdnRecord {

	/**
	 * the statusCode
	 */
	private final int statusCode;
	
	/**
	 * the key
	 */
	private final String key;

	/**
	 * the contentType
	 */
	private final String contentType;

	/**
	 * the data
	 */
	private final byte[] data;

	/**
	 * Constructor.
	 * @param statusCode the HTTP status code
	 * @param key the CDN storage key
	 * @param contentType the content type
	 * @param data the data
	 */
	public FakeCdnRecord(final int statusCode, final String key, final String contentType, final byte[] data) {
		this.statusCode = statusCode;
		this.key = key;
		this.contentType = contentType;
		this.data = data;
	}

	/**
	 * Getter method for the statusCode.
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}
	
	/**
	 * Getter method for the key.
	 * @return the key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Getter method for the contentType.
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}
	
	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

}
