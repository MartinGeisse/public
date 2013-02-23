/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

/**
 * Default implementation of {@link AbstractDelegatingContextMenuItemWithTextInput}
 * that does no specific conversion but simply passes the user input to the delegate.
 *
 * @param <C> the type of context data specified with the menu item
 * @param <A> the menu anchor type
 */
public final class DelegatingContextMenuItemWithTextInput<C, A> extends AbstractDelegatingContextMenuItemWithTextInput<C, A, String> {

	/**
	 * Constructor.
	 */
	public DelegatingContextMenuItemWithTextInput() {
		super();
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 */
	public DelegatingContextMenuItemWithTextInput(final String name) {
		super(name);
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 * @param delegate the delegate to invoke
	 */
	public DelegatingContextMenuItemWithTextInput(final String name, final IContextMenuDelegate<C, A, String> delegate) {
		super(name, delegate);
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 * @param delegate the delegate to invoke
	 * @param context the context to pass to the delegate
	 */
	public DelegatingContextMenuItemWithTextInput(final String name, final IContextMenuDelegate<C, A, String> delegate, final C context) {
		super(name, delegate, context);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.AbstractDelegatingContextMenuItemWithTextInput#convertParameter(java.lang.String)
	 */
	@Override
	protected String convertParameter(String userInput) {
		return userInput;
	}
	
}
