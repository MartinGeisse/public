/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import name.martingeisse.admin.navigation.handler.DisabledNavigationNodeHandler;

/**
 * Default implementation if {@link INavigationNodeHandlerMergeStrategy}.
 * This strategy can only merge handlers if either handler is a
 * {@link DisabledNavigationNodeHandler}, and merges them by returning
 * the other handler.
 */
public class DefaultNavigationNodeHandlerMergeStrategy implements INavigationNodeHandlerMergeStrategy {

	/**
	 * Constructor.
	 */
	public DefaultNavigationNodeHandlerMergeStrategy() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandlerMergeStrategy#merge(name.martingeisse.admin.navigation.INavigationNodeHandler, name.martingeisse.admin.navigation.INavigationNodeHandler)
	 */
	@Override
	public INavigationNodeHandler merge(INavigationNodeHandler firstHandler, INavigationNodeHandler secondHandler) {
		if (firstHandler instanceof DisabledNavigationNodeHandler) {
			return secondHandler;
		} else if (secondHandler instanceof DisabledNavigationNodeHandler) {
			return firstHandler;
		} else {
			throw new IllegalArgumentException("Cannot merge: Neither node handler is a DisabledNavigationNodeHandler");
		}
	}

}
