/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame.handlers;

import java.util.ArrayList;
import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import name.martingeisse.stackd.client.frame.IFrameHandler;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;

/**
 * This handler is a list of other handlers. Each method call is
 * forwarded to all of them.
 */
public class HandlerList extends ArrayList<IFrameHandler> implements IFrameHandler {

	/**
	 * Constructor.
	 */
	public HandlerList() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onBeforeHandleStep()
	 */
	@Override
	public void onBeforeHandleStep() {
		for (IFrameHandler handler : this) {
			handler.onBeforeHandleStep();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
		for (IFrameHandler handler : this) {
			handler.handleStep();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onAfterHandleStep()
	 */
	@Override
	public void onAfterHandleStep() {
		for (IFrameHandler handler : this) {
			handler.onAfterHandleStep();
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onBeforeDraw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void onBeforeDraw(GlWorkerLoop glWorkerLoop) {
		for (IFrameHandler handler : this) {
			handler.onBeforeDraw(glWorkerLoop);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#draw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void draw(GlWorkerLoop glWorkerLoop) {
		for (IFrameHandler handler : this) {
			handler.draw(glWorkerLoop);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onAfterDraw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void onAfterDraw(GlWorkerLoop glWorkerLoop) {
		for (IFrameHandler handler : this) {
			handler.onAfterDraw(glWorkerLoop);
		}
	}

}
