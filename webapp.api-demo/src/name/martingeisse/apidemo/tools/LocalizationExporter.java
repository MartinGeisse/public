/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo.tools;

import java.io.File;

import name.martingeisse.api.tools.CollectLocalizationPropertiesAction;

/**
 * Exports localization properties to property files.
 */
public class LocalizationExporter {

	/**
	 * The main method.
	 * @param args ignored
	 */
	public static void main(String[] args) {
		CollectLocalizationPropertiesAction action = new CollectLocalizationPropertiesAction();
		action.run(new File("src"));
	}

}
