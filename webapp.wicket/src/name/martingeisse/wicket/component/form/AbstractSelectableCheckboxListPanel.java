/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.form;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * A panel that contains a list of DIVs, each with a checkbox and name.
 * The DIVs are single-selectable and trigger an AJAX event on selection,
 * typically used to update other components.
 * 
 * The model for this component is of type List<T> and should provide
 * entries to populate the list.
 * 
 * @param <T> the element type
 */
public abstract class AbstractSelectableCheckboxListPanel<T> extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public AbstractSelectableCheckboxListPanel(String id) {
		super(id);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entryList the list of entries
	 */
	public AbstractSelectableCheckboxListPanel(String id, List<T> entryList) {
		super(id, Model.of(entryList));
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entryListModel the model for the list of entries
	 */
	public AbstractSelectableCheckboxListPanel(String id, IModel<List<T>> entryListModel) {
		super(id, entryListModel);
	}

	/**
	 * Getter method for the model.
	 * @return the model
	 */
	@SuppressWarnings("unchecked")
	public IModel<List<T>> getModel() {
		return (IModel<List<T>>)getDefaultModel();
	}

	/**
	 * Setter method for the model.
	 * @param model the model to set
	 */
	public void setModel(IModel<List<T>> model) {
		setDefaultModel(model);
	}
	
	/**
	 * Creates a model for the name of an entry, using the model for the entry itself.
	 * The returned model is passed to a {@link Label}.
	 * 
	 * @param entryModel the entry model
	 * @return the name model
	 */
	protected abstract IModel<?> createNameModel(IModel<T> entryModel);

	/**
	 * This method is invoked from AJAX request handling when an entry has
	 * been selected by the user.
	 * @param entry the selected entry
	 */
	protected void onEntrySelected(T entry) {
	}
	
	/**
	 * Returns a new map containing the selection status of all elements.
	 * @return the selection status
	 */
	public Map<T, Boolean> copySelectionStatus() {
		throw new NotImplementedException("");
	}
	
}
