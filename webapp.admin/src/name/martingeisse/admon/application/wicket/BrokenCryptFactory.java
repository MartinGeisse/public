/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admon.application.wicket;

import org.apache.wicket.util.crypt.ICrypt;
import org.apache.wicket.util.crypt.ICryptFactory;

/**
 * This factory generates {@link ICrypt} objects which just throw an
 * exception when trying to use them. This makes sure that no code
 * relies on encryption when in fact we don't have proper encryption
 * support.
 * 
 * Rationale: The default encryption support from Wicket uses
 * one of two different fixed default keys and a fixed salt. These
 * values are known to anybody who downloads the Wicket distribution!
 * 
 * Information from the web: The salt and iterator count" are public
 * information, but the key isn't. Still, the salt may have to be
 * unique (not shared with other Wicket applications) to be useful.
 */
public class BrokenCryptFactory implements ICryptFactory, ICrypt {

	/**
	 * the MESSAGE
	 */
	private static String MESSAGE = "trying to use the broken crypto-implementation!";
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.util.crypt.ICrypt#decryptUrlSafe(java.lang.String)
	 */
	@Override
	public String decryptUrlSafe(String text) {
		throw new RuntimeException(MESSAGE);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.crypt.ICrypt#encryptUrlSafe(java.lang.String)
	 */
	@Override
	public String encryptUrlSafe(String plainText) {
		throw new RuntimeException(MESSAGE);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.crypt.ICrypt#setKey(java.lang.String)
	 */
	@Override
	public void setKey(String key) {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.crypt.ICryptFactory#newCrypt()
	 */
	@Override
	public ICrypt newCrypt() {
		return this;
	}
	
}
