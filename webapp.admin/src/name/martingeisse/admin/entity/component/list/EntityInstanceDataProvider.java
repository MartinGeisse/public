/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.component.list;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.admin.entity.schema.database.AbstractDatabaseDescriptor;
import name.martingeisse.common.jdbc.ResultSetReader;
import name.martingeisse.common.sql.CountAllTarget;
import name.martingeisse.common.sql.PrimaryTableFetchSpecifier;
import name.martingeisse.common.sql.SelectStatement;
import name.martingeisse.common.sql.build.SqlBuilderForMySql;

import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * An {@link IDataProvider} that fetches entity instances and returns
 * them as {@link EntityInstance} objects.
 */
public class EntityInstanceDataProvider implements IDataProvider<EntityInstance> {

	/**
	 * the entityModel
	 */
	private final IModel<EntityDescriptor> entityModel;
	
	/**
	 * the filter
	 */
	private final IEntityListFilter filter;
	
	/**
	 * Constructor.
	 * @param entityModel the model that provides the entity descriptor
	 */
	public EntityInstanceDataProvider(IModel<EntityDescriptor> entityModel) {
		this.entityModel = entityModel;
		this.filter = null;
	}
	
	/**
	 * Constructor.
	 * @param entityModel the model that provides the entity descriptor
	 * @param filter the filter
	 */
	public EntityInstanceDataProvider(IModel<EntityDescriptor> entityModel, IEntityListFilter filter) {
		this.entityModel = entityModel;
		this.filter = filter;
	}
	
	/**
	 * Getter method for the entityModel.
	 * @return the entityModel
	 */
	public IModel<EntityDescriptor> getEntityModel() {
		return entityModel;
	}
	
	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return getEntityModel().getObject();
	}
	
	/**
	 * Getter method for the filter.
	 * @return the filter
	 */
	public IEntityListFilter getFilter() {
		return filter;
	}
	
	/**
	 * This method can be implemented by subclasses to trigger additional behavior
	 * when the fetch result is available. The default implementation does nothing.
	 * @param reader the result set reader
	 */
	protected void onResultAvailable(ResultSetReader reader) throws SQLException {
	}
	
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
			final EntityDescriptor entity = getEntity();
			final AbstractDatabaseDescriptor database = entity.getDatabase();
			connection = database.createConnection();
			final Statement statement = connection.createStatement();
			
			String countQuery;
			{
				SelectStatement countStatement = new SelectStatement();
				countStatement.getTargets().add(new CountAllTarget());
				countStatement.setPrimaryTableFetchSpecifier(new PrimaryTableFetchSpecifier(entity.getTableName(), IEntityListFilter.ALIAS));
				if (filter != null) {
					countStatement.getConditions().add(filter.getFilterExpression());
				}
				countQuery = countStatement.toString(new SqlBuilderForMySql());
			}
			int size = (int)(long)(Long)database.executeSingleResultQuery(statement, countQuery);
			
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
			final EntityDescriptor entity = getEntity();
			final AbstractDatabaseDescriptor database = entity.getDatabase();
			connection = database.createConnection();
			final Statement statement = connection.createStatement();

			// send the query to the database and analyze result meta-data
			String query;
			{
				SelectStatement selectStatement = new SelectStatement();
				selectStatement.setPrimaryTableFetchSpecifier(new PrimaryTableFetchSpecifier(entity.getTableName(), IEntityListFilter.ALIAS));
				if (filter != null) {
					selectStatement.getConditions().add(filter.getFilterExpression());
				}
				selectStatement.setOffset(first);
				selectStatement.setLimit(count);
				query = selectStatement.toString(new SqlBuilderForMySql());
			}
			final ResultSet resultSet = statement.executeQuery(query);
			ResultSetReader reader = new ResultSetReader(resultSet, entity.getIdColumnName(), entity.getRawEntityListFieldOrder());
			
			// fetch data and fill the rows array
			List<EntityInstance> rows = new ArrayList<EntityInstance>();
			String[] fieldNames = reader.getFieldOrder(); // EntityInstance.getFieldNames(entity, resultSet);
			while (reader.next()) {
				rows.add(new EntityInstance(entity, reader.getId(), fieldNames, reader.getRow()));
			}
			
			// additional client-specific behavior
			onResultAvailable(reader);

			// clean up
			resultSet.close();
			statement.close();
			
			// return the rows as an iterator
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
