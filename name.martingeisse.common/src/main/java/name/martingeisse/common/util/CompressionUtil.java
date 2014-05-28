/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.util;

import java.io.ByteArrayOutputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import org.apache.log4j.Logger;

/**
 * Compression-related utility methods.
 */
public final class CompressionUtil {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(CompressionUtil.class);
	
	/**
	 * Prevent instantiation.
	 */
	private CompressionUtil() {
	}
	
	/**
	 * DEFLATEs the specified input data.
	 * 
	 * @param data the input data
	 * @param dictionary the dictionary, or null if none
	 * @return the compressed data
	 */
	public static byte[] deflate(byte[] data, byte[] dictionary) {
		Deflater deflater = new Deflater(8, true);
		if (dictionary != null) {
			deflater.setDictionary(dictionary);
		}
		deflater.setInput(data);
		deflater.finish();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[256];
		while (!deflater.finished()) {
			int n = deflater.deflate(buffer);
			byteArrayOutputStream.write(buffer, 0, n);
		}
		byte[] result = byteArrayOutputStream.toByteArray();
		return result;
	}
	
	/**
	 * Inflates (un-DEFLATEs) the specified compressed data.
	 * @param compressedData the compressed data
	 * @param dictionary the dictionary, or null if none
	 * @return the uncompressed data
	 */
	public static byte[] inflate(byte[] compressedData, byte[] dictionary) {
		// TODO the documentation for Inflater "expects a dummy byte" -- this seems to mean that
		// compressedData should be one byte longer than the input. Check if it works anyway;
		// Comment from Mark Adler on SO: "The current versions of zlib don't need it. I'm not
		// sure what version of zlib is used by java.util.zip."
		Inflater inflater = new Inflater(true);
		if (dictionary != null) {
			inflater.setDictionary(dictionary);
		}
		inflater.setInput(compressedData);
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[256];
			while (!inflater.finished()) {
				int n = inflater.inflate(buffer);
				byteArrayOutputStream.write(buffer, 0, n);
			}
			return byteArrayOutputStream.toByteArray();
		} catch (DataFormatException e) {
			throw new RuntimeException(e);
		}
	}
	
}
