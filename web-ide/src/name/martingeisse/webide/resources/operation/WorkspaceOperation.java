/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.operation;

/**
 * Implementations of this class modify the workspace. Each workspace
 * operation is run from the outside by invoking the {@link #run()}
 * method.
 * 
 * Internally, this creates an {@link IWorkspaceOperationContext} that
 * represents the overall modification performed by this class. This
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
 * {@link #run()} from there.
 */
public abstract class WorkspaceOperation {

	/**
	 * Constructor.
	 */
	public WorkspaceOperation() {
	}
	
	/**
	 * Runs this operation by creating a new operation context for
	 * this operation and all operations it delegates to.
	 */
	public void run() {
		WorkspaceOperationContext context = new WorkspaceOperationContext();
		perform(context);
		context.dispose();
	}

	/**
	 * Performs the work represented by this operation.
	 * @param context the context needed to make actual changes
	 * to the workspace.
	 */
	protected abstract void perform(IWorkspaceOperationContext context);
	
}
