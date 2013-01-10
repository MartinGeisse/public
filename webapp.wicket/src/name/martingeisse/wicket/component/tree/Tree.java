/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.tree;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.model.IModel;

/**
 * Basic tree functionality. This class doesn't have markup;
 * rather, a tree is attached to markup that can be nested
 * recursively and contains a nesting repeater with a wicket:id
 * like this:
 * 
 * <div wicket:id="tree">
 *     ... other stuff ...
 *     <div wicket:id="children" />
 *     ... other stuff ...
 * </div>
 *
 * The expected wicket:id of the nesting repeater is "children"
 * by default but subclasses can override this.
 * 
 * @param <T> the underlying node type
 */
public abstract class Tree<T> extends AbstractRepeater {

	/**
	 * the treeProvider
	 */
	private final ITreeProvider<T> treeProvider;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param treeProvider the tree provider
	 */
	public Tree(String id, ITreeProvider<T> treeProvider) {
		super(id);
		this.treeProvider = treeProvider;
	}

	/**
	 * Getter method for the treeProvider.
	 * @return the treeProvider
	 */
	public ITreeProvider<T> getTreeProvider() {
		return treeProvider;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#onPopulate()
	 */
	@Override
	protected void onPopulate() {
		removeAll();
		createItems(this, treeProvider.getRoots());
	}
	
	/**
	 * Creates items for the specified nodes.
	 */
	private void createItems(WebMarkupContainer parent, Iterator<? extends T> nodeIterator) {
		while (nodeIterator.hasNext()) {
			createItems(parent, nodeIterator.next());
		}
	}

	
	/**
	 * Creates items for the specified node and its subnodes.
	 */
	private void createItems(WebMarkupContainer parent, T node) {
		TreeItem item = new TreeItem(id, treeProvider.model(node));
		parent.add(item);
		populateItem(item);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#renderIterator()
	 */
	@Override
	protected Iterator<? extends Component> renderIterator() {
		return iterator();
	}

	/**
	 * Returns the wicket:id of the repeater for child nodes.
	 * @return the children id
	 */
	public String getChildrenRepeaterId() {
		return "children";
	}

	/**
	 * Populates an item with components. Note that the children repeater
	 * is added outside this method.
	 * @param item the item to populate
	 */
	protected abstract void populateItem(TreeItem item);
	
}
