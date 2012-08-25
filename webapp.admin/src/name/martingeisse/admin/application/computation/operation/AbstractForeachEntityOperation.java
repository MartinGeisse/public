/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.application.computation.operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import name.martingeisse.admin.database.IEntityDatabaseConnection;
import name.martingeisse.admin.entity.instance.EntityInstance;
import name.martingeisse.admin.entity.list.IEntityListFilter;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.computation.operation.AbstractForeachOperation;
import name.martingeisse.common.computation.operation.ForeachHandlingMode;

import org.apache.log4j.Logger;

import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.expr.Wildcard;

/**
 * Base class for operations that fetch a list of entity instances,
 * then loop over them and execute a subclass method for each
 * instance. This class provides exception handling, transactions,
 * and logging, all of them per-list and per-element.
 * 
 * Automatic logging is performed on DEBUG and TRACE levels. Add your
 * own logging calls if you need logging on higher levels.
 * 
 * @param <P> internal per-call parameter type. This type is typically decided
 * on by the concrete subclass since it knows which data to pass around.
 */
public abstract class AbstractForeachEntityOperation<P> extends AbstractForeachOperation<EntityInstance, P> {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(AbstractForeachEntityOperation.class);

	/**
	 * the entityDescriptor
	 */
	private EntityDescriptor entityDescriptor;

	/**
	 * the transactionMode
	 */
	private ForeachHandlingMode transactionMode;

	/**
	 * Constructor. The exception handling mode and transaction mode
	 * default to NONE.
	 */
	public AbstractForeachEntityOperation() {
		this(null, ForeachHandlingMode.NONE, ForeachHandlingMode.NONE);
	}

	/**
	 * Constructor. The exception handling mode and transaction mode
	 * default to NONE.
	 * @param entityDescriptor the entity descriptor for the entity to fetch from
	 */
	public AbstractForeachEntityOperation(final EntityDescriptor entityDescriptor) {
		this(entityDescriptor, ForeachHandlingMode.NONE, ForeachHandlingMode.NONE);
	}

	/**
	 * Constructor.
	 * @param entityDescriptor the entity descriptor for the entity to fetch from
	 * @param exceptionMode the exception handling mode
	 * @param transactionMode the transaction handling mode
	 */
	public AbstractForeachEntityOperation(final EntityDescriptor entityDescriptor, final ForeachHandlingMode exceptionMode, final ForeachHandlingMode transactionMode) {
		super(exceptionMode);
	}

	/**
	 * Getter method for the entityDescriptor.
	 * @return the entityDescriptor
	 */
	public EntityDescriptor getEntityDescriptor() {
		return entityDescriptor;
	}

	/**
	 * Setter method for the entityDescriptor.
	 * @param entityDescriptor the entityDescriptor to set
	 */
	public void setEntityDescriptor(final EntityDescriptor entityDescriptor) {
		this.entityDescriptor = entityDescriptor;
	}

	/**
	 * Getter method for the transactionMode.
	 * @return the transactionMode
	 */
	public ForeachHandlingMode getTransactionMode() {
		return transactionMode;
	}

	/**
	 * Setter method for the transactionMode.
	 * @param transactionMode the transactionMode to set
	 */
	public void setTransactionMode(final ForeachHandlingMode transactionMode) {
		this.transactionMode = transactionMode;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#onValidate(java.lang.Object)
	 */
	@Override
	protected void onValidate(final P parameter) {
		super.onValidate(parameter);
		if (transactionMode == null) {
			throw new IllegalStateException("transactionMode is null");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#determineList(java.lang.Object)
	 */
	@Override
	protected List<EntityInstance> determineList(final P parameter) {
		try {

			// obtain a ResultSet
			final EntityDescriptor entity = getEntityDescriptor();
			final SQLQuery query = entity.query(IEntityListFilter.ALIAS);
			configureQuery(parameter, query);
			final ResultSet resultSet = query.getResults(Wildcard.all);

			// fetch rows
			entity.checkDataRowMeta(resultSet);
			final List<EntityInstance> rows = new ArrayList<EntityInstance>();
			while (resultSet.next()) {
				rows.add(new EntityInstance(entity, resultSet));
			}

			// clean up
			resultSet.close();
			return rows;

		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Configures the query that is used to fetch entity instances.
	 * This method can, for example, add a WHERE clause, ORDER clause,
	 * or OFFSET/LIMIT. By default, the query contains nothing but
	 * the FROM clause.
	 * 
	 * Query clauses should refer to the entity being fetched as
	 * {@link IEntityListFilter#ALIAS}.
	 * 
	 * The default implementation does nothing.
	 * 
	 * @param parameter internal per-call parameter
	 * @param query the query to configure
	 */
	protected void configureQuery(final P parameter, final SQLQuery query) {
	}

	/**
	 * Converts the current row from the specified result set to an entity instance.
	 * Overriding this method basically allows subclasses of this class to use
	 * subclasses of {@link EntityInstance}. Operations need not override this
	 * method if {@link EntityInstance} fits their needs.
	 * 
	 * @param parameter internal per-call parameter
	 * @param resultSet the result set
	 * @return the entity instance
	 * @throws SQLException on SQL errors
	 */
	protected EntityInstance convertRow(final P parameter, final ResultSet resultSet) throws SQLException {
		return new EntityInstance(getEntityDescriptor(), resultSet);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#onListObtained(java.lang.Object, java.util.List)
	 */
	@Override
	protected void onListObtained(final P parameter, final List<EntityInstance> list) {
		super.onListObtained(parameter, list);
		logger.debug(this.getClass().getName() + ": " + list.size() + " rows found");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#handleListAndContextWork(java.lang.Object)
	 */
	@Override
	protected void handleListAndContextWork(final P parameter) {
		IEntityDatabaseConnection connection = getEntityDescriptor().getConnection();
		if (transactionMode.isIncludesGlobal()) {
			connection.begin();
		}
		super.handleListAndContextWork(parameter);
		if (transactionMode.isIncludesGlobal() && connection.isTransactionRunning()) {
			connection.commit();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#onGlobalException(java.lang.Object, java.lang.Exception)
	 */
	@Override
	protected void onGlobalException(final P parameter, final Exception e) {
		super.onGlobalException(parameter, e);
		IEntityDatabaseConnection connection = getEntityDescriptor().getConnection();
		if (transactionMode.isIncludesGlobal()) {
			connection.rollback();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#handleElementAndContextWork(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void handleElementAndContextWork(final P parameter, final EntityInstance element) {
		IEntityDatabaseConnection connection = getEntityDescriptor().getConnection();
		if (transactionMode.isIncludesLocal()) {
			connection.begin();
		}
		super.handleElementAndContextWork(parameter, element);
		if (transactionMode.isIncludesLocal() && connection.isTransactionRunning()) {
			connection.commit();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#onLocalException(java.lang.Object, java.lang.Object, java.lang.Exception)
	 */
	@Override
	protected void onLocalException(final P parameter, final EntityInstance element, final Exception e) {
		super.onLocalException(parameter, element, e);
		IEntityDatabaseConnection connection = getEntityDescriptor().getConnection();
		if (transactionMode.isIncludesLocal()) {
			connection.rollback();
		}
	}

}
