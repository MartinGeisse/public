/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation.handler;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.NavigationNode;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.mount.MountMapper;

/**
 * This handler does not mount a page. When asked to create a link, this handler
 * delegates to its first child to create the link, so what looks like a link
 * to the root actually links to the first child. In addition, this handler
 * mounts a redirect to its first child in case the URL is being accessed
 * directly. Obviously, this handler *requires* its node to have at least
 * one child.
 * 
 * Note that since the markup content of the link are not created by navigation
 * nodes directly but by the caller, the title used for the link text is still
 * that of this handler (and not that of the first child). This is intentional:
 * Assume that thing X is represented by the node of this handler. Sub-nodes
 * represent aspects of X. When X is linked to, the link shall lead to the
 * default presentation of X, and that is provided by the first child. However,
 * the link text shall not read "default presentation of X" or
 * "overview of X" but "X".
 *
 * An example where this handler is useful: Assume there is a sub-area of the
 * navigation tree that has its own navigation menu. If that menu cannot
 * offer a link to the root node (for whatever reason, typically because it
 * doesn't fit well into the UI) then once the user leaves the page for the
 * root node, he/she cannot return (except with the back button, but it would
 * be ugly to *require* using the back button for that). Such a local root
 * node would typically show an overview page. With this handler, the root
 * can redirect to its first child, and the overview be inserted as an
 * additional (first) child of the root. Now the actual root isn't important
 * anymore and the user can return to the overview using the normal navigation
 * menu.
 */
public final class FirstChildNavigationHandler extends AbstractNavigationNodeHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.handler.AbstractNavigationNodeHandler#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public void mountRequestMappers(final AdminWicketApplication application, final NavigationNode node) {
		final IRequestHandler requestHandler = new RedirectRequestHandler(getFirstChild(node).getPath());
		application.mount(new MountMapper(node.getPath(), requestHandler));
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNodeHandler#createLink(java.lang.String, name.martingeisse.admin.navigation.NavigationNode)
	 */
	@Override
	public AbstractLink createLink(final String id, final NavigationNode node) {
		return getFirstChild(node).createLink(id);
	}

	/**
	 * @param node
	 * @return
	 */
	private NavigationNode getFirstChild(final NavigationNode node) {
		if (node.getChildren().isEmpty()) {
			throw new IllegalStateException("FirstChildNavigationHandler used for child-less node " + node.getPath());
		}
		return node.getChildren().get(0);
	}

}
