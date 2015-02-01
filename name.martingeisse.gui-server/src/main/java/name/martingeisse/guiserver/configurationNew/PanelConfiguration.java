/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configurationNew;


/**
 * The configuration for a panel.
 */
public final class PanelConfiguration extends ConfigurationElement {

	/**
	 * the CONFIGURATION_FILENAME_SUFFIX
	 */
	public static final String CONFIGURATION_FILENAME_SUFFIX = ".panel.xml";
	
	/**
	 * Constructor.
	 * @param path the path to this panel
	 */
	public PanelConfiguration(String path) {
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
