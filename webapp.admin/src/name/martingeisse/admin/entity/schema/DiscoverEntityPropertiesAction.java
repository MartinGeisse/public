/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.entity.EntityConfigurationUtil;
import name.martingeisse.admin.entity.property.IRawEntityListPropertyDisplayFilter;
import name.martingeisse.admin.entity.property.type.ISqlType;
import name.martingeisse.admin.entity.property.type.IntegerType;
import name.martingeisse.admin.entity.property.type.LongType;
import name.martingeisse.admin.entity.property.type.StringType;
import name.martingeisse.admin.entity.property.type.UnknownSqlType;

/**
 * This action discovers the entity properties for a single entity.
 */
class DiscoverEntityPropertiesAction {

	/**
	 * the connection
	 */
	private Connection connection;

	/**
	 * the entity
	 */
	private EntityDescriptor entity;

	/**
	 * Constructor.
	 */
	public DiscoverEntityPropertiesAction() {
	}

	/**
	 * Getter method for the connection.
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Setter method for the connection.
	 * @param connection the connection to set
	 */
	public void setConnection(final Connection connection) {
		this.connection = connection;
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}

	/**
	 * Setter method for the entity.
	 * @param entity the entity to set
	 */
	public void setEntity(final EntityDescriptor entity) {
		this.entity = entity;
	}

	/**
	 * Executes this action.
	 * @return the result
	 */
	public List<EntityPropertyDescriptor> execute() {
		try {
			final List<EntityPropertyDescriptor> result = new ArrayList<EntityPropertyDescriptor>();
			final ResultSet resultSet = connection.getMetaData().getColumns(null, null, entity.getTableName(), null);
			while (resultSet.next()) {
				final EntityPropertyDescriptor propertyDescriptor = new EntityPropertyDescriptor();
				propertyDescriptor.setVisibleInRawEntityList(true);
				propertyDescriptor.setName(resultSet.getString("COLUMN_NAME"));
				propertyDescriptor.setType(determineType(resultSet));
				propertyDescriptor.setVisibleInRawEntityList(isPropertyVisibleInRawEntityList(propertyDescriptor));
				result.add(propertyDescriptor);
			}
			resultSet.close();
			return result;
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Determines the {@link ISqlType} for the column currently described
	 * in the specified {@link ResultSet}. This method does not forward the
	 * result set.
	 * 
	 * @param resultSet the result set with column meta-data
	 * @return the type for the current column
	 */
	private ISqlType determineType(final ResultSet resultSet) throws SQLException {
		final int sqlTypeCode = resultSet.getInt("DATA_TYPE");
		switch (sqlTypeCode) {

		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
			return IntegerType.instance;

		case Types.BIGINT:
			return LongType.instance;

		case Types.CHAR:
		case Types.NCHAR:
		case Types.VARCHAR:
		case Types.NVARCHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CLOB:
		case Types.NCLOB:
			return StringType.instance;

		default:
			return new UnknownSqlType(sqlTypeCode);

		}
	}

	/**
	 * @param propertyDescriptor
	 * @return
	 */
	private boolean isPropertyVisibleInRawEntityList(final EntityPropertyDescriptor propertyDescriptor) {
		int score = Integer.MIN_VALUE;
		boolean visible = true;
		for (final IRawEntityListPropertyDisplayFilter filter : EntityConfigurationUtil.getRawEntityListPropertyDisplayFilters()) {
			final int filterScore = filter.getScore(entity, propertyDescriptor);
			if (filterScore >= score) {
				final Boolean filterResult = filter.isPropertyVisible(entity, propertyDescriptor);
				if (filterResult != null) {
					score = filterScore;
					visible = filterResult;
				}
			}
		}
		return visible;
	}

}
