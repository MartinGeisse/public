/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

/**
 * This menu item delegates to an {@link IContextMenuDelegate}. The
 * additional parameter passed to that interface is converted from
 * a string by the subclass, input by the user after selecting the
 * menu item.
 * 
 * See {@link DelegatingContextMenuItemWithTextInput} for a default
 * implementation that does no specific conversion but simply passes
 * the user input to the delegate.
 *
 * @param <C> the type of context data specified with the menu item
 * @param <A> the menu anchor type
 * @param <P> the type of additional parameter data passed on invocation
 */
public abstract class AbstractDelegatingContextMenuItemWithTextInput<C, A, P> extends SimpleContextMenuItem<A> {

	/**
	 * the delegate
	 */
	private IContextMenuDelegate<C, A, P> delegate;

	/**
	 * the context
	 */
	private C context;

	/**
	 * Constructor.
	 */
	public AbstractDelegatingContextMenuItemWithTextInput() {
		super();
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 */
	public AbstractDelegatingContextMenuItemWithTextInput(final String name) {
		super(name);
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 * @param delegate the delegate to invoke
	 */
	public AbstractDelegatingContextMenuItemWithTextInput(final String name, final IContextMenuDelegate<C, A, P> delegate) {
		super(name);
		this.delegate = delegate;
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 * @param delegate the delegate to invoke
	 * @param context the context to pass to the delegate
	 */
	public AbstractDelegatingContextMenuItemWithTextInput(final String name, final IContextMenuDelegate<C, A, P> delegate, final C context) {
		super(name);
		this.delegate = delegate;
		this.context = context;
	}

	/**
	 * Getter method for the delegate.
	 * @return the delegate
	 */
	public final IContextMenuDelegate<C, A, P> getDelegate() {
		return delegate;
	}

	/**
	 * Setter method for the delegate.
	 * @param delegate the delegate to set
	 */
	public final void setDelegate(final IContextMenuDelegate<C, A, P> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Getter method for the context.
	 * @return the context
	 */
	public final C getContext() {
		return context;
	}

	/**
	 * Setter method for the context.
	 * @param context the context to set
	 */
	public final void setContext(final C context) {
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.SimpleContextMenuItem#onSelect(java.lang.Object)
	 */
	@Override
	protected final void onSelect(final A anchor) {
		delegate.invoke(context, anchor, null);
	}
	
	/**
	 * Converts the user input to the parameter that is then passed
	 * to the delegate.
	 * @param userInput the user input
	 * @return the converted parameter
	 */
	protected abstract P convertParameter(String userInput);

}
