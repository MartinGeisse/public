/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list.populator;

import java.util.List;

import name.martingeisse.admin.entity.component.list.EntityInstanceDataProvider;
import name.martingeisse.admin.entity.component.list.EntityListPanelPage;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.util.IGetPageable;
import name.martingeisse.wicket.util.zebra.ZebraDataGridView;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Populator-based presentation of entities.
 * 
 * Note that this class cannot be directly used in combination with {@link EntityListPanelPage}.
 * Instead, a subclass must be created that passes the populators to the constructor of this class. 
 */
public class PopulatorBasedEntityListPanel extends Panel implements IGetPageable {

	/**
	 * the cellPopulators
	 */
	private transient List<IEntityCellPopulator> cellPopulators;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entityModel the entity descriptor model
	 * @param cellPopulators the cell populators that do the actual presentation
	 */
	public PopulatorBasedEntityListPanel(final String id, final IModel<EntityDescriptor> entityModel, final List<IEntityCellPopulator> cellPopulators) {
		super(id);
		setDefaultModel(entityModel);
		this.cellPopulators = cellPopulators;
	}

	/**
	 * Getter method for the cellPopulators.
	 * @return the cellPopulators
	 */
	public List<IEntityCellPopulator> getCellPopulators() {
		return cellPopulators;
	}

	/**
	 * Setter method for the cellPopulators.
	 * @param cellPopulators the cellPopulators to set
	 */
	public void setCellPopulators(final List<IEntityCellPopulator> cellPopulators) {
		this.cellPopulators = cellPopulators;
	}

	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public int getWidth() {
		return cellPopulators.size();
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

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.util.IGetPageable#getPageable()
	 */
	@Override
	public IPageable getPageable() {
		return (IPageable)get("rows");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new ListView<IEntityCellPopulator>("headers", cellPopulators) {
			@Override
			protected void populateItem(final ListItem<IEntityCellPopulator> item) {
				item.add(new Label("name", item.getModelObject().getTitle()));
			}
		});
		add(new ZebraDataGridView<EntityInstance>("rows", cellPopulators, new EntityInstanceDataProvider(getEntityDescriptorModel())));
	}

}
