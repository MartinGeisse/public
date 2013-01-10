/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.experiment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 *
 */
public class MyTreeProvider implements ITreeProvider<Integer> {

	/**
	 * the roots
	 */
	private List<Integer> roots;

	/**
	 * Constructor.
	 */
	public MyTreeProvider(Integer... roots) {
		this(Arrays.asList(roots));
	}

	/**
	 * 
	 */
	public MyTreeProvider(List<Integer> roots) {
		this.roots = roots;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#getRoots()
	 */
	@Override
	public Iterator<? extends Integer> getRoots() {
		return roots.iterator();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(Integer node) {
		return getChildren(node).hasNext();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Iterator<? extends Integer> getChildren(Integer node) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = node - 1; i > 1; i--) {
			if (node / i * i == node) {
				result.add(i);
			}
		}
		return result.iterator();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.tree.ITreeProvider#model(java.lang.Object)
	 */
	@Override
	public IModel<Integer> model(Integer object) {
		return Model.of(object);
	}

}
