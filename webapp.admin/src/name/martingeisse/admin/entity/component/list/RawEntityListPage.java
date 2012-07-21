/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list;

import java.sql.SQLException;

import name.martingeisse.admin.entity.multi.EntityInstanceDataProvider;
import name.martingeisse.admin.entity.multi.RawEntityListPanel;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.single.EntityInstance;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer;
import name.martingeisse.admin.readonly.ReadOnlyRenderingConfigurationUtil;
import name.martingeisse.admin.util.LinkUtil;
import name.martingeisse.common.jdbc.ResultSetReader;
import name.martingeisse.wicket.util.zebra.ZebraDataView;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * This page displays the publicly visible fields of an entity --
 * as defined by {@link EntityDescriptor#getRawEntityListFieldOrder()} --
 * in a table.
 */
public class RawEntityListPage extends AbstractEntityListPage {

	/**
	 * the renderers
	 */
	private transient IPropertyReadOnlyRenderer[] renderers;
	
	/**
	 * Constructor.
	 * @param parameters the page parameters
	 */
	public RawEntityListPage(PageParameters parameters) {
		super(parameters);
	}

	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public int getWidth() {
		// cannot return renderers.length, otherwise we depend on the order in which child components get their onBeforeRender() called
		return getEntity().getRawEntityListFieldOrder().length;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.admin.entity.component.list.AbstractEntityListPage#getPageable()
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
		getMainContainer().add(new Loop("headers", new PropertyModel<Integer>(RawEntityListPage.this, "width")) {
			@Override
			protected void populateItem(LoopItem item) {
				item.add(new Label("name", getEntity().getRawEntityListFieldOrder()[item.getIndex()]));
			}
		});
		getMainContainer().add(new ZebraDataView<EntityInstance>("rows", new MyDataProvider(), 30) {
			@Override
			protected void populateItem(final Item<EntityInstance> rowItem) {
				rowItem.add(new Loop("cells", new PropertyModel<Integer>(RawEntityListPage.this, "width")) {
					@Override
					protected void populateItem(final LoopItem cellItem) {
						EntityDescriptor entity = getEntity();
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
	 * {@link RawEntityListPanel} since that would cause an infinite
	 * loop on detach(). We also need the common logic from {@link EntityInstanceDataProvider}.
	 */
	private class MyDataProvider extends EntityInstanceDataProvider {

		/**
		 * Constructor.
		 */
		public MyDataProvider() {
			super(new PropertyModel<EntityDescriptor>(RawEntityListPage.this, "entity"));
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
				renderers[i] = ReadOnlyRenderingConfigurationUtil.createPropertyReadOnlyRenderer(reader.getSqlFieldType(i));
				if (renderers[i] == null) {
					throw new RuntimeException("no renderer");
				}
			}
			
		}
		
	}
	
}
