/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security;

import java.util.Locale;
import name.martingeisse.common.util.HmacUtil;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Some utility methods to handle signed security tokens.
 */
public final class SecurityTokenUtil {

	/**
	 * the dateTimeFormatter
	 */
	private static final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis().withLocale(Locale.US).withZoneUTC();
	
	/**
	 * Prevent instantiation.
	 */
	private SecurityTokenUtil() {
	}
	
	/**
	 * Cretes a signed token that contains a subject (a simple keyword that tells what
	 * the token is about), a timestamp, and a HMAC that is generated from the payload
	 * data and a secret.
	 * 
	 * This method makes no assumptions about whether the timestamp denotes the creation
	 * time or the expiry time of the token. This is left to the methods that check
	 * tokens for validity.
	 * 
	 * @param subject the subject. Should not contain pipe characters.
	 * @param timestamp the timestamp
	 * @param secret the secret used to generate the HMAC
	 * @return the token
	 */
	public static String createToken(String subject, Instant timestamp, String secret) {
		StringBuilder builder = new StringBuilder();
		builder.append(subject);
		builder.append('|');
		builder.append(dateTimeFormatter.print(timestamp));
		String payload = builder.toString();
		builder.append('|');
		builder.append(HmacUtil.generateHmacBase64(payload, secret, HmacUtil.ALGORITHM_SHA256));
		return builder.toString();
	}
	
}
