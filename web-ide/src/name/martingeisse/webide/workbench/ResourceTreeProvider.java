/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.common.util.string.EmptyIterator;
import name.martingeisse.webide.resources.ResourceHandle;
import name.martingeisse.webide.resources.ResourcePath;

import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * {@link ITreeProvider} implementation for the resource tree.
 * 
 * The provider can either be rootless or not. Non-rootless providers
 * serve the workspace root as the tree root; rootless providers serve
 * the children of the workspace root as multiple roots.
 */
public class ResourceTreeProvider implements ITreeProvider<ResourceHandle> {

	/**
	 * the workspaceId
	 */
	private final long workspaceId;
	
	/**
	 * the rootless
	 */
	private final boolean rootless;

	/**
	 * Constructor.
	 * @param workspaceId the ID of the workspace to provide
	 * @param rootless whether this is a rootless provider (see class comment).
	 */
	public ResourceTreeProvider(long workspaceId, boolean rootless) {
		this.workspaceId = workspaceId;
		this.rootless = rootless;
	}

	/**
	 * Getter method for the workspaceId.
	 * @return the workspaceId
	 */
	public long getWorkspaceId() {
		return workspaceId;
	}
	
	/**
	 * Getter method for the rootless.
	 * @return the rootless
	 */
	public boolean isRootless() {
		return rootless;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#model(java.lang.Object)
	 */
	@Override
	public IModel<ResourceHandle> model(final ResourceHandle object) {
		return Model.of(object);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#getRoots()
	 */
	@Override
	public Iterator<? extends ResourceHandle> getRoots() {
		ResourceHandle root = new ResourceHandle(workspaceId, ResourcePath.ROOT);
		return rootless ? getChildren(root) : Arrays.asList(root).iterator();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(final ResourceHandle node) {
		return node.hasChildren();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Iterator<? extends ResourceHandle> getChildren(final ResourceHandle node) {
		List<? extends ResourceHandle> children = node.getChildrenList();
		return (children == null ? new EmptyIterator<ResourceHandle>() : children.iterator());
	}
	
}
