/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.elements;


/**
 * The configuration for a URL that is directly targeted by a form.
 */
public final class FormUrlConfiguration extends ConfigurationElement {

	/**
	 * the CONFIGURATION_FILENAME_SUFFIX
	 */
	public static final String CONFIGURATION_FILENAME_SUFFIX = ".form.properties";
	
	/**
	 * Constructor.
	 * @param path the path to this form API
	 */
	public FormUrlConfiguration(String path) {
		super(path);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configurationNew.ConfigurationElement#getBackendUriPath()
	 */
	@Override
	public String getBackendUriPath() {
		return getPath() + CONFIGURATION_FILENAME_SUFFIX;
	}

}
