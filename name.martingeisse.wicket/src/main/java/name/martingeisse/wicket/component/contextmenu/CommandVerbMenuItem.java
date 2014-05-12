/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

import name.martingeisse.common.terms.CommandVerb;

/**
 * A menu item that sends a command verb to its callback builder
 * (typically the same component that provides the anchor). The
 * anchor is ignored by the menu item itself, but the callback
 * builder will typically access the same anchor to interpret the
 * command verb.
 * 
 * Example: The resource view uses a JsTree both the define the anchor
 * (i.e. to provide selected resources to menu items) and as the callback
 * builder (i.e. to provide an AJAX mechanism to menu items). Other menu items
 * might operate on the anchor directly, but the "delete" item instead
 * sends the "delete" command verb, ignoring its anchor. The JsTree
 * receives the command verb, and in turn takes the same anchor which
 * this menu item ignored, and interprets the command verb by deleting
 * the selected resources. 
 * 
 * The main point of this class is to implement functionality that is accessible
 * both through the context menu and through keyboard shortcuts "once and
 * only once", by implementing it in the anchor component and having the
 * context menu delegate to there.
 * 
 * @param <A> the anchor type
 */
public final class CommandVerbMenuItem<A> extends AbstractNamedContextMenuItem<A> {

	/**
	 * the commandVerb
	 */
	private CommandVerb commandVerb;

	/**
	 * Constructor.
	 */
	public CommandVerbMenuItem() {
		super();
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 */
	public CommandVerbMenuItem(final String name) {
		super(name);
	}

	/**
	 * Constructor.
	 * @param commandVerb the command verb to send to the callback builder
	 */
	public CommandVerbMenuItem(final CommandVerb commandVerb) {
		super();
		this.commandVerb = commandVerb;
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 * @param commandVerb the command verb to send to the callback builder
	 */
	public CommandVerbMenuItem(final String name, final CommandVerb commandVerb) {
		super(name);
		this.commandVerb = commandVerb;
	}

	/**
	 * Getter method for the commandVerb.
	 * @return the commandVerb
	 */
	public CommandVerb getCommandVerb() {
		return commandVerb;
	}

	/**
	 * Setter method for the commandVerb.
	 * @param commandVerb the commandVerb to set
	 */
	public void setCommandVerb(final CommandVerb commandVerb) {
		this.commandVerb = commandVerb;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#notifySelectedInternal(java.lang.Object, java.lang.Object)
	 */
	@Override
	void notifySelectedInternal(final A anchor, final Object data) {
		throw new RuntimeException("the callback builder routed a command verb to the context menu");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.ContextMenuItem#buildItem(java.lang.StringBuilder, name.martingeisse.wicket.component.contextmenu.IContextMenuCallbackBuilder)
	 */
	@Override
	void buildItem(final StringBuilder builder, final IContextMenuCallbackBuilder callbackBuilder) {
		builder.append("createCommandVerbContextMenuItem('").append(getName());
		builder.append("', function() {");
		callbackBuilder.buildContextMenuCallback(builder, commandVerb);
		builder.append("})");
	}

}
