/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

/**
 * An interface to implement for visiting the component group configuration structure.
 */
public interface IComponentGroupConfigurationVisitor {

	/**
	 * Begins visiting a component group configuration.
	 * 
	 * @param componentGroupConfiguration the configuration being visited
	 * @return true to continue visiting this configuration, false to skip it.
	 * Skipping will skip visiting children, and will also skip the call to
	 * {@link #endVisit(ComponentGroupConfiguration)}.
	 */
	public boolean beginVisit(ComponentGroupConfiguration componentGroupConfiguration);
	
	/**
	 * Ends visiting a component group configuration.
	 * 
	 * @param componentGroupConfiguration the configuration being visited
	 */
	public void endVisit(ComponentGroupConfiguration componentGroupConfiguration);
	
}
