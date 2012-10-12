/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema.orm;

import static com.mysema.query.types.PathMetadataFactory.forVariable;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.instance.RawEntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.datarow.DataRowMeta;
import name.martingeisse.common.util.GenericTypeUtil;
import name.martingeisse.common.util.ParameterUtil;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.expr.Wildcard;

/**
 * This class represents the mapping between an entity and specific code for that entity.
 * If the entity is mapped to a generated class, then this class stores the class object
 * for that class. In any case, this class is used to build {@link RelationalPath}
 * instances for database queries.
 */
public final class EntityOrmMapping {

	/**
	 * the entity
	 */
	private final EntityDescriptor entity;

	/**
	 * the entityInstanceClass
	 */
	private final Class<?> entityInstanceClass;

	/**
	 * the queryClass
	 */
	private final Class<? extends RelationalPath<?>> queryClass;
	
	/**
	 * the defaultPath
	 */
	private final RelationalPath<?> defaultPath;
	
	/**
	 * Constructor.
	 * @param entity the parent entity (must not be null)
	 * @param entityInstanceClass the class used for entity instances, or null to use the
	 * default {@link RawEntityInstance}.
	 * @param queryClass the QueryDSL Q-class for the entity
	 */
	public EntityOrmMapping(final EntityDescriptor entity, final Class<?> entityInstanceClass, final Class<? extends RelationalPath<?>> queryClass) {
		this.entity = ParameterUtil.ensureNotNull(entity, "entity");
		this.entityInstanceClass = entityInstanceClass;
		this.queryClass = queryClass;
		this.defaultPath = createRelationalPath();
	}

	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}

	/**
	 * Getter method for the entityInstanceClass.
	 * @return the entityInstanceClass
	 */
	public Class<?> getEntityInstanceClass() {
		return entityInstanceClass;
	}
	
	/**
	 * Getter method for the queryClass.
	 * @return the queryClass
	 */
	public Class<? extends RelationalPath<?>> getQueryClass() {
		return queryClass;
	}
	
	/**
	 * Getter method for the defaultPath.
	 * @return the defaultPath
	 */
	public RelationalPath<?> getDefaultPath() {
		return defaultPath;
	}

	/**
	 * Creates a {@link RelationalPath} for this entity with the default path variable (the table name).
	 * @return the {@link RelationalPath}
	 */
	public RelationalPath<?> createRelationalPath() {
		return createRelationalPath(entity.getTableName());
	}

	/**
	 * Creates a {@link RelationalPath} for this entity with the specified path variable.
	 * @param variable the path variable (AS clause name)
	 * @return the {@link RelationalPath}
	 */
	public RelationalPath<?> createRelationalPath(String variable) {
		// implementation note: QueryDSL scans the Q-classes for their static fields,
		// so if we have Q-classes, we *must* pass *them* and not just a RelationalPath
		// with the same type and table.
		if (queryClass == null) {
			return new RelationalPathBase<Object>(Object.class, forVariable(variable), "null", entity.getTableName());
		} else {
			try {
				return queryClass.getConstructor(String.class).newInstance(variable);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Returns a single instance from the specified query. This method must be used
	 * if the calling code cannot ensure that there is a generated bean class for
	 * this entity, and in turn ensures that {@link RawEntityInstance} is used
	 * if no bean class is available.
	 * 
	 * Note that this method does not add a LIMIT to the query since that would
	 * modify the query; it just uses only a single result from the result set.
	 * Calling code should add a LIMIT to improve performance.
	 * 
	 * @param query the query
	 * @param path the path used in the query
	 * @return the entity instance
	 */
	public IEntityInstance getSingle(SQLQuery query, RelationalPath<?> path) {
		if (entityInstanceClass == null) {
			try {
				final ResultSet resultSet = query.getResults(Wildcard.all);
				checkDataRowMeta(resultSet);
				IEntityInstance row;
				if (resultSet.next()) {
					row = new RawEntityInstance(entity, resultSet);
				} else {
					row = null;
				}
				resultSet.close();
				return row;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			return (IEntityInstance)query.singleResult(path);
		}
	}

	/**
	 * Returns all instances from the specified query. This method must be used
	 * if the calling code cannot ensure that there is a generated bean class for
	 * this entity, and in turn ensures that {@link RawEntityInstance} is used
	 * if no bean class is available.
	 * 
	 * @param query the query
	 * @param path the path used in the query
	 * @return the entity instances
	 */
	public List<IEntityInstance> getAll(SQLQuery query, RelationalPath<?> path) {
		if (entityInstanceClass == null) {
			try {
				final ResultSet resultSet = query.getResults(Wildcard.all);
				checkDataRowMeta(resultSet);
				final List<IEntityInstance> rows = new ArrayList<IEntityInstance>();
				while (resultSet.next()) {
					rows.add(new RawEntityInstance(entity, resultSet));
				}
				resultSet.close();
				return rows;
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		} else {
			return GenericTypeUtil.unsafeCast(query.list(path));
		}
	}

	/**
	 * Ensures that the data row meta-data for this entity is equal to the one
	 * for the specified result set meta-data. Throws an {@link IllegalStateException}
	 * if that is not the case. This indicates that the table schema is no longer
	 * the same as when this {@link EntityDescriptor} was created, i.e. that the
	 * table schema has been changed while the admin application was running.
	 * 
	 * @param resultSet the result set to check
	 * @throws SQLException on SQL errors
	 */
	private void checkDataRowMeta(final ResultSet resultSet) throws SQLException {
		if (!entity.getDataRowMeta().equals(new DataRowMeta(resultSet.getMetaData()))) {
			throw new IllegalStateException("data row schema for entity " + entity.getName() + " does not match");
		}
	}
	
}
