/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;


/**
 * Standard link component. This component supports a limited feature set
 * that satisfies many use cases, but is intentionally limited to be
 * simple to use.
 */
public final class LinkConfiguration implements ContentElementConfiguration {

	/**
	 * the targetPageConfigurationPath
	 */
	private final String targetPageConfigurationPath;

	/**
	 * the label
	 */
	private final String label;

	/**
	 * Constructor.
	 */
	private LinkConfiguration(String targetPageConfigurationPath, String label) {
		this.targetPageConfigurationPath = targetPageConfigurationPath;
		this.label = label;
	}

	/**
	 * Getter method for the targetPageConfigurationPath.
	 * @return the targetPageConfigurationPath
	 */
	public String getTargetPageConfigurationPath() {
		return targetPageConfigurationPath;
	}

	/**
	 * Getter method for the label.
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Creates a link to the specified page.
	 * 
	 * @param targetPageConfigurationPath the path configuration page of the page to link to
	 * @param label the link label text
	 * @return the link configuration
	 */
	public static LinkConfiguration forPageConfiguration(String targetPageConfigurationPath, String label) {
		return new LinkConfiguration(targetPageConfigurationPath, label);
	}

}
