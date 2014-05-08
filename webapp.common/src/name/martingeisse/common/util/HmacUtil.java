/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * Utility methods to deal with HMACs.
 */
public class HmacUtil {

	/**
	 * Use this constant for the "algorithm" parameter for SHA256.
	 */
	public static final String ALGORITHM_SHA256 = "HmacSHA256";
	
	/**
	 * Base function to generate a HMAC.
	 * 
	 * @param payload the payload data
	 * @param secret the secret to sign the HMAC
	 * @param algorithm the HMAC algorithm to use
	 * @return the HMAC
	 */
	public static byte[] generateHmac(byte[] payload, byte[] secret, String algorithm) {
		try {
			final Mac mac = Mac.getInstance(algorithm);
			mac.init(new SecretKeySpec(secret, algorithm));
			return mac.doFinal(payload);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Generates a HMAC, assuming UTF-8 encoding for all strings.
	 * 
	 * @param payload the payload data
	 * @param secret the secret to sign the HMAC
	 * @param algorithm the HMAC algorithm to use
	 * @return the HMAC
	 */
	public static byte[] generateHmac(String payload, String secret, String algorithm) {
		Charset utf8 = Charset.forName("utf-8");
		return generateHmac(payload.getBytes(utf8), secret.getBytes(utf8), algorithm);
	}
	
	/**
	 * Generates a HMAC, assuming UTF-8 encoding for all strings, and base64-encodes
	 * the result.
	 * 
	 * @param payload the payload data
	 * @param secret the secret to sign the HMAC
	 * @param algorithm the HMAC algorithm to use
	 * @return the HMAC, base64 encoded
	 */
	public static String generateHmacBase64(String payload, String secret, String algorithm) {
		return Base64.encodeBase64String(generateHmac(payload, secret, algorithm));
	}

}
