/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;


/**
 * Base interface for all navigation nodes that can be the
 * parent node of other nodes.
 */
public interface INavigationParentNode extends INavigationNode, Iterable<INavigationNode> {
}
