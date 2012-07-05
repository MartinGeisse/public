/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.multi;

import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.single.EntityInstance;
import name.martingeisse.wicket.util.zebra.ZebraDataView;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Base class that uses a {@link DataView} to show a list of entity instances.
 * Concrete subclasses provide markup and populate the items of the data view.
 */
public abstract class AbstractEntityDataViewPanel extends Panel {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entity the entity
	 * @param parameters the page parameters
	 */
	public AbstractEntityDataViewPanel(final String id, final EntityDescriptor entity, final PageParameters parameters) {
		super(id);
		setDefaultModel(Model.of(entity));
	}
	
	/**
	 * Getter method for the entityDescriptorModel.
	 * @return the entityDescriptorModel
	 */
	@SuppressWarnings("unchecked")
	public IModel<EntityDescriptor> getEntityDescriptorModel() {
		return (IModel<EntityDescriptor>)getDefaultModel();
	}
	
	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return (EntityDescriptor)getDefaultModelObject();
	}
	
	/**
	 * Getter method for the pageable.
	 * @return the pageable
	 */
	public IPageable getPageable() {
		return (IPageable)get("rows");
	}
	
	/**
	 * Creates the {@link IDataProvider} for the {@link DataView}. The default
	 * implementation creates an {@link EntityInstanceDataProvider}. 
	 * @return the data provider
	 */
	protected IDataProvider<EntityInstance> createDataProvider() {
		return new EntityInstanceDataProvider(getEntityDescriptorModel());
	}
	
	/**
	 * Creates an {@link AbstractRepeater} component that renders the table headers.
	 * @param id the wicket id
	 * @return the repeater
	 */
	protected abstract AbstractRepeater createHeaderRepeater(String id);

	/**
	 * Populates a table row item with components.
	 * @param rowItem the row item to populate
	 */
	protected abstract void populateRowItem(final Item<EntityInstance> rowItem);
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(createHeaderRepeater("headers"));
		add(new ZebraDataView<EntityInstance>("rows", createDataProvider(), 30) {
			@Override
			protected void populateItem(final Item<EntityInstance> rowItem) {
				AbstractEntityDataViewPanel.this.populateRowItem(rowItem);
			}
		});
	}

}
