/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

import org.apache.wicket.Component;

/**
 * This menu item uses markup from an unrelated component to render
 * itself. It does not have a callback method for being clicked
 * since jQuery doesn't support this for such custom items.
 * 
 * On the client side, the component will be moved into the menu when
 * it is opened, and back when closed. This must be considered by
 * client-side scripts that might update components while the
 * menu is open.
 * 
 * "Moving back" the component markup actually means appending to the
 * parent element. This class cannot determine the exact time when
 * the component markup is moved to the menu, so to move it back to
 * its exact original location, it would have to insert a marker element
 * in advance to remember the location (which in itself can cause various
 * problems). Instead, caller code should simply make sure that appending
 * to the parent is sufficient; if that is not possible, an automatically
 * inserted marker element isn't likely possible either.
 * 
 * Using a component in this item causes it to enable
 * {@link Component#setOutputMarkupId(boolean)}.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public class ComponentMenuItem<A> extends ContextMenuItem<A> {

	/**
	 * the component
	 */
	private Component component;

	/**
	 * Constructor.
	 */
	public ComponentMenuItem() {
	}

	/**
	 * Constructor.
	 * @param component the component to use for the menu item
	 */
	public ComponentMenuItem(final Component component) {
		setComponent(component);
	}

	/**
	 * Getter method for the component.
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}

	/**
	 * Setter method for the component.
	 * @param component the component to set
	 */
	public void setComponent(final Component component) {
		this.component = component;
		if (component != null) {
			component.setOutputMarkupId(true);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#notifySelectedInternal(java.lang.Object, java.lang.Object)
	 */
	@Override
	void notifySelectedInternal(final A anchor, final Object data) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#buildItem(java.lang.StringBuilder, name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder)
	 */
	@Override
	void buildItem(final StringBuilder builder, final IContextMenuCallbackBuilder callbackBuilder) {
		builder.append("createComponentMenuItem('#").append(component.getMarkupId()).append("')");
	}

}
