/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.multi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import name.martingeisse.admin.application.ApplicationConfiguration;
import name.martingeisse.admin.pages.EntityPresentationPage;
import name.martingeisse.admin.readonly.FallbackRenderer;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRenderer;
import name.martingeisse.admin.readonly.IPropertyReadOnlyRendererContributor;
import name.martingeisse.admin.schema.DatabaseDescriptor;
import name.martingeisse.admin.schema.EntityDescriptor;
import name.martingeisse.admin.schema.EntityPropertyDescriptor;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
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
	 * the idColumnIndex
	 */
	private transient int idColumnIndex;
	
	/**
	 * the columnNames
	 */
	private transient String[] columnNames;
	
	/**
	 * the renderers
	 */
	private transient IPropertyReadOnlyRenderer[] renderers;
	
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
		add(new Loop("rows", new PropertyModel<Integer>(RawGlobalEntityListPanel.this, "visibleRows")) {
			
			/* (non-Javadoc)
			 * @see org.apache.wicket.markup.html.list.Loop#newItem(int)
			 */
			@Override
			protected LoopItem newItem(final int iteration) {
				return new LoopItem(iteration) {
					@Override
					protected void onComponentTag(ComponentTag tag) {
						super.onComponentTag(tag);
						tag.getAttributes().put("class", ((iteration & 1) == 0) ? "even" : "odd");
					}
				};
			}
			
			@Override
			protected void populateItem(final LoopItem rowItem) {
				rowItem.add(new Loop("cells", new PropertyModel<Integer>(RawGlobalEntityListPanel.this, "width")) {
					@Override
					protected void populateItem(final LoopItem cellItem) {
						PageParameters parameters = new PageParameters();
						parameters.add("entity", ((EntityDescriptor)RawGlobalEntityListPanel.this.getDefaultModelObject()).getTableName());
						parameters.add("id", rows[rowItem.getIndex()][idColumnIndex]);
						BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", EntityPresentationPage.class, parameters);
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
			int rowCount = fetchTableSize(statement, entity.getTableName());
			pageCount = (rowCount + ROWS_PER_PAGE - 1) / ROWS_PER_PAGE;
			
			// send the query to the database and analyze result meta-data
			final ResultSet resultSet = statement.executeQuery("SELECT * FROM \"" + entity.getTableName() + "\" ORDER BY id LIMIT " + ROWS_PER_PAGE + " OFFSET " + (ROWS_PER_PAGE * currentPage));
			idColumnIndex = findIdColumnIndex(resultSet);
			int fetchWidth = resultSet.getMetaData().getColumnCount();
			boolean[] columnVisible = new boolean[fetchWidth];
			width = 0;
			for (int i=0; i<fetchWidth; i++) {
				String columnName = resultSet.getMetaData().getColumnName(1 + i);
				EntityPropertyDescriptor propertyDescriptor = entity.getProperties().get(columnName);
				boolean visible = (propertyDescriptor != null && propertyDescriptor.isVisibleInRawEntityList());
				columnVisible[i] = visible;
				if (visible) {
					width++;
				}
			}
			
			// fetch data and fill the rows array
			rows = new Object[ROWS_PER_PAGE][width];
			visibleRows = 0;
			while (resultSet.next()) {
				int position = 0;
				for (int i = 0; i < fetchWidth; i++) {
					if (columnVisible[i]) {
						rows[visibleRows][position] = resultSet.getObject(1 + i);
						position++;
					}
				}
				visibleRows++;
			}
			
			// determine the column names and renderers
			columnNames = new String[width];
			renderers = new IPropertyReadOnlyRenderer[width];
			int position = 0;
			for (int i=0; i<fetchWidth; i++) {
				if (columnVisible[i]) {
					int type = resultSet.getMetaData().getColumnType(1 + i);
					IPropertyReadOnlyRenderer renderer = determineRenderer(type);
					if (renderer == null) {
						throw new RuntimeException("no renderer");
					}
					columnNames[position] = resultSet.getMetaData().getColumnName(1 + i);
					renderers[position] = renderer;
					position++;
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
	
	/**
	 * @return the table size
	 */
	private int fetchTableSize(Statement statement, String tableName) throws SQLException {
		final ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM \"" + tableName + "\"");
		if (!resultSet.next()) {
			throw new RuntimeException("unexpected result set layout for COUNT(*) (no row)");
		}
		if (resultSet.getMetaData().getColumnCount() != 1) {
			throw new RuntimeException("unexpected result set layout for COUNT(*) (number of columns is " + resultSet.getMetaData().getColumnCount() + ")");
		}
		int count = resultSet.getInt(1);
		if (resultSet.next()) {
			throw new RuntimeException("unexpected result set layout for COUNT(*)");
		}
		resultSet.close();
		return count;
	}

	/**
	 * @param resultSet
	 * @return
	 */
	private static int findIdColumnIndex(ResultSet resultSet) throws SQLException {
		ResultSetMetaData meta = resultSet.getMetaData();
		for (int i=0; i<meta.getColumnCount(); i++) {
			if (meta.getColumnLabel(1 + i).equals("id")) {
				return i;
			}
		}
		throw new RuntimeException("no id column found!");
	}

	/**
	 * @return
	 */
	private IPropertyReadOnlyRenderer determineRenderer(int type) {
		FallbackRenderer fallbackRenderer = new FallbackRenderer();
		for (IPropertyReadOnlyRendererContributor contributor : ApplicationConfiguration.getCapabilities().getPropertyReadOnlyRendererContributors()) {
			IPropertyReadOnlyRenderer renderer = contributor.getRenderer(type);
			if (renderer != null) {
				fallbackRenderer.setPrimaryRenderer(renderer);
			}
		}
		return fallbackRenderer;
	}
	
}
