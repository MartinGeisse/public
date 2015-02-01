/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configurationNew.content;

/**
 * Base class for component configurations.
 */
public abstract class AbstractComponentConfiguration implements ComponentConfiguration {

	/**
	 * the id
	 */
	private final String id;

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public AbstractComponentConfiguration(String id) {
		this.id = id;
	}

	/**
	 * Getter method for the id.
	 * @return the id
	 */
	public final String getId() {
		return id;
	}
	
}
