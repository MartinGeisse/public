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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer;
import name.martingeisse.admin.schema.DatabaseDescriptor;
import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.schema.EntityPropertyDescriptor;
import name.martingeisse.admin.util.LinkUtil;
import name.martingeisse.common.jdbc.JdbcUtil;
import name.martingeisse.common.jdbc.ResultSetReader;
import name.martingeisse.wicket.util.ZebraLoop;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Raw presentation of entities. TODO: This panel will supersede {@link RawGlobalEntityListPanel}.
 */
public class RawGlobalEntityListPanel extends Panel implements IPageable {

	/**
	 * the ROWS_PER_PAGE
	 */
	private static final int ROWS_PER_PAGE = 30;
	
	/**
	 * the currentPage
	 */
	private int currentPage;

	/**
	 * the pageCount
	 */
	private transient int pageCount;
	
	/**
	 * the width
	 */
	private transient int width;
	
	/**
	 * the columnNames
	 */
	private transient String[] columnNames;
	
	/**
	 * the renderers
	 */
	private transient IPropertyReadOnlyRenderer[] renderers;

	/**
	 * the rowIds
	 */
	private transient Object[] rowIds;
	
	/**
	 * the rows
	 */
	private transient Object[][] rows;
	
	/**
	 * the visibleRows
	 */
	private transient int visibleRows;

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param entity the entity
	 * @param parameters the page parameters
	 */
	public RawGlobalEntityListPanel(final String id, final EntityDescriptor entity, final PageParameters parameters) {
		super(id);
		currentPage = 0;
		setDefaultModel(Model.of(entity));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getCurrentPage()
	 */
	@Override
	public int getCurrentPage() {
		return currentPage;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#setCurrentPage(int)
	 */
	@Override
	public void setCurrentPage(int page) {
		this.currentPage = page;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getPageCount()
	 */
	@Override
	public int getPageCount() {
		return pageCount;
	}
	
	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Getter method for the rows.
	 * @return the rows
	 */
	public Object[][] getRows() {
		return rows;
	}
	
	/**
	 * Getter method for the visibleRows.
	 * @return the visibleRows
	 */
	public int getVisibleRows() {
		return visibleRows;
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
				item.add(new Label("name", columnNames[item.getIndex()]));
			}
		});
		add(new ZebraLoop("rows", new PropertyModel<Integer>(RawGlobalEntityListPanel.this, "visibleRows")) {
			@Override
			protected void populateItem(final LoopItem rowItem) {
				rowItem.add(new Loop("cells", new PropertyModel<Integer>(RawGlobalEntityListPanel.this, "width")) {
					@Override
					protected void populateItem(final LoopItem cellItem) {
						EntityDescriptor entity = (EntityDescriptor)RawGlobalEntityListPanel.this.getDefaultModelObject();
						AbstractLink link = LinkUtil.createSingleEntityLink("link", entity, rowIds[rowItem.getIndex()]);
						link.add(renderers[cellItem.getIndex()].createLabel("value", rows[rowItem.getIndex()][cellItem.getIndex()]));
						cellItem.add(link);
					}
				});
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
			final DatabaseDescriptor database = entity.getDatabase();
			connection = database.createConnection();
			final Statement statement = connection.createStatement();

			// fetch table size for pagination
			int rowCount = JdbcUtil.fetchTableSize(statement, entity.getTableName());
			pageCount = (rowCount + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
			
			// determine visible fields and their order
			List<EntityPropertyDescriptor> fieldOrder = new ArrayList<EntityPropertyDescriptor>();
			for (EntityPropertyDescriptor property : entity.getProperties().values()) {
				if (property.isVisibleInRawEntityList()) {
					fieldOrder.add(property);
				}
			}
			Comparator<EntityPropertyDescriptor> fieldComparator = ApplicationConfiguration.getRawEntityListFieldOrder();
			if (fieldComparator != null) {
				Collections.sort(fieldOrder, fieldComparator);
			}
			String[] fieldOrderArray = new String[fieldOrder.size()];
			int position = 0;
			for (EntityPropertyDescriptor property : fieldOrder) {
				fieldOrderArray[position] = property.getName();
				position++;
			}
			
			// send the query to the database and analyze result meta-data
			final ResultSet resultSet = statement.executeQuery("SELECT * FROM \"" + entity.getTableName() + "\" ORDER BY id LIMIT " + ROWS_PER_PAGE + " OFFSET " + (ROWS_PER_PAGE * currentPage));
			ResultSetReader reader = new ResultSetReader(resultSet, fieldOrderArray);
			width = reader.getWidth();
			
			// fetch data and fill the rows array
			rowIds = new Object[ROWS_PER_PAGE];
			rows = new Object[ROWS_PER_PAGE][];
			visibleRows = 0;
			while (reader.next()) {
				rowIds[visibleRows] = reader.getId();
				rows[visibleRows] = reader.getRow();
				visibleRows++;
			}
			
			// determine the column names and renderers
			columnNames = new String[width];
			renderers = new IPropertyReadOnlyRenderer[width];
			for (int i=0; i<width; i++) {
				columnNames[i] = reader.getFieldOrder()[i];
				renderers[i] = ApplicationConfiguration.getCapabilities().createPropertyReadOnlyRenderer(reader.getSqlFieldType(i));
				if (renderers[i] == null) {
					throw new RuntimeException("no renderer");
				}
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
