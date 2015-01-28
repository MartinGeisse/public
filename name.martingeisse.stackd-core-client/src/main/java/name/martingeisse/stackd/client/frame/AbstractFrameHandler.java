/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame;

import name.martingeisse.stackd.client.glworker.GlWorkerLoop;

/**
 * Base implementation of {@link IFrameHandler}.
 */
public abstract class AbstractFrameHandler implements IFrameHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.frame.IFrameHandler#onBeforeHandleStep()
	 */
	@Override
	public void onBeforeHandleStep() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.frame.IFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.frame.IFrameHandler#onAfterHandleStep()
	 */
	@Override
	public void onAfterHandleStep() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onBeforeDraw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void onBeforeDraw(GlWorkerLoop glWorkerLoop) {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#draw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void draw(GlWorkerLoop glWorkerLoop) {
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.IFrameHandler#onAfterDraw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void onAfterDraw(GlWorkerLoop glWorkerLoop) {
	}

}
