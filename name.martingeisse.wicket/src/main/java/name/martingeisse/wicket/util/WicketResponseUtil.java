/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.util;

import java.io.IOException;
import java.io.Reader;
import name.martingeisse.common.util.CharArrayCharSequence;
import org.apache.wicket.request.Response;

/**
 * Utilities to deal with Wicket response objects.
 */
public final class WicketResponseUtil {

	/**
	 * Copies all text from the specified reader to the response.
	 * @param reader the reader
	 * @param response the response
	 * @throws IOException on I/O errors
	 */
	public static void copyText(Reader reader, Response response) throws IOException {
		char[] buffer = new char[1024];
		while (true) {
			int n = reader.read(buffer);
			if (n < 0) {
				break;
			}
			response.write(new CharArrayCharSequence(buffer, 0, n));
		}
	}
	
	/**
	 * Prevent instantiation.
	 */
	private WicketResponseUtil() {
	}
	
}
