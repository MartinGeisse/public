/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

/**
 * This menu item delegates to an {@link IContextMenuDelegate}. It does
 * not use the additional parameter specified by that interface (i.e.
 * it passes null for that parameter).
 *
 * @param <C> the type of context data specified with the menu item
 * @param <A> the menu anchor type
 */
public final class DelegatingContextMenuItem<C, A> extends SimpleContextMenuItem<A> {

	/**
	 * the delegate
	 */
	private IContextMenuDelegate<C, A, ?> delegate;

	/**
	 * the context
	 */
	private C context;

	/**
	 * Constructor.
	 */
	public DelegatingContextMenuItem() {
		super();
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 */
	public DelegatingContextMenuItem(final String name) {
		super(name);
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 * @param delegate the delegate to invoke
	 */
	public DelegatingContextMenuItem(final String name, final IContextMenuDelegate<C, A, ?> delegate) {
		super(name);
		this.delegate = delegate;
	}

	/**
	 * Constructor.
	 * @param name the name of this menu item
	 * @param delegate the delegate to invoke
	 * @param context the context to pass to the delegate
	 */
	public DelegatingContextMenuItem(final String name, final IContextMenuDelegate<C, A, ?> delegate, final C context) {
		super(name);
		this.delegate = delegate;
		this.context = context;
	}

	/**
	 * Getter method for the delegate.
	 * @return the delegate
	 */
	public IContextMenuDelegate<C, A, ?> getDelegate() {
		return delegate;
	}

	/**
	 * Setter method for the delegate.
	 * @param delegate the delegate to set
	 */
	public void setDelegate(final IContextMenuDelegate<C, A, ?> delegate) {
		this.delegate = delegate;
	}

	/**
	 * Getter method for the context.
	 * @return the context
	 */
	public C getContext() {
		return context;
	}

	/**
	 * Setter method for the context.
	 * @param context the context to set
	 */
	public void setContext(final C context) {
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.wicket.component.contextmenu.SimpleContextMenuItem#onSelect(java.lang.Object)
	 */
	@Override
	protected void onSelect(final A anchor) {
		delegate.invoke(context, anchor, null);
	}

}
