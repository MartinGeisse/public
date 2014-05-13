/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server;

/**
 * TODO fields from this class should be moved to the server configuration
 */
public class MinerServerSecurityConstants {

	/**
	 * the SECURITY_TOKEN_SECRET
	 */
	public static final String SECURITY_TOKEN_SECRET = "qwiuofghiuqwhqipuwhfiuqoghfiuoqwhfiuqwbfhowquizgbfhuqhfgiuqghio";
	
	/**
	 * the ACCOUNT_ACCESS_TOKEN_MAX_AGE_SECONDS
	 */
	public static final int ACCOUNT_ACCESS_TOKEN_MAX_AGE_SECONDS = 5 * 60;
	
	/**
	 * the ACCOUNT_ACCESS_TOKEN_MAX_AGE_MILLISECONDS
	 */
	public static final int ACCOUNT_ACCESS_TOKEN_MAX_AGE_MILLISECONDS = 1000 * ACCOUNT_ACCESS_TOKEN_MAX_AGE_SECONDS;

}
