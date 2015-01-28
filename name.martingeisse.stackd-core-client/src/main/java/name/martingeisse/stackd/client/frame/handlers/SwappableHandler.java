/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame.handlers;

import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import name.martingeisse.stackd.client.frame.IFrameHandler;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;

/**
 * This is a simple handler that contains another wrapped handler
 * and allows to swap that wrapped handler during runtime.
 * All handler methods are simply forwarded to the wrapped handler,
 * or skipped when the wrapped handler was set to null.
 */
public final class SwappableHandler implements IFrameHandler {

	/**
	 * the wrappedHandler
	 */
	private IFrameHandler wrappedHandler;
	
	/**
	 * Constructor.
	 */
	public SwappableHandler() {
	}

	/**
	 * Constructor.
	 * @param wrappedHandler the wrapped handler
	 */
	public SwappableHandler(IFrameHandler wrappedHandler) {
		this.wrappedHandler = wrappedHandler;
	}

	/**
	 * Getter method for the wrappedHandler.
	 * @return the wrappedHandler
	 */
	public IFrameHandler getWrappedHandler() {
		return wrappedHandler;
	}
	
	/**
	 * Setter method for the wrappedHandler.
	 * @param wrappedHandler the wrappedHandler to set
	 */
	public void setWrappedHandler(IFrameHandler wrappedHandler) {
		this.wrappedHandler = wrappedHandler;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onBeforeHandleStep()
	 */
	@Override
	public void onBeforeHandleStep() {
		if (wrappedHandler != null) {
			wrappedHandler.onBeforeHandleStep();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
		if (wrappedHandler != null) {
			wrappedHandler.handleStep();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onAfterHandleStep()
	 */
	@Override
	public void onAfterHandleStep() {
		if (wrappedHandler != null) {
			wrappedHandler.onAfterHandleStep();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onBeforeDraw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void onBeforeDraw(GlWorkerLoop glWorkerLoop) {
		if (wrappedHandler != null) {
			wrappedHandler.onBeforeDraw(glWorkerLoop);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#draw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void draw(GlWorkerLoop glWorkerLoop) {
		if (wrappedHandler != null) {
			wrappedHandler.draw(glWorkerLoop);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onAfterDraw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void onAfterDraw(GlWorkerLoop glWorkerLoop) {
		if (wrappedHandler != null) {
			wrappedHandler.onAfterDraw(glWorkerLoop);
		}
	}

}
