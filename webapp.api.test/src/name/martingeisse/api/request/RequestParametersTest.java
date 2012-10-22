/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * TODO: document me
 *
 */
public class RequestParametersTest {

	/**
	 * 
	 */
	private static void checkBooleanParser(String value, boolean expectedResult) {
		RequestParameters parameters = new RequestParameters(null);
		parameters.getCustomParameters().put("testparam", value);
		assertEquals(expectedResult, parameters.getBoolean("testparam", true));
		assertEquals(expectedResult, parameters.getBoolean("testparam", false));
	}
	

	/**
	 * @throws Exception on errors
	 */
	@Test(expected = MissingRequestParameterException.class)
	public void testBooleanMissing() throws Exception {
		RequestParameters parameters = new RequestParameters(null);
		parameters.getCustomParameters().put("testparam", "true");
		parameters.getBoolean("other", true);
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testBooleanNull() throws Exception {
		RequestParameters parameters = new RequestParameters(null);
		parameters.getCustomParameters().put("testparam", "true");
		assertNull(parameters.getBoolean("other", false));
	}
	
	/**
	 * @throws Exception on errors
	 */
	@Test
	public void testBoolean() throws Exception {
		checkBooleanParser("0", false);
		checkBooleanParser("false", false);
		checkBooleanParser("no", false);
		checkBooleanParser("off", false);
		checkBooleanParser("1", true);
		checkBooleanParser("true", true);
		checkBooleanParser("yes", true);
		checkBooleanParser("on", true);
	}
	
}
