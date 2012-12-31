/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench.components.contextmenu;

/**
 * A simple item with a custom-specified name and callback (by
 * implementing the onSelect() method in a subclass). This menu
 * item asks the user for an additional input string before
 * invoking the server-side handling method.
 * 
 * @param <A> the anchor type (see {@link ContextMenu} for explanation)
 */
public abstract class SimpleContextMenuItemWithTextInput<A> extends AbstractNamedContextMenuItem<A> {

	/**
	 * the prompt
	 */
	private String prompt;

	/**
	 * Constructor.
	 */
	public SimpleContextMenuItemWithTextInput() {
	}

	/**
	 * Constructor.
	 * @param name the displayed name of this menu item
	 * @param prompt the prompt to show
	 */
	public SimpleContextMenuItemWithTextInput(final String name, String prompt) {
		super(name);
		this.prompt = prompt;
	}

	/**
	 * Getter method for the prompt.
	 * @return the prompt
	 */
	public String getPrompt() {
		return prompt;
	}

	/**
	 * Setter method for the prompt.
	 * @param prompt the prompt to set
	 */
	public void setPrompt(final String prompt) {
		this.prompt = prompt;
	}

	/**
	 * This method is invoked when the menu item is selected.
	 * @param anchor the anchor
	 * @param data the data entered by the user
	 */
	protected abstract void onSelect(A anchor, String data);

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.ContextMenuItem#notifySelectedInternal(java.lang.Object)
	 */
	@Override
	final void notifySelectedInternal(final A anchor, final Object data) {
		onSelect(anchor, (String)data);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.ContextMenuItem#buildItem(java.lang.StringBuilder, name.martingeisse.webide.workbench.components.contextmenu.IContextMenuCallbackBuilder)
	 */
	@Override
	void buildItem(final StringBuilder builder, final IContextMenuCallbackBuilder callbackBuilder) {
		builder.append("createContextMenuItemWithPrompt('").append(getName()).append("', '").append(prompt);
		builder.append("', function(key, data, options) {");
		callbackBuilder.buildContextMenuCallback(builder);
		builder.append("})");
	}

}
