/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.model.database;

import name.martingeisse.common.database.EntityConnectionManager;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import com.mysema.query.sql.RelationalPathBase;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Predicate;

/**
 * This model provides a single entity instance based on
 * conditions and/or custom code.
 *
 * @param <T> the entity type
 */
public class EntityModel<T> extends LoadableDetachableModel<T> {

	/**
	 * the NO_PREDICATES
	 */
	private static final Predicate[] NO_PREDICATES = new Predicate[0];

	/**
	 * the table
	 */
	private final RelationalPathBase<T> table;

	/**
	 * the predicates
	 */
	private final Predicate[] predicates;

	/**
	 * Constructor.
	 * @param table the table for the entity
	 */
	public EntityModel(RelationalPathBase<T> table) {
		this(table, NO_PREDICATES);
	}

	/**
	 * Static replacement for the constructor (avoids the explicit type argument).
	 * @param table the table for the entity
	 * @return the model
	 */
	public static <T> EntityModel<T> of(RelationalPathBase<T> table) {
		return new EntityModel<T>(table);
	}

	/**
	 * Constructor.
	 * @param table the table for the entity
	 * @param predicates the predicates used to find the entity instance
	 */
	public EntityModel(RelationalPathBase<T> table, Predicate[] predicates) {
		this.table = table;
		this.predicates = predicates;
	}

	/**
	 * Static replacement for the constructor (avoids the explicit type argument).
	 * @param table the table for the entity
	 * @param predicates the predicates used to find the entity instance
	 * @return the model
	 */
	public static <T> EntityModel<T> of(RelationalPathBase<T> table, Predicate[] predicates) {
		return new EntityModel<T>(table, predicates);
	}

	/**
	 * Getter method for the table.
	 * @return the table
	 */
	public RelationalPathBase<T> getTable() {
		return table;
	}

	/**
	 * Getter method for the predicates.
	 * @return the predicates
	 */
	public final Predicate[] getPredicates() {
		return predicates;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#load()
	 */
	@Override
	protected T load() {
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(table);
		configureQuery(query);
		return query.singleResult(table);
	}

	/**
	 * Configures the query that is used to fetch the entity from the database.
	 * Note that a "limit 1" is configured in addition, since this model can
	 * return only a single instance anyway.
	 * 
	 * The default implementation applies the predicates.
	 * 
	 * @param query the query to configure
	 */
	protected void configureQuery(SQLQuery query) {
		query.where(predicates);
	}

	/**
	 * Returns a model for a single field of the entity.
	 * @param expression the field name or expression
	 * @return the field model
	 */
	public <F> IModel<F> field(String expression) {
		return new PropertyModel<F>(this, expression);
	}

}
