/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

/**
 * An interface to implement for visiting the component configuration structure.
 */
public interface IComponentConfigurationVisitor {

	/**
	 * Begins visiting a component configuration.
	 * 
	 * @param componentConfiguration the configuration being visited
	 * @return true to continue visiting this configuration, false to skip it.
	 * Skipping will skip visiting children, and will also skip the call to
	 * {@link #endVisit(ComponentConfiguration)}.
	 */
	public boolean beginVisit(ComponentConfiguration componentConfiguration);
	
	/**
	 * Ends visiting a component configuration.
	 * 
	 * @param componentConfiguration the configuration being visited
	 */
	public void endVisit(ComponentConfiguration componentConfiguration);
	
}
