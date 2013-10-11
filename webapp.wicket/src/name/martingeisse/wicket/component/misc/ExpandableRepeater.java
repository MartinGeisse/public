/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import java.util.Iterator;
import java.util.List;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;
import name.martingeisse.wicket.util.AjaxRequestUtil;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Repeater subclass that initially shows a list of items.
 * The list can later be expanded by loading additional items, possibly
 * multiple times. The component stores as component state how many
 * times new items were loaded, but not the item lists themselves,
 * so while stateful it is still pretty lightweight.
 * 
 * Loading items is centered around *batches*. The list first shows
 * the initial batch (index 0). Each time the list is expanded,
 * another batch is added. Then, either the whole list is rendered,
 * or just the newly added batch (AJAX only). Rendering takes
 * the model and the number of visible batches as input, but the
 * batch contents (including the number of items in each batch) must
 * be determined by the subclass when the list view is rendered.
 * 
 * Rendering only the newly generated items in an AJAX expansion
 * requires knowledge about the place these items should go to, and
 * any HTML element name that is valid for that place. This cannot
 * currently be determined automatically from this repeater's markup
 * in all cases. Automatic detection works when the initial list of
 * items contains at least one item, by taking the necessary information
 * from that item. If the initial list (i.e. the first batch) can
 * be empty, use {@link #notifyAboutPrototypeItem(String, String)}
 * to tell the repeater how newly rendered items should be handled.
 * 
 * @param <T> the model type
 * @param <E> the element type
 */
public abstract class ExpandableRepeater<T, E> extends AbstractRepeater {

	/**
	 * the batchCount
	 */
	private int batchCount;

	/**
	 * the currentList
	 */
	private transient List<E> currentList;

	/**
	 * the validElementType
	 */
	private String validElementType;

	/**
	 * the initialItemMarkupId
	 */
	private String initialItemMarkupId;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public ExpandableRepeater(final String id, final IModel<T> model) {
		super(id, model);
		init();
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public ExpandableRepeater(final String id) {
		super(id);
		init();
	}

	/**
	 * 
	 */
	private void init() {
		this.batchCount = 1;
		setOutputMarkupId(true);
	}

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	@SuppressWarnings("unchecked")
	public final IModel<T> getModel() {
		return (IModel<T>)getDefaultModel();
	}

	/**
	 * Setter method for the model.
	 * @param model the model
	 */
	public final void setModel(final IModel<T> model) {
		setDefaultModel(model);
	}

	/**
	 * Getter method for the batchCount.
	 * @return the batchCount
	 */
	public final int getBatchCount() {
		return batchCount;
	}

	/**
	 * Setter method for the batchCount.
	 * @param batchCount the batchCount to set
	 */
	public final void setBatchCount(final int batchCount) {
		this.batchCount = batchCount;
	}

	/**
	 * 
	 */
	private static String internalGetItemId(final int major, final int minor) {
		if (major < 0 || major >= 1000000 || minor < 0 || minor >= 1000000) {
			throw new IllegalArgumentException("getDefaultId() invalid arguments: " + major + ", " + minor);
		}
		return String.format("%06d", major) + String.format("%06d", minor);
	}

	/**
	 * Expands the number of visible batches by one.
	 */
	public final void expandByOneBatch() {

		// Increment the batch count -- this will already cause a full render to consider the new batch.
		batchCount++;

		// In non-AJAX calls, that's it. In AJAX calls, we'll have to render an on-the-fly update.
		final AjaxRequestTarget target = AjaxRequestUtil.getAjaxRequestTarget();
		if (target == null) {
			return;
		}

		// build items
		final int batchIndex = batchCount - 1;
		final List<E> batch = loadBatch(batchIndex);
		final int expansionSize = batch.size();
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("var newItemMarkupIds = [");
		boolean first = true;
		for (int localIndex = 0; localIndex < expansionSize; localIndex++) {
			final IModel<E> listItemModel = new PropertyModel<E>(batch, Integer.toString(localIndex));
			final Item<E> listItem = newItem(internalGetItemId(batchIndex, localIndex), listItemModel);
			add(listItem);
			populateItem(listItem);
			if (first) {
				first = false;
			} else {
				stringBuilder.append(',');
			}
			JavascriptAssemblerUtil.appendStringLiteral(stringBuilder, listItem.getMarkupId());
			target.add(listItem);
		}

		// build append script
		stringBuilder.append("];\n");
		stringBuilder.append("var $parent = $('#").append(initialItemMarkupId).append("').parent();");
		stringBuilder.append("for (var i in newItemMarkupIds) {\n");
		stringBuilder.append("  var item = document.createElement('").append(validElementType).append("'); ");
		stringBuilder.append("  item.id = newItemMarkupIds[i];");
		stringBuilder.append("  $parent.append(item);");
		stringBuilder.append("}\n");
		target.prependJavaScript(stringBuilder);

	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#getStatelessHint()
	 */
	@Override
	protected boolean getStatelessHint() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		this.currentList = loadBatches(batchCount);
		super.onBeforeRender();
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#onPopulate()
	 */
	@Override
	protected final void onPopulate() {
		removeAll();
		final int size = currentList.size();
		for (int index = 0; index < size; index++) {
			final IModel<E> model = new PropertyModel<E>(currentList, Integer.toString(index));
			final Item<E> item = newItem(internalGetItemId(0, index), model);
			add(item);
			populateItem(item);
		}
	}

	/**
	 * Creates a new item.
	 * @param id the wicket id
	 * @param itemModel the model
	 * @return the item
	 */
	protected Item<E> newItem(final String id, final IModel<E> itemModel) {
		return new Item<E>(id, itemModel);
	}

	/**
	 * Populates an item with sub-components.
	 * @param item the item
	 */
	protected abstract void populateItem(final Item<E> item);

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#renderIterator()
	 */
	@Override
	protected final Iterator<? extends Component> renderIterator() {
		return iterator();
	}

	/**
	 * Loads all visible batches.
	 * 
	 * @param batchCount the number of visible batches
	 * @return a list containing the items from all those batches
	 */
	protected abstract List<E> loadBatches(int batchCount);

	/**
	 * Loads a single batch.
	 * 
	 * @param batchIndex the index of the batch to load
	 * @return a list containing the items from that batch
	 */
	protected abstract List<E> loadBatch(int batchIndex);

	/**
	 * @param validElementType an element type that is valid for items
	 * @param initialItemMarkupId the markup ID of an initially rendered item
	 * or an item-like placeholder at the place where items should go
	 */
	public final void notifyAboutPrototypeItem(final String validElementType, final String initialItemMarkupId) {
		this.validElementType = validElementType;
		this.initialItemMarkupId = initialItemMarkupId;
	}

	/**
	 * @param item the rendered item
	 */
	protected final void notifyAboutPrototypeItem(final Component item) {
		final ComponentTag componentTag = (ComponentTag)item.getMarkup().get(0);
		notifyAboutPrototypeItem(componentTag.getName(), item.getMarkupId());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.repeater.AbstractRepeater#renderChild(org.apache.wicket.Component)
	 */
	@Override
	protected void renderChild(final Component child) {
		super.renderChild(child);
		notifyAboutPrototypeItem(child);
	}

	/**
	 * The item class used in the {@link ExpandableRepeater}. This class is
	 * special in that it doesn't use an index, since indices cannot be maintained
	 * at reasonable cost when doing an expansion.
	 * 
	 * @param <E> the element model type
	 */
	public static class Item<E> extends AbstractItem {

		/**
		 * Constructor.
		 * @param id the wicket id
		 */
		public Item(final String id) {
			super(id);
			setOutputMarkupId(true);
		}

		/**
		 * Constructor.
		 * @param id the wicket id
		 * @param model the model
		 */
		public Item(final String id, final IModel<E> model) {
			super(id, model);
			setOutputMarkupId(true);
		}

		/**
		 * Getter method for the model.
		 * @return the model
		 */
		@SuppressWarnings("unchecked")
		public final IModel<E> getModel() {
			return (IModel<E>)getDefaultModel();
		}

		/**
		 * Setter method for the model.
		 * @param model the model
		 */
		public final void setModel(final IModel<E> model) {
			setDefaultModel(model);
		}

		/**
		 * Getter method for the model object.
		 * @return the model object
		 */
		@SuppressWarnings("unchecked")
		public final E getModelObject() {
			return (E)getDefaultModelObject();
		}

	}
}
