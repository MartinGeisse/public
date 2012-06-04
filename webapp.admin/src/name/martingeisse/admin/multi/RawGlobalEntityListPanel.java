/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer;
import name.martingeisse.admin.schema.AbstractDatabaseDescriptor;
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
public class RawGlobalEntityListPanel extends Panel {

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
	public RawGlobalEntityListPanel(final String id, final EntityDescriptor entity, final PageParameters parameters) {
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
		add(new Loop("headers", new PropertyModel<Integer>(RawGlobalEntityListPanel.this, "width")) {
			@Override
			protected void populateItem(LoopItem item) {
				item.add(new Label("name", getEntity().getRawEntityListFieldOrder()[item.getIndex()]));
			}
		});
		add(new DataView<EntityInstance>("rows", new MyDataProvider(), 30) {
			@Override
			protected void populateItem(final Item<EntityInstance> rowItem) {
				rowItem.add(new Loop("cells", new PropertyModel<Integer>(RawGlobalEntityListPanel.this, "width")) {
					@Override
					protected void populateItem(final LoopItem cellItem) {
						EntityDescriptor entity = (EntityDescriptor)RawGlobalEntityListPanel.this.getDefaultModelObject();
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
	 * {@link RawGlobalEntityListPanel} since that would cause an infinite
	 * loop on detach().
	 */
	private class MyDataProvider implements IDataProvider<EntityInstance> {

		/* (non-Javadoc)
		 * @see org.apache.wicket.model.IDetachable#detach()
		 */
		@Override
		public void detach() {
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.repeater.data.IDataProvider#size()
		 */
		@Override
		public int size() {
			Connection connection = null;
			try {
				final EntityDescriptor entity = (EntityDescriptor)getDefaultModelObject();
				final AbstractDatabaseDescriptor database = entity.getDatabase();
				connection = database.createConnection();
				final Statement statement = connection.createStatement();
				int size = database.fetchTableSize(statement, entity.getTableName());
				statement.close();
				return size;
			} catch (final SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (final SQLException e) {
				}
			}
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.repeater.data.IDataProvider#iterator(int, int)
		 */
		@Override
		public Iterator<? extends EntityInstance> iterator(int first, int count) {
			Connection connection = null;
			try {
				final EntityDescriptor entity = (EntityDescriptor)getDefaultModelObject();
				final AbstractDatabaseDescriptor database = entity.getDatabase();
				connection = database.createConnection();
				final Statement statement = connection.createStatement();

				// send the query to the database and analyze result meta-data
				String query;
				{
					char b = database.getIdentifierBeginQuoteCharacter();
					char e = database.getIdentifierEndQuoteCharacter();
					String orderClause = database.getDefaultOrderClause();
					query = "SELECT * FROM " + b + entity.getTableName() + e + orderClause + " LIMIT " + count + " OFFSET " + first;
				}
				final ResultSet resultSet = statement.executeQuery(query);
				ResultSetReader reader = new ResultSetReader(resultSet, entity.getIdColumnName(), entity.getRawEntityListFieldOrder());
				int width = reader.getWidth();
				
				// fetch data and fill the rows array
				List<EntityInstance> rows = new ArrayList<EntityInstance>();
				String[] fieldNames = EntityInstance.getFieldNames(entity, resultSet);
				while (reader.next()) {
					rows.add(new EntityInstance(entity, reader.getId(), fieldNames, reader.getRow()));
				}
				
				// determine the column names and renderers
				renderers = new IPropertyReadOnlyRenderer[width];
				for (int i=0; i<width; i++) {
					renderers[i] = ApplicationConfiguration.getCapabilities().createPropertyReadOnlyRenderer(reader.getSqlFieldType(i));
					if (renderers[i] == null) {
						throw new RuntimeException("no renderer");
					}
				}

				// clean up
				resultSet.close();
				statement.close();
				return rows.iterator();

			} catch (final SQLException e) {
				throw new RuntimeException(e);
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (final SQLException e) {
				}
			}
		}

		/* (non-Javadoc)
		 * @see org.apache.wicket.markup.repeater.data.IDataProvider#model(java.lang.Object)
		 */
		@Override
		public IModel<EntityInstance> model(EntityInstance object) {
			return Model.of(object);
		}
		
	}
	
}
