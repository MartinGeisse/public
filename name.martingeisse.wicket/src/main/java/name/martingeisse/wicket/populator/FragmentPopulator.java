/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.populator;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

/**
 * A cell populator that is based on a Wicket fragment.
 * This populator is useful when mixing "real" cell populators
 * with custom markup in a single populator-based list.
 * @param <T> the row type
 */
public class FragmentPopulator<T> implements ICellPopulator<T> {

	/**
	 * the markupProvider
	 */
	private final MarkupContainer markupProvider;

	/**
	 * the fragmentMarkupId
	 */
	private final String fragmentMarkupId;

	/**
	 * Constructor.
	 * @param markupProvider the markup container that provides the markup for the fragment
	 * @param fragmentMarkupId the wicket:id of the fragment in the markupProvider
	 */
	public FragmentPopulator(final MarkupContainer markupProvider, final String fragmentMarkupId) {
		this.markupProvider = markupProvider;
		this.fragmentMarkupId = fragmentMarkupId;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.IDetachable#detach()
	 */
	@Override
	public void detach() {
	}


	/* (non-Javadoc)
	 * @see org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator#populateItem(org.apache.wicket.markup.repeater.Item, java.lang.String, org.apache.wicket.model.IModel)
	 */
	@Override
	public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
		final Fragment fragment = new Fragment(componentId, fragmentMarkupId, markupProvider, rowModel);
		cellItem.add(fragment);
		populateFragment(fragment, rowModel);
	}

	/**
	 * This method can be used by subclasses to add components to the fragment.
	 * @param fragment the fragment
	 * @param rowModel the row model (also the model of the fragment)
	 */
	protected void populateFragment(final Fragment fragment, final IModel<T> rowModel) {
	}
	
}
