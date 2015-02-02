/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration.elements;


/**
 * The configuration for a form-processing API.
 */
public final class FormApiConfiguration extends ConfigurationElement {

	/**
	 * the CONFIGURATION_FILENAME_SUFFIX
	 */
	public static final String CONFIGURATION_FILENAME_SUFFIX = ".api";
	
	/**
	 * Constructor.
	 * @param path the path to this form API
	 */
	public FormApiConfiguration(String path) {
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
