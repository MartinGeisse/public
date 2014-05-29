/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

/**
 * This class must be able to generate local tree node IDs. See {@link JsTree}
 * for a detailed explanation of local and global IDs. Basically, this interface
 * is used to generate an ID string per tree node that is unique among the
 * node's siblings.
 * 
 * This interface should also produce the same ID every time it is used for
 * the "same" node, whatever that means for a specific node type. This is
 * used to find nodes in AJAX callbacks and during "soft" refreshing.
 *
 * @param <T> the node type
 */
public interface ITreeNodeIdProvider<T> {

	/**
	 * Returns the local node ID for the specified node.
	 * @param node the node
	 * @return the local node ID
	 */
	public String getLocalId(T node);

	/**
	 * This provider generates an ID from the identity hash of the node object.
	 */
	public static class DefaultProvider implements ITreeNodeIdProvider<Object> {
		
		/* (non-Javadoc)
		 * @see name.martingeisse.wicket.component.tree.ITreeNodeIdProvider#getLocalId(java.lang.Object)
		 */
		@Override
		public String getLocalId(Object node) {
			return Integer.toString(System.identityHashCode(node));
		}
		
	}
	
	/**
	 * The shared instance of the default provider type.
	 */
	public static final DefaultProvider DEFAULT = new DefaultProvider();
	
}
