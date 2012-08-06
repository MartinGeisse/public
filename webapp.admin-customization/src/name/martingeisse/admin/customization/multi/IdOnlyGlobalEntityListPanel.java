/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.customization.multi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.entity.model.EntityDescriptorModel;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Id-only presentation of entities.
 */
public class IdOnlyGlobalEntityListPanel extends Panel implements IPageable {
	
	/**
	 * the ids
	 */
	private transient List<Object> ids;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entity the entity
	 * @param parameters the page parameters
	 */
	public IdOnlyGlobalEntityListPanel(final String id, final EntityDescriptor entity, final PageParameters parameters) {
		super(id);
		setDefaultModel(new EntityDescriptorModel(entity));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getCurrentPage()
	 */
	@Override
	public int getCurrentPage() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#setCurrentPage(int)
	 */
	@Override
	public void setCurrentPage(int page) {
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getPageCount()
	 */
	@Override
	public int getPageCount() {
		return 1;
	}

	/**
	 * Getter method for the ids.
	 * @return the ids
	 */
	public List<Object> getIds() {
		return ids;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onInitialize()
	 */
	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new ListView<Object>("entries", new PropertyModel<List<Object>>(this, "ids")) {
			@Override
			protected void populateItem(ListItem<Object> item) {
				item.add(new Label("id", item.getModelObject().toString()));
			}
		});
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Page#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {

		// fetch data
		Connection connection = null;
		try {
			final EntityDescriptor entity = (EntityDescriptor)getDefaultModelObject();
			final AbstractDatabaseDescriptor database = entity.getDatabase();
			connection = database.createConnection();
			final Statement statement = connection.createStatement();

			// send the query to the database and analyze result meta-data
			final ResultSet resultSet = statement.executeQuery("SELECT id FROM \"" + entity.getTableName() + "\" ORDER BY id");
			
			// fetch data and fill the rows array
			ids = new ArrayList<Object>();
			while (resultSet.next()) {
				ids.add(resultSet.getObject(1));
			}

			// clean up
			resultSet.close();
			statement.close();

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
		
		// only initialize children after the table size have been fetched
		super.onBeforeRender();
		
	}
	
}
