/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.security;

import java.util.Locale;
import name.martingeisse.common.util.HmacUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.ReadableInstant;
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
	public static String createToken(String subject, ReadableInstant timestamp, String secret) {
		StringBuilder builder = new StringBuilder();
		builder.append(subject);
		builder.append('|');
		builder.append(dateTimeFormatter.print(timestamp));
		String payload = builder.toString();
		builder.append('|');
		builder.append(HmacUtil.generateHmacBase64(payload, secret, HmacUtil.ALGORITHM_SHA256));
		return builder.toString();
	}
	
	/**
	 * Validates the specified security token. Returns the subject if the token is valid.
	 * Throws an {@link IllegalArgumentException} if invalid. This exception contains
	 * an error message about the problem.
	 * 
	 * @param token the token
	 * @param maxTimestamp the maximum allowed value for the token's timestamp
	 * @param secret the secret used to generate the HMAC
	 * @return the token's subject
	 */
	public static String validateToken(String token, ReadableInstant maxTimestamp, String secret) {
		
		// split the token into segments
		String[] tokenSegments = StringUtils.split(token, '|');
		if (tokenSegments.length != 3) {
			throw new IllegalArgumentException("malformed token (has " + tokenSegments.length + " segments)");
		}
		
		// validate the signature
		String payload = (tokenSegments[0] + '|' + tokenSegments[1]);
		String expectedSignatureBase64 = HmacUtil.generateHmacBase64(payload, secret, HmacUtil.ALGORITHM_SHA256);
		if (!expectedSignatureBase64.equals(tokenSegments[2])) {
			throw new IllegalArgumentException("invalid token signature");
		}
		
		// validate the timestamp
		ReadableInstant timestamp;
		try {
			timestamp = dateTimeFormatter.parseDateTime(tokenSegments[1]);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("malformed timestamp: " + e.getMessage());
		}
		if (timestamp.isAfter(maxTimestamp)) {
			throw new IllegalArgumentException("token has expired");
		}

		// return the subject
		return tokenSegments[0];
		
	}
	
}
