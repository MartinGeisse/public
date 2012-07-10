/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.util.Iterator;
import java.util.List;

import name.martingeisse.admin.application.wicket.AdminWicketApplication;
import name.martingeisse.admin.navigation.leaf.INavigationLeafVisitor;
import name.martingeisse.admin.pages.NavigationFolderPage;
import name.martingeisse.common.util.SpecialHandlingList;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;

/**
 * This class represents a navigation node that just contains
 * other nodes but does not link an application page itself.
 * Depending on the presentation, navigation folders DO have
 * a page, but it is only used to show its child nodes.
 */
public final class NavigationFolder extends AbstractNavigationNode implements INavigationParentNode {

	/**
	 * the children
	 */
	private final List<INavigationNode> children = new SubNodeList();

	/**
	 * Constructor.
	 */
	public NavigationFolder() {
	}

	/**
	 * Getter method for the children.
	 * @return the children
	 */
	public List<INavigationNode> getChildren() {
		return children;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<INavigationNode> iterator() {
		return children.iterator();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.AbstractNavigationNode#createLink(java.lang.String)
	 */
	@Override
	public AbstractLink createLink(final String id) {
		
//		return new Link<Void>(id) {
//			@Override
//			public void onClick() {
//				RequestCycle.get().setResponsePage(new NavigationFolderPage(NavigationFolder.this));
//			}
//		};
		
		// TODO: This calls for a new type of bookmarkable link, not based
		// on page class but on navigation path
		return new ExternalLink(id, getPath());
		
	}

	/**
	 * Adds a child node to the list of children and sets this node as the
	 * child's parent. Also sets the child's title to the specified title. Returns the child.
	 * @param <T> the type of the child
	 * @param child the child to add
	 * @param id the id to set for the child
	 * @param title the title to set for the child
	 * @return the child
	 */
	public <T extends AbstractNavigationNode> T initChild(final T child, final String id, final String title) {
		children.add(child);
		child.setId(id);
		child.setTitle(title);
		return child;
	}

	/**
	 * Creates a new {@link NavigationFolder} with the specified title, then adds
	 * the new folder to this folder and sets the new folder's parent to this.
	 * @param id the id to set for the child
	 * @param title the title of the new subfolder
	 * @return the new folder
	 */
	public NavigationFolder addNewSubfolder(final String id, final String title) {
		return initChild(new NavigationFolder(), id, title);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#visitLeafNodes(name.martingeisse.admin.navigation.leaf.INavigationLeafVisitor)
	 */
	@Override
	public void visitLeafNodes(final INavigationLeafVisitor visitor) {
		for (final INavigationNode child : children) {
			child.visitLeafNodes(visitor);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#findMostSpecificNode(java.lang.String)
	 */
	@Override
	public INavigationNode findMostSpecificNode(String path) {
		if (path == null || path.isEmpty() || path.charAt(0) != '/') {
			return this;
		}
		int index = path.indexOf('/', 1);
		index = (index == -1 ? path.length() : index);
		String segment = path.substring(1, index);
		for (final INavigationNode child : children) {
			if (segment.equals(child.getId())) {
				return child.findMostSpecificNode(path.substring(index));
			}
		}
		return this;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.INavigationNode#mountRequestMappers(name.martingeisse.admin.application.wicket.AdminWicketApplication)
	 */
	@Override
	public void mountRequestMappers(AdminWicketApplication application) {
		if (getParent() != null) {
			application.mount(new NavigationMountedRequestMapper(getPath(), NavigationFolderPage.class));
		}
		for (INavigationNode child : children) {
			child.mountRequestMappers(application);
		}
	}
	
	/**
	 * Specialized list implementation that sets the parent node of sub-nodes.
	 */
	private class SubNodeList extends SpecialHandlingList<INavigationNode> {

		/* (non-Javadoc)
		 * @see name.martingeisse.common.util.SpecialHandlingList#onAfterAddElement(java.lang.Object)
		 */
		@Override
		protected void onAfterAddElement(final INavigationNode element) {
			element.setParent(NavigationFolder.this);
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.common.util.SpecialHandlingList#onBeforeRemoveElement(java.lang.Object)
		 */
		@Override
		protected void onBeforeRemoveElement(final INavigationNode element) {
			element.setParent(null);
		}

	}

}
