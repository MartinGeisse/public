/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.navigation;

import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.pages.NavigationFolderPage;

import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * This class represents a navigation node that just contains
 * other nodes but does not link an application page itself.
 * Depending on the presentation, navigation folders DO have
 * a page, but it is only used to show its child nodes.
 */
public class NavigationFolder extends AbstractNavigationNode {

	/**
	 * the children
	 */
	private List<AbstractNavigationNode> children = new ArrayList<AbstractNavigationNode>();

	/**
	 * Constructor.
	 */
	public NavigationFolder() {
	}

	/**
	 * Getter method for the children.
	 * @return the children
	 */
	public List<AbstractNavigationNode> getChildren() {
		return children;
	}

	/**
	 * Setter method for the children.
	 * @param children the children to set
	 */
	public void setChildren(final List<AbstractNavigationNode> children) {
		this.children = children;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.AbstractNavigationNode#createLink(java.lang.String)
	 */
	@Override
	public AbstractLink createLink(final String id) {
		return new Link<Void>(id) {
			@Override
			public void onClick() {
				RequestCycle.get().setResponsePage(new NavigationFolderPage(NavigationFolder.this));
			}
		};
	}

	/**
	 * Adds a child node to the list of children and sets this node as the
	 * child's parent. Returns the child.
	 * @param <T> the type of the child
	 * @param child the child to add
	 * @return the child
	 */
	public <T extends AbstractNavigationNode> T initChild(T child) {
		child.setParent(this);
		children.add(child);
		return child;
	}

	/**
	 * Adds a child node to the list of children and sets this node as the
	 * child's parent. Also sets the child's title to the specified title. Returns the child.
	 * @param <T> the type of the child
	 * @param child the child to add
	 * @param title the title to set for the child
	 * @return the child
	 */
	public <T extends AbstractNavigationNode> T initChild(T child, String title) {
		child.setParent(this);
		child.setTitle(title);
		children.add(child);
		return child;
	}
	
	/**
	 * Creates a new {@link NavigationFolder} with the specified title, then adds
	 * the new folder to this folder and sets the new folder's parent to this.
	 * @param title the title of the new subfolder
	 * @return the new folder
	 */
	public NavigationFolder addNewSubfolder(String title) {
		NavigationFolder subfolder = initChild(new NavigationFolder());
		subfolder.setTitle(title);
		return subfolder;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.navigation.AbstractNavigationNode#visitLeafNodes(name.martingeisse.admin.navigation.INavigationLeafVisitor)
	 */
	@Override
	public void visitLeafNodes(INavigationLeafVisitor visitor) {
		for (AbstractNavigationNode child : children) {
			child.visitLeafNodes(visitor);
		}
	}
	
}
