/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi;

import java.sql.SQLException;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer;
import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.single.EntityInstance;
import name.martingeisse.admin.util.LinkUtil;
import name.martingeisse.common.jdbc.ResultSetReader;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Raw presentation of entities.
 */
public class MultiCellPanel extends Panel {

	/**
	 * the renderers
	 */
	private transient IPropertyReadOnlyRenderer[] renderers;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entity the entity
	 * @param parameters the page parameters
	 */
	public MultiCellPanel(final String id, final EntityDescriptor entity, final PageParameters parameters) {
		super(id);
		setDefaultModel(Model.of(entity));
	}

	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public int getWidth() {
		// cannot return renderers.length, otherwise we depend on the order in which child components get their onBeforeRender() called
		return getEntity().getRawEntityListFieldOrder().length;
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

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new Loop("headers", new PropertyModel<Integer>(MultiCellPanel.this, "width")) {
			@Override
			protected void populateItem(LoopItem item) {
				item.add(new Label("name", getEntity().getRawEntityListFieldOrder()[item.getIndex()]));
			}
		});
		add(new DataView<EntityInstance>("rows", new MyDataProvider(), 30) {
			@Override
			protected void populateItem(final Item<EntityInstance> rowItem) {
				rowItem.add(new Loop("cells", new PropertyModel<Integer>(MultiCellPanel.this, "width")) {
					@Override
					protected void populateItem(final LoopItem cellItem) {
						EntityDescriptor entity = (EntityDescriptor)MultiCellPanel.this.getDefaultModelObject();
						EntityInstance instance = rowItem.getModelObject();
						AbstractLink link;
						if (instance.getId() == null) {
							link = LinkUtil.createDisabledLink("link");
						} else {
							link = LinkUtil.createSingleEntityLink("link", entity, instance.getId());
						}
						link.add(renderers[cellItem.getIndex()].createLabel("value", instance.getFieldValues()[cellItem.getIndex()]));
						cellItem.add(link);
					}
				});
			}
		});
	}

	/**
	 * Custom {@link IDataProvider} implementation -- cannot be implemented by
	 * {@link MultiCellPanel} since that would cause an infinite
	 * loop on detach(). We also need the common logic from {@link EntityInstanceDataProvider}.
	 */
	private class MyDataProvider extends EntityInstanceDataProvider {

		/**
		 * Constructor.
		 */
		public MyDataProvider() {
			super(MultiCellPanel.this.getEntityDescriptorModel());
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.admin.multi.EntityInstanceDataProvider#onResultAvailable(name.martingeisse.common.jdbc.ResultSetReader)
		 */
		@Override
		protected void onResultAvailable(ResultSetReader reader) throws SQLException {

			// determine the column names and renderers
			int width = reader.getWidth();
			renderers = new IPropertyReadOnlyRenderer[width];
			for (int i=0; i<width; i++) {
				renderers[i] = ApplicationConfiguration.getCapabilities().createPropertyReadOnlyRenderer(reader.getSqlFieldType(i));
				if (renderers[i] == null) {
					throw new RuntimeException("no renderer");
				}
			}
			
		}
		
	}
	
}
