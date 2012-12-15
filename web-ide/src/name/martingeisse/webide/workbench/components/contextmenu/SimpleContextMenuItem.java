/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components.contextmenu;

/**
 * A simple item with a custom-specified name and callback (by
 * implementing the onSelect() method in a subclass).
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public abstract class SimpleContextMenuItem<A> extends ContextMenuItem<A> {

	/**
	 * the name
	 */
	private String name;

	/**
	 * Constructor.
	 */
	public SimpleContextMenuItem() {
	}

	/**
	 * Constructor.
	 * @param name the displayed name of this menu item
	 */
	public SimpleContextMenuItem(String name) {
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

	/**
	 * This method is invoked when the menu item is selected.
	 */
	protected abstract void onSelect();
	
	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.ContextMenuItem#notifySelectedInternal(java.lang.Object)
	 */
	@Override
	final void notifySelectedInternal(A anchor) {
		onSelect();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.ContextMenuItem#buildItem(java.lang.StringBuilder)
	 */
	@Override
	final void buildItem(final StringBuilder builder) {
		builder.append("{name: '").append(name).append("'}");
	}

}
