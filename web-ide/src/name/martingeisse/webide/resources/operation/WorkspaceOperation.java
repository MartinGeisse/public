/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

import name.martingeisse.webide.resources.ResourcePath;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Implementations of this class modify the workspace. Each workspace
 * operation is run from the outside by invoking the {@link #run()}
 * method.
 * 
 * Internally, this creates a {@link WorkspaceOperationContext} that
 * handles the overall modification performed by this class. This
 * context is passed to the perform() method to do actual work.
 * Concrete modification steps are performed by calling methods of
 * the context. The context is also used to cache information about
 * the resource storage. The bottom line is that the context is only
 * valid within perform(), and should not be saved for use outside
 * this method.
 * 
 * If an operation wishes to delegate to another operation, it should
 * invoke the perform() method of that operation from its own
 * perform() method, passing the context. It should never invoke
 * {@link #run()} from there, since that would create a new context.
 */
public abstract class WorkspaceOperation {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(WorkspaceOperation.class);
	
	/**
	 * Constructor.
	 */
	public WorkspaceOperation() {
	}
	
	/**
	 * Runs this operation by creating a new operation context for
	 * this operation and all operations it delegates to.
	 */
	public final void run() {
		WorkspaceOperationContext context = new WorkspaceOperationContext();
		logAndPerform(context);
		context.dispose();
	}
	
	/**
	 * Calls perform(), surrounded by DEBUG-level logging. Subclasses are encouraged
	 * to provide their own logging. They typically do not invoke the super method
	 * but call perform() directly.
	 */
	void logAndPerform(WorkspaceOperationContext context) {
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " begin");
		}
		perform(context);
		if (logger.isDebugEnabled()) {
			logger.debug(getClass().getSimpleName() + " end");
		}
	}

	/**
	 * Performs the work represented by this operation.
	 * @param context the context needed to make actual changes
	 * to the workspace.
	 */
	protected abstract void perform(WorkspaceOperationContext context);
	
	/**
	 * Checks whether DEBUG-level logging is enabled.
	 */
	static final boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	
	/**
	 * Logs the specified path at DEBUG level (optimized).
	 * @param prefix the prefix to log
	 * @param path the path to log
	 */
	static final void debug(String prefix, ResourcePath path) {
		if (logger.isDebugEnabled()) {
			logger.debug(prefix + ": " + path);
		}
	}
	
	/**
	 * Logs the specified path at DEBUG level (optimized).
	 * @param prefix the prefix to log
	 * @param paths the paths to log
	 */
	static final void debug(String prefix, ResourcePath[] paths) {
		if (logger.isDebugEnabled()) {
			logger.debug(prefix + ": " + StringUtils.join(paths, ", "));
		}
	}
	
	/**
	 * Checks whether TRACE-level logging is enabled.
	 */
	static final boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
	
	/**
	 * Logs the specified path at TRACE level (optimized).
	 * @param prefix the prefix to log
	 * @param path the path to log
	 */
	static final void trace(String prefix, ResourcePath path) {
		if (logger.isTraceEnabled()) {
			logger.trace(prefix + ": " + path);
		}
	}
	
	/**
	 * Logs the specified path at TRACE level (optimized).
	 * @param prefix the prefix to log
	 * @param paths the paths to log
	 */
	static final void trace(String prefix, ResourcePath[] paths) {
		if (logger.isTraceEnabled()) {
			logger.trace(prefix + ": " + StringUtils.join(paths, ", "));
		}
	}
	
}
