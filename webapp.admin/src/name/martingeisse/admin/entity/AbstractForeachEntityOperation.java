/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.entity;

import java.util.List;

import name.martingeisse.admin.entity.instance.IEntityInstance;
import name.martingeisse.admin.entity.schema.EntityDescriptor;
import name.martingeisse.common.computation.operation.AbstractForeachOperation;
import name.martingeisse.common.computation.operation.ForeachHandlingMode;
import name.martingeisse.common.database.IEntityDatabaseConnection;
import name.martingeisse.common.util.ObjectStateUtil;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;

import org.apache.log4j.Logger;

import com.mysema.query.sql.SQLQuery;

/**
 * Base class for operations that fetch a list of entity instances,
 * then loop over them and execute a subclass method for each
 * instance. This class provides exception handling, transactions,
 * and logging, all of them per-list and per-element.
 * 
 * The entity query uses the default path for the entity.
 * 
 * Automatic logging is performed on DEBUG and TRACE levels. Add your
 * own logging calls if you need logging on higher levels.
 * 
 * @param <P> internal per-call parameter type. This type is typically decided
 * on by the concrete subclass since it knows which data to pass around.
 */
public abstract class AbstractForeachEntityOperation<P> extends AbstractForeachOperation<IEntityInstance, P> {

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
		this.entityDescriptor = ParameterUtil.ensureNotNull(entityDescriptor, "entityDescriptor");
		this.transactionMode = ParameterUtil.ensureNotNull(transactionMode, "transactionMode");
	}

	/**
	 * Getter method for the entityDescriptor.
	 * @return the entityDescriptor
	 */
	public final EntityDescriptor getEntityDescriptor() {
		return entityDescriptor;
	}

	/**
	 * Setter method for the entityDescriptor.
	 * @param entityDescriptor the entityDescriptor to set
	 */
	public final void setEntityDescriptor(final EntityDescriptor entityDescriptor) {
		this.entityDescriptor = ParameterUtil.ensureNotNull(entityDescriptor, "entityDescriptor");
	}

	/**
	 * Getter method for the transactionMode.
	 * @return the transactionMode
	 */
	public final ForeachHandlingMode getTransactionMode() {
		return transactionMode;
	}

	/**
	 * Setter method for the transactionMode.
	 * @param transactionMode the transactionMode to set
	 */
	public final void setTransactionMode(final ForeachHandlingMode transactionMode) {
		this.transactionMode = ParameterUtil.ensureNotNull(transactionMode, "transactionMode");
	}

	/**
	 * Returns a database connection for the entity.
	 * @return the connection
	 */
	private IEntityDatabaseConnection getConnection() {
		return ReturnValueUtil.nullNotAllowed(entityDescriptor.getConnection(), "getConnection() in entity: " + entityDescriptor.getName());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#onValidate(java.lang.Object)
	 */
	@Override
	protected void onValidate(final P parameter) {
		super.onValidate(parameter);
		ObjectStateUtil.nullNotAllowed(transactionMode, "transactionMode");
		ObjectStateUtil.nullNotAllowed(entityDescriptor, "entityDescriptor");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#determineList(java.lang.Object)
	 */
	@Override
	protected List<IEntityInstance> determineList(final P parameter) {
		final SQLQuery query = entityDescriptor.getQueryBuilder().createQuery();
		configureQuery(parameter, query);
		return entityDescriptor.getQueryBuilder().getAll(query);
	}

	/**
	 * Configures the query that is used to fetch entity instances.
	 * This method can, for example, add a WHERE clause, ORDER clause,
	 * or OFFSET/LIMIT. By default, the query contains nothing but
	 * the FROM clause.
	 * 
	 * Query clauses should refer to the entity being fetched using
	 * its default path.
	 * 
	 * The default implementation does nothing.
	 * 
	 * @param parameter internal per-call parameter
	 * @param query the query to configure
	 */
	protected void configureQuery(final P parameter, final SQLQuery query) {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#onListObtained(java.lang.Object, java.util.List)
	 */
	@Override
	protected void onListObtained(final P parameter, final List<IEntityInstance> list) {
		super.onListObtained(parameter, list);
		logger.debug(this.getClass().getName() + ": " + list.size() + " rows found");
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#handleListAndContextWork(java.lang.Object)
	 */
	@Override
	protected void handleListAndContextWork(final P parameter) {
		IEntityDatabaseConnection connection = getConnection();
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
		IEntityDatabaseConnection connection = getConnection();
		if (transactionMode.isIncludesGlobal()) {
			connection.rollback();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.computation.operation.AbstractForeachOperation#handleElementAndContextWork(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void handleElementAndContextWork(final P parameter, final IEntityInstance element) {
		IEntityDatabaseConnection connection = getConnection();
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
	protected void onLocalException(final P parameter, final IEntityInstance element, final Exception e) {
		super.onLocalException(parameter, element, e);
		IEntityDatabaseConnection connection = getConnection();
		if (transactionMode.isIncludesLocal()) {
			connection.rollback();
		}
	}

}
