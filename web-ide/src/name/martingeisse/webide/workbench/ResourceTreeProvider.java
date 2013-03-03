/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.workbench;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.common.util.string.EmptyIterator;
import name.martingeisse.webide.resources.ResourcePath;
import name.martingeisse.webide.resources.Workspace;

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
public class ResourceTreeProvider implements ITreeProvider<ResourcePath> {

	/**
	 * the rootless
	 */
	private final boolean rootless;

	/**
	 * Constructor.
	 * @param rootless whether this is a rootless provider (see class comment).
	 */
	public ResourceTreeProvider(boolean rootless) {
		this.rootless = rootless;
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
	public IModel<ResourcePath> model(final ResourcePath object) {
		return Model.of(object);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#getRoots()
	 */
	@Override
	public Iterator<? extends ResourcePath> getRoots() {
		return rootless ? getChildren(ResourcePath.ROOT) : Arrays.asList(ResourcePath.ROOT).iterator();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(final ResourcePath node) {
		File resource = Workspace.map(node);
		if (!resource.isDirectory()) {
			return false;
		}
		return (resource.list().length > 0);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Iterator<? extends ResourcePath> getChildren(final ResourcePath node) {
		File resource = Workspace.map(node);
		if (!resource.isDirectory()) {
			return new EmptyIterator<ResourcePath>();
		}
		File[] children = resource.listFiles();
		List<ResourcePath> childrenPaths = new ArrayList<ResourcePath>();
		for (File child : children) {
			childrenPaths.add(Workspace.map(child));
		}
		return childrenPaths.iterator();
	}
	
}
