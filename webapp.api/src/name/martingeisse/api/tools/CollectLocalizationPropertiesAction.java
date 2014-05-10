/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.api.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;

/**
 * This action scans all property files, converts property names to canonical
 * names based on the package, base file name, and original property name,
 * then generates a single property file per locale using those canonical
 * names.
 */
public class CollectLocalizationPropertiesAction extends LocalizationFileAction {

	/**
	 * the canonicalizedPropertiesByLocale
	 */
	private Map<String, Map<String, String>> canonicalizedPropertiesByLocale;
	
	/**
	 * Constructor.
	 */
	public CollectLocalizationPropertiesAction() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.tools.LocalizationFileAction#run(java.io.File[])
	 */
	@Override
	public void run(File... sourceFolders) {
		canonicalizedPropertiesByLocale = new HashMap<String, Map<String, String>>();
		super.run(sourceFolders);
		for (Map.Entry<String, Map<String, String>> localeEntry : canonicalizedPropertiesByLocale.entrySet()) {
			String localeName = localeEntry.getKey();
			Properties properties = new Properties();
			for (Map.Entry<String, String> textEntry : localeEntry.getValue().entrySet()) {
				properties.put(textEntry.getKey(), textEntry.getValue());
			}
			writeOutputFileForLocale(localeName, properties);
		}
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.api.tools.LocalizationFileAction#onLocalizationFile(java.util.LinkedList, java.io.File, java.lang.String, java.lang.String)
	 */
	@Override
	protected void onLocalizationFile(LinkedList<String> packageStack, File file, String baseName, String localeName) {
		
		// obtain the per-locale map
		Map<String, String> mapForLocale = canonicalizedPropertiesByLocale.get(localeName);
		if (mapForLocale == null) {
			mapForLocale = new HashMap<String, String>();
			canonicalizedPropertiesByLocale.put(localeName, mapForLocale);
		}
		
		// load the property file
		Properties properties = new Properties();
		try (InputStream inputStream = new FileInputStream(file)) {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// canonicalize the properties and store them in the per-locale map
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			String name = (String)entry.getKey();
			String value = (String)entry.getValue();
			String canonicalName = canonicalizePropertyName(name, packageStack, file, baseName);
			mapForLocale.put(canonicalName, value);
		}
		
	}
	
	/**
	 * Returns the canonical property name for the specified property.
	 * 
	 * @param propertyName the name of the property in its original property file
	 * @param packageStack the package stack
	 * @param file the original property file
	 * @param baseName the base name of the property file
	 * @return the canonical property name
	 */
	protected String canonicalizePropertyName(String propertyName, LinkedList<String> packageStack, File file, String baseName) {
		return getPackageName(packageStack) + '.' + propertyName;
	}

	/**
	 * Writes the output file for the specified locale name.
	 * @param localeName the locale name
	 * @param properties the properties to write
	 */
	protected void writeOutputFileForLocale(String localeName, Properties properties) {
		File file = new File("localization_" + localeName + ".properties");
		try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
			properties.store(fileOutputStream, null);
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
}
