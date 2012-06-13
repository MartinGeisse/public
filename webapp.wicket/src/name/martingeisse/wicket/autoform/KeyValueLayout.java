/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.autoform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.wicket.panel.simple.CheckBoxPanel;
import name.martingeisse.wicket.panel.simple.PasswordTextFieldPanel;
import name.martingeisse.wicket.panel.simple.TextFieldPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This container uses a table that contains keys in the left columns,
 * values in the right. It does not dictate what makes a "value";
 * an arbitrary Wicket component can be used (its Wicket id must be
 * obtained from this layout though). Keys are constrained to be labels.
 * 
 * If a markup ID prefix is specified, then each table row gets a
 * markup ID of the kind "prefix_itemId".
 */
public class KeyValueLayout extends Panel {

	/**
	 * the markupIdPrefix
	 */
	private String markupIdPrefix;

	/**
	 * the layoutItems
	 */
	private final List<LayoutItem> layoutItems;

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public KeyValueLayout(final String id) {
		super(id);
		this.layoutItems = new ArrayList<KeyValueLayout.LayoutItem>();
		add(new MyListView("rows", layoutItems, new PropertyModel<String>(this, "markupIdPrefix")));
	}

	/**
	 * Returns the Wicket:id to use for value components in this layout.
	 * @return the value component wicket:id.
	 */
	public static String getValueComponentId() {
		return "valueComponent";
	}

	/**
	 * Getter method for the markupIdPrefix.
	 * @return the markupIdPrefix
	 */
	public String getMarkupIdPrefix() {
		return markupIdPrefix;
	}

	/**
	 * Setter method for the markupIdPrefix.
	 * @param markupIdPrefix the markupIdPrefix to set
	 */
	public void setMarkupIdPrefix(final String markupIdPrefix) {
		this.markupIdPrefix = markupIdPrefix;
	}

	/**
	 * Adds a layout item. The key is displayed in the left column.
	 * The column is displayed in the right column and must use the wicket:id
	 * returned by getValueComponentId(), and not be added to another parent.
	 * @param itemId the item ID
	 * @param key the key text
	 * @param valueComponent the value component
	 */
	public void addItem(final String itemId, final String key, final Component valueComponent) {
		final LayoutItem layoutItem = new LayoutItem();
		layoutItem.itemId = itemId;
		layoutItem.key = key;
		layoutItem.valueComponent = valueComponent;
		layoutItems.add(layoutItem);
	}

	/**
	 * Adds a layout item. The key is displayed in the left column.
	 * The column is displayed in the right column and must use the wicket:id
	 * returned by getValueComponentId(), and not be added to another parent.
	 * 
	 * This method also allows a model for an error message to be specified. The error message,
	 * if present, is displayed next to the value component.
	 * 
	 * @param itemId the item ID
	 * @param key the key text
	 * @param valueComponent the value component
	 * @param errorMessageModel the model for the error message to show next to the item
	 */
	public void addItem(final String itemId, final String key, final Component valueComponent, final IModel<String> errorMessageModel) {
		final LayoutItem layoutItem = new LayoutItem();
		layoutItem.itemId = itemId;
		layoutItem.key = key;
		layoutItem.valueComponent = valueComponent;
		layoutItem.errorMessageModel = errorMessageModel;
		layoutItems.add(layoutItem);
	}

	/**
	 * Convenience method to add a read-only text field.
	 * @param <T> the model type
	 * @param itemId the item ID
	 * @param key the key label text
	 * @param valueModel the model that provides the displayed value
	 */
	public <T> void addReadOnlyTextField(final String itemId, final String key, final IModel<T> valueModel) {
		final TextFieldPanel<T> textFieldPanel = new TextFieldPanel<T>(getValueComponentId(), valueModel);
		textFieldPanel.getTextField().setEnabled(false);
		addItem(itemId, key, textFieldPanel);
	}

	/**
	 * NOTE: Model types not yet supported -- need to pass the class object for that as well
	 * Convenience method to add a text field.
	 * @param itemId the item ID
	 * @param key the key label text
	 * @param valueModel the model used by the text field
	 */
	public void addTextField(final String itemId, final String key, final IModel<String> valueModel) {
		addItem(itemId, key, new TextFieldPanel<String>(getValueComponentId(), valueModel));
	}

	/**
	 * Convenience method to add a password text field.
	 * @param itemId the item ID
	 * @param key the key label text
	 * @param valueModel the model used by the password text field
	 */
	public void addPasswordTextField(final String itemId, final String key, final IModel<String> valueModel) {
		addItem(itemId, key, new PasswordTextFieldPanel(getValueComponentId(), valueModel));
	}

	/**
	 * @param itemId the item ID
	 * @param key the key label text
	 * @param valueModel the model used by the check box
	 */
	public void addCheckBox(final String itemId, final String key, final IModel<Boolean> valueModel) {
		addItem(itemId, key, new CheckBoxPanel(getValueComponentId(), valueModel));
	}

	/**
	 * An item that is shown as a row in the key/value layout.
	 */
	private static class LayoutItem implements Serializable {

		/**
		 * the itemId
		 */
		public String itemId;

		/**
		 * the key
		 */
		public String key;

		/**
		 * the valueComponent
		 */
		public Component valueComponent;

		/**
		 * the optional errorMessageModel
		 */
		public IModel<String> errorMessageModel;

	}

	/**
	 * The ListView implementation used for the layout itself.
	 */
	private static class MyListView extends ListView<LayoutItem> {

		/**
		 * the markupIdPrefixModel
		 */
		private final IModel<String> markupIdPrefixModel;

		/**
		 * Constructor.
		 * @param id the wicket id
		 * @param items the list of items to show
		 * @param markupIdPrefixModel the model for the markup ID prefix
		 */
		public MyListView(final String id, final List<LayoutItem> items, final IModel<String> markupIdPrefixModel) {
			super(id, items);
			this.markupIdPrefixModel = markupIdPrefixModel;
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
		 */
		@Override
		protected void populateItem(final ListItem<LayoutItem> item) {
			final LayoutItem layoutItem = item.getModelObject();

			final String markupIdPrefix = markupIdPrefixModel.getObject();
			if (markupIdPrefix != null) {
				item.setOutputMarkupId(true);
				item.setMarkupId(markupIdPrefix + "_" + layoutItem.itemId);
			} else {
				item.setOutputMarkupId(false);
			}

			final WebMarkupContainer keyCell = new WebMarkupContainer("keyCell");
			item.add(keyCell);
			keyCell.add(new Label("keyLabel", layoutItem.key));

			final WebMarkupContainer valueCell = new WebMarkupContainer("valueCell");
			item.add(valueCell);
			valueCell.add(layoutItem.valueComponent);

			final WebMarkupContainer errorMessageCell = new WebMarkupContainer("errorMessageCell");
			item.add(errorMessageCell);
			if (layoutItem.errorMessageModel == null) {
				errorMessageCell.add(new Label("errorMessage"));
			} else {
				errorMessageCell.add(new Label("errorMessage", layoutItem.errorMessageModel));
			}

		}

	}
}
