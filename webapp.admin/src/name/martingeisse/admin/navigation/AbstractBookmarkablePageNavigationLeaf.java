/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;


/**
 * This navigation leaf refers to a bookmarkable page. This is assumed to be
 * the most-used type of navigation leaf. Different subclasses use different
 * means to determine the target page. See {@link BookmarkablePageNavigationLeaf}
 * for a simple implementation that directly stores the page class and
 * page parameters. Other subclasses include e.g.
 * {@link GlobalEntityListNavigationLeaf} which links to a global entity
 * list presentation page (it also uses a page class and page parameters
 * to create a bookmarkable link, but obtains them on-the-fly from
 * higher-level settings).
 */
public abstract class AbstractBookmarkablePageNavigationLeaf extends AbstractNavigationLeaf {
}
