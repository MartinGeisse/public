/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

/**
 * Base class for menu items that have names.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public abstract class AbstractNamedContextMenuItem<A> extends ContextMenuItem<A> {

	/**
	 * the name
	 */
	private String name;

	/**
	 * Constructor.
	 */
	public AbstractNamedContextMenuItem() {
	}

	/**
	 * Constructor.
	 * @param name the displayed name of this menu item
	 */
	public AbstractNamedContextMenuItem(String name) {
		this.name = name;
	}

	/**
	 * Getter method for the name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for the name.
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

}
