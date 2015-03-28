/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.template;

/**
 * An interface to implement for visiting the component group configuration structure.
 */
public interface IComponentGroupConfigurationVisitorAcceptor {
	
	/**
	 * Accepts the specified visitor, calling its beginVisit(), passing
	 * it to child configurations, and calling its endVisit(). This
	 * method must also respect the flag returned from beginVisit().
	 *  
	 * @param visitor the visitor to accept
	 */
	public void accept(IComponentGroupConfigurationVisitor visitor);
	
}
