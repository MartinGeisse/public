/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.simvm.model;

import name.martingeisse.common.util.ReturnValueUtil;

/**
 * Base class for simulation model elements.
 */
public abstract class AbstractSimulationModelElement implements ISimulationModelElement {
	
	/**
	 * the customTitle
	 */
	private String customTitle;
	
	/**
	 * Getter method for the title.
	 * 
	 * @return the title
	 */
	@Override
	public final String getTitle() {
		if (customTitle != null) {
			return customTitle;
		}
		return ReturnValueUtil.nullNotAllowed(getDefaultTitle(), "getDefaultTitle");
	}
	
	/**
	 * Setter method for the title. Pass null to revert
	 * to the default title.
	 * 
	 * @param title the title to set, or null to use the default
	 */
	public final void setTitle(String title) {
		this.customTitle = title;
	}
	
	/**
	 * Returns the default title for this element, used
	 * as the default for {@link #getTitle()}. The default
	 * implementation returns {@link #toString()}.
	 * 
	 * @return the default title
	 */
	protected String getDefaultTitle() {
		return toString();
	}
	
}
