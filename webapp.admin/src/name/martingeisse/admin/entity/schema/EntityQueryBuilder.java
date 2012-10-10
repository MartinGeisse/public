/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.schema;

import java.util.List;

import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.instance.RawEntityInstance;
import name.martingeisse.admin.entity.schema.orm.EntityOrmMapping;
import name.martingeisse.common.util.ParameterUtil;

import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLDeleteClause;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * This class encapsulates entity methods for building QueryDSL queries.
 * 
 * Internally this class relies on the entity's {@link EntityOrmMapping}
 * to generate {@link RelationalPath} instances.
 */
public final class EntityQueryBuilder {

	/**
	 * the entity
	 */
	private final EntityDescriptor entity;

	/**
	 * Constructor.
	 * @param entity the entity
	 */
	public EntityQueryBuilder(EntityDescriptor entity) {
		this.entity = entity;
	}
	
	/**
	 * Getter method for the entity.
	 * @return the entity
	 */
	public EntityDescriptor getEntity() {
		return entity;
	}
	
	/**
	 * Getter method for the defaultPath.
	 * @return the defaultPath
	 */
	public RelationalPath<?> getDefaultPath() {
		return entity.getSpecificCodeMapping().getDefaultPath();
	}
	
	/**
	 * Creates a {@link RelationalPath} for this entity with the default path variable (the table name).
	 * @return the {@link RelationalPath}
	 */
	public RelationalPath<?> createRelationalPath() {
		return entity.getSpecificCodeMapping().createRelationalPath();
	}

	/**
	 * Creates a {@link RelationalPath} for this entity with the specified path variable.
	 * @param variable the path variable (AS clause name)
	 * @return the {@link RelationalPath}
	 */
	public RelationalPath<?> createRelationalPath(String variable) {
		return entity.getSpecificCodeMapping().createRelationalPath(variable);
	}
	
	/**
	 * Creates an {@link SQLInsertClause} for this entity using the default path (the table name).
	 * @return the insert clause
	 */
	public SQLInsertClause createInsert() {
		return entity.getConnection().createInsert(getDefaultPath());
	}
	
	/**
	 * Creates an {@link SQLInsertClause} for this entity using the specified path.
	 * @param path the path 
	 * @return the insert clause
	 */
	public SQLInsertClause createInsert(final RelationalPath<?> path) {
		ParameterUtil.ensureNotNull(path, "path");
		return entity.getConnection().createInsert(path);
	}

	/**
	 * Creates an {@link SQLInsertClause} for this entity using the specified
	 * columns. This is a convenience method to allow specifying the column
	 * names as strings instead of {@link Path}s.
	 * 
	 * This method uses the default path for the entity.
	 * 
	 * @param columns the column names
	 * @return the insert clause
	 */
	public SQLInsertClause createInsert(final String... columns) {
		return createInsert(getDefaultPath(), columns);
	}

	/**
	 * Creates an {@link SQLInsertClause} for this entity using the specified
	 * columns. This is a convenience method to allow specifying the column
	 * names as strings instead of {@link Path}s.
	 * 
	 * @param path the path for this entity
	 * @param columns the column names
	 * @return the insert clause
	 */
	public SQLInsertClause createInsert(final RelationalPath<?> path, final String... columns) {
		ParameterUtil.ensureNotNull(path, "path");
		ParameterUtil.ensureNotNull(columns, "columns");
		ParameterUtil.ensureNoNullElement(columns, "columns");
		final Path<?>[] columnPaths = new Path<?>[columns.length];
		for (int i = 0; i < columnPaths.length; i++) {
			columnPaths[i] = Expressions.path(Object.class, columns[i]);
		}
		return createInsert(path).columns(columnPaths);
	}
	
	/**
	 * Creates an {@link SQLUpdateClause} for this entity using the default path (the table name).
	 * @return the update clause
	 */
	public SQLUpdateClause createUpdate() {
		return createUpdate(getDefaultPath());
	}

	/**
	 * Creates an {@link SQLUpdateClause} for this entity.
	 * @param path the path for this entity
	 * @return the update clause
	 */
	public SQLUpdateClause createUpdate(final RelationalPath<?> path) {
		ParameterUtil.ensureNotNull(path, "path");
		return entity.getConnection().createUpdate(path);
	}

	/**
	 * Creates an {@link SQLDeleteClause} for this entity.
	 * @return the delete clause
	 */
	public SQLDeleteClause createDelete() {
		return createDelete(getDefaultPath());
	}

	/**
	 * Creates an {@link SQLDeleteClause} for this entity.
	 * @param path the path for this entity
	 * @return the delete clause
	 */
	public SQLDeleteClause createDelete(final RelationalPath<?> path) {
		ParameterUtil.ensureNotNull(path, "path");
		return entity.getConnection().createDelete(path);
	}

	/**
	 * Queries for the number of instances of this entity, or the number of
	 * instances accepted by the specified filter predicate.
	 * This method assumes that the predicate uses the default path for the entity
	 * (the table name).
	 * 
	 * @param filterPredicate the filter predicate (null to count all instances)
	 * @return the number of instances
	 */
	public long count(final Predicate filterPredicate) {
		return count(getDefaultPath(), filterPredicate);
	}

	/**
	 * Queries for the number of instances of this entity, or the number of
	 * instances accepted by the specified filter predicate.
	 * This method assumes that the predicate uses the specified path for the entity.
	 * 
	 * @param path the path for the entity
	 * @param filterPredicate the filter predicate (null to count all instances)
	 * @return the number of instances
	 */
	public long count(final RelationalPath<?> path, final Predicate filterPredicate) {
		SQLQuery countQuery = entity.getConnection().createQuery().from(path);
		if (filterPredicate != null) {
			countQuery = countQuery.where(filterPredicate);
		}
		return countQuery.count();
	}
	
	/**
	 * Creates a query for this entity using the default path (the table name).
	 * @return the query
	 */
	public SQLQuery createQuery() {
		return createQuery(getDefaultPath());
	}
	
	/**
	 * Creates a query for this entity using the specified path.
	 * @param path the path for this entity
	 * @return the query
	 */
	public SQLQuery createQuery(final RelationalPath<?> path) {
		ParameterUtil.ensureNotNull(path, "path");
		return entity.getConnection().createQuery().from(path);
	}

	/**
	 * Shortcut for getSingle() using the default path (the table name).
	 * @param query the query
	 * @return the entity instance
	 */
	public IEntityInstance getSingle(SQLQuery query) {
		return getSingle(query, getDefaultPath());
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
		return entity.getSpecificCodeMapping().getSingle(query, path);
	}

	/**
	 * Shortcut for getAll() using the default path (the table name).
	 * @param query the query
	 * @return the entity instances
	 */
	public List<IEntityInstance> getAll(SQLQuery query) {
		return getAll(query, getDefaultPath());
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
	public List<IEntityInstance> getAll(SQLQuery query, final RelationalPath<?> path) {
		return entity.getSpecificCodeMapping().getAll(query, path);
	}

}
