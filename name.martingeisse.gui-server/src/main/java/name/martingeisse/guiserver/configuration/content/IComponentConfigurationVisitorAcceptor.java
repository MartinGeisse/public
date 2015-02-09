/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

/**
 * An interface to implement for visiting the component configuration structure.
 */
public interface IComponentConfigurationVisitorAcceptor {
	
	/**
	 * Accepts the specified visitor, calling its beginVisit(), passing
	 * it to child configurations, and calling its endVisit(). This
	 * method must also respect the flag returned from beginVisit().
	 *  
	 * @param visitor the visitor to accept
	 */
	public void accept(IComponentConfigurationVisitor visitor);
	
}
