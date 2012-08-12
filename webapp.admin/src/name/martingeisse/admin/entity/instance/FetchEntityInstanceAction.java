/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity.instance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import name.martingeisse.admin.entity.schema.EntityDescriptor;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.Wildcard;

/**
 * This action fetches a single entity instance.
 */
public class FetchEntityInstanceAction {

	/**
	 * the entity
	 */
	private EntityDescriptor entity;

	/**
	 * the id
	 */
	private Object id;

	/**
	 * the optional
	 */
	private boolean optional;

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
	 * Getter method for the id.
	 * @return the id
	 */
	public Object getId() {
		return id;
	}

	/**
	 * Setter method for the id.
	 * @param id the id to set
	 */
	public void setId(final Object id) {
		this.id = id;
	}

	/**
	 * Getter method for the optional.
	 * @return the optional
	 */
	public boolean isOptional() {
		return optional;
	}

	/**
	 * Setter method for the optional.
	 * @param optional the optional to set
	 */
	public void setOptional(final boolean optional) {
		this.optional = optional;
	}

	/**
	 * Executes this action.
	 * @return the result
	 */
	public EntityInstance execute() {

		if (entity.getIdColumnName() == null) {
			throw new RuntimeException("Cannot fetch entity instance for entity " + entity.getName() + ": ID column unknown");
		}

		Connection connection = null;
		try {
			connection = entity.getDatabase().createConnection();
			SQLQuery query = new SQLQueryImpl(connection, new MySQLTemplates()); // TODO use the database descriptor as a factory
			Path<?> entityExpression = Expressions.path(Object.class, entity.getTableName());
			Expression<?> idExpression = Expressions.path(Object.class, entityExpression, entity.getIdColumnName());
			Predicate idMatchPredicate = Expressions.predicate(Ops.EQ, idExpression, Expressions.constant(id));
			final ResultSet resultSet = query.from(entityExpression).where(idMatchPredicate).getResults(Wildcard.all);
			entity.checkDataRowMeta(resultSet);
			if (resultSet.next()) {
				return new EntityInstance(entity, resultSet);
			} else if (optional) {
				return null;
			} else {
				throw new IllegalArgumentException("unknown ID " + id + " for entity " + entity.getName());
			}
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

}
