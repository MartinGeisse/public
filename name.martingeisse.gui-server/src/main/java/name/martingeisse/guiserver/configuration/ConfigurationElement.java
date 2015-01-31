/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

/**
 * Common interface for all configuration elements (pages, namespaces, ...)
 */
public interface ConfigurationElement {
	
	/**
	 * Initializes this element.
	 * @param parentNamespace the parent namespace that contains this element
	 * @param key the key used for this element within the parent namespace
	 */
	public void initialize(ConfigurationNamespace parentNamespace, String key);
	
	/**
	 * Returns the parent namespace
	 * @return the parent namespace
	 */
	public ConfigurationNamespace getParentNamespace();
	
	/**
	 * Returns the key used for this element within the parent namespace
	 * @return the key used for this element within the parent namespace
	 */
	public String getKey();
	
	/**
	 * Returns the absolute path of this element in the configuration
	 * @return the absolute path of this element in the configuration
	 */
	public String getPath();
	
}
