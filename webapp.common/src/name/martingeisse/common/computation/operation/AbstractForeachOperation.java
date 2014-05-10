/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.computation.operation;

import java.util.List;
import name.martingeisse.common.util.ObjectStateUtil;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.common.util.ReturnValueUtil;
import org.apache.log4j.Logger;

/**
 * This operation loops over a list of elements and invokes a subclass method for
 * each element. It allows subclasses to provide implicit handling for the list
 * and/or elements. This class itself offers exception handling; other examples
 * include JDBC transaction handling.
 * 
 * This operation does not implement any of the operation interfaces since it
 * is subclass-dependent where the list comes from.
 * 
 * @param <T> the element type
 * @param <P> internal per-call parameter type. This type is typically decided
 * on by the concrete subclass since it knows which data to pass around.
 */
public abstract class AbstractForeachOperation<T, P> {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(AbstractForeachOperation.class);

	/**
	 * the exceptionMode
	 */
	private ForeachHandlingMode exceptionMode;

	/**
	 * Constructor. The exception handling mode defaults to NONE.
	 */
	public AbstractForeachOperation() {
		this.exceptionMode = ForeachHandlingMode.NONE;
	}

	/**
	 * Constructor.
	 * @param exceptionMode the exception handling mode
	 */
	public AbstractForeachOperation(final ForeachHandlingMode exceptionMode) {
		this.exceptionMode = ParameterUtil.ensureNotNull(exceptionMode, "exceptionMode");;
	}

	/**
	 * Getter method for the exceptionMode.
	 * @return the exceptionMode
	 */
	public ForeachHandlingMode getExceptionMode() {
		return exceptionMode;
	}

	/**
	 * Setter method for the exceptionMode.
	 * @param exceptionMode the exceptionMode to set
	 */
	public void setExceptionMode(final ForeachHandlingMode exceptionMode) {
		this.exceptionMode = ParameterUtil.ensureNotNull(exceptionMode, "exceptionMode");;
	}

	/**
	 * This is the "main" method of this class and should be invoked
	 * by subclasses.
	 * 
	 * @param parameter internal per-call parameter
	 */
	protected final void run(final P parameter) {

		// validate
		ObjectStateUtil.nullNotAllowed(exceptionMode, "exceptionMode");
		onValidate(parameter);

		// run the internal implementation of this method either with or
		// without exception handling
		if (exceptionMode.isIncludesGlobal()) {
			try {
				handleListAndContextWork(parameter);
			} catch (final Exception e) {
				onGlobalException(parameter, e);
			}
		} else {
			handleListAndContextWork(parameter);
		}

	}

	/**
	 * This method is invoked even before exception handling and should
	 * validate the fields of this operation itself. If any invalid fields
	 * are found, this method should throw an exception; this exception
	 * will be propagated to the caller of the operation (typically an
	 * {@link IllegalStateException}).
	 * 
	 * @param parameter internal per-call parameter
	 */
	protected void onValidate(final P parameter) {
	}

	/**
	 * This method is invoked when a list-level exception was caught.
	 * The default implementation logs the exception at "error" level.
	 * 
	 * @param parameter internal per-call parameter
	 * @param e the exception
	 */
	protected void onGlobalException(final P parameter, final Exception e) {
		logger.error("an exception occurred", e);
	}

	/**
	 * This method allows subclasses to add context handling around handling for the
	 * whole list. Implementations must eventually invoke determineListThenLoop().
	 * The default implementation just invokes that method.
	 * 
	 * @param parameter internal per-call parameter
	 */
	protected void handleListAndContextWork(final P parameter) {
		determineListThenLoop(parameter);
	}
	
	/**
	 * This method actually invokes determineList() to fetch the list, then loops
	 * over its elements. 
	 * @param parameter internal per-call parameter
	 * @return the element list
	 */
	protected final List<T> determineListThenLoop(final P parameter) {
		final List<T> list = determineList(parameter);
		ReturnValueUtil.nullNotAllowed(list, "determineList");
		onListObtained(parameter, list);
		for (final T element : list) {
			if (exceptionMode.isIncludesLocal()) {
				try {
					handleElementAndContextWork(parameter, element);
				} catch (final Exception e) {
					onLocalException(parameter, element, e);
				}
			} else {
				handleElementAndContextWork(parameter, element);
			}
		}
		return list;
	}

	/**
	 * This method is invoked when the element list has been obtained.
	 * @param parameter internal per-call parameter
	 */
	protected void onListObtained(final P parameter, final List<T> list) {
	}

	/**
	 * Determines the list to loop over.
	 * 
	 * @param parameter internal per-call parameter
	 * @return the list
	 */
	protected abstract List<T> determineList(P parameter);

	/**
	 * This method is invoked when an element-level exception was caught.
	 * The default implementation logs the exception at "error" level.
	 * 
	 * @param parameter internal per-call parameter
	 * @param element the element for which the exception occurred
	 * @param e the exception
	 */
	protected void onLocalException(final P parameter, final T element, final Exception e) {
		logger.error("an exception occurred", e);
	}

	/**
	 * This method does "context work" contributed by subclasses, then eventually
	 * invokes onElement() for the element.
	 * 
	 * Subclasses may override this method and/or onElement(). This raises the
	 * question when to override which of them:
	 * - override onElement() in the concrete subclass to do the actual work
	 * - override handleElementAndContextWork() in abstract subclasses of this class
	 *   to establish the context for onElement().
	 * 
	 * For example, an abstract subclass of this class that adds JDBC transactions
	 * would override handleElementAndContextWork(), since transactions must be
	 * established "around" onElement(). Had the subclass done transaction handling
	 * in onElement(), then its concrete subclass could not insert code "between"
	 * the transaction handling statements, only "around" them.
	 * 
	 * The default implementation just invokes onElement().
	 * 
	 * @param parameter internal per-call parameter
	 * @param element the element
	 */
	protected void handleElementAndContextWork(final P parameter, final T element) {
		onElement(parameter, element);
	}
	
	/**
	 * This method does the actual work for an element.
	 * 
	 * @param parameter internal per-call parameter
	 * @param element the element
	 */
	protected abstract void onElement(final P parameter, final T element);
	
}
