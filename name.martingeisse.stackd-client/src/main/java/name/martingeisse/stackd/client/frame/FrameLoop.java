/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame;

import static org.lwjgl.opengl.GL11.glFlush;
import name.martingeisse.stackd.client.frame.handlers.HandlerList;
import name.martingeisse.stackd.client.frame.handlers.SwappableHandler;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;


/**
 * Performs a loop, typically drawing the world each
 * frame and potentially doing other things. At a lower
 * level, this loop object keeps a hierarchy of handlers that
 * are executed each frame. The hierarchy is anchored in a
 * single root handler of type {@link SwappableHandler} that allows
 * to put an arbitrary application handler -- typically itself
 * a {@link HandlerList} -- in place.
 * 
 * This class can optionally store a GL worker loop. If this is the case, then
 * the following things happen:
 * 
 * - the worker loop gets passed to all frame handlers when drawing, so the
 *   frame handlers can pass drawing WUs to the worker loop
 * 
 * - the frame loop's call to {@link Display#update()} and to
 *   {@link Display#processMessages()} is passed to the worker
 *   loop instead of executed directly
 *   
 * - if the worker loop is overloaded, whole drawing phases are skipped, including
 *   calls to {@link IFrameHandler#onBeforeDraw(GlWorkerLoop)} and
 *   {@link IFrameHandler#onAfterDraw(GlWorkerLoop)}.
 *   
 * - after drawing, this class adds a frame boundary marker to the GL worker loop
 * 
 */
public final class FrameLoop {

	/**
	 * the rootHandler
	 */
	private final SwappableHandler rootHandler;
	
	/**
	 * the glWorkerLoop
	 */
	private final GlWorkerLoop glWorkerLoop;
	
	/**
	 * Constructor.
	 */
	public FrameLoop() {
		this(null);
	}
	
	/**
	 * Constructor.
	 * @param glWorkerLoop optional GL worker loop as explained in the class comment
	 */
	public FrameLoop(GlWorkerLoop glWorkerLoop) {
		this.rootHandler = new SwappableHandler();
		this.glWorkerLoop = glWorkerLoop;
	}
	
	/**
	 * Getter method for the rootHandler.
	 * @return the rootHandler
	 */
	public SwappableHandler getRootHandler() {
		return rootHandler;
	}
	
	/**
	 * Executes a single frame, executing all handlers.
	 * @throws BreakFrameLoopException if a handler wants to break the frame loop
	 */
	public void executeFrame() throws BreakFrameLoopException {
		
		// draw all handlers
		try {
			if (glWorkerLoop == null || !glWorkerLoop.isOverloaded()) {
				rootHandler.onBeforeDraw(glWorkerLoop);
				rootHandler.draw(glWorkerLoop);
				rootHandler.onAfterDraw(glWorkerLoop);
				if (glWorkerLoop != null) {
					// TODO don't create a new object every frame
					glWorkerLoop.getQueue().add(new GlWorkUnit() {
						@Override
						public void execute() {
							glFlush();
							Display.update();
							Display.processMessages();
						}
					});
					glWorkerLoop.scheduleFrameBoundary();
				} else {
					glFlush();
					Display.update();
					Display.processMessages();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("unexpected exception while drawing", e);
		}
		
		// handle inputs and OS messages
		Mouse.poll();
		Keyboard.poll();
		
		// prepare game logic steps
		try {
			rootHandler.onBeforeHandleStep();
			rootHandler.handleStep();
			rootHandler.onAfterHandleStep();
		} catch (BreakFrameLoopException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("unexpected exception during onBeforeHandleStep()", e);
		}
		
	}

	/**
	 * Executes frames using {@link #executeFrame()} endlessly
	 * until one of the handlers throws a {@link BreakFrameLoopException}.
	 * 
	 * @param fixedFrameInterval the fixed minimum length of each frame in
	 * milliseconds, or null to run as many frames as possible
	 */
	public void executeLoop(Integer fixedFrameInterval) {
		FrameTimer frameTimer = (fixedFrameInterval == null ? null : new FrameTimer(fixedFrameInterval));
		try {
			while (true) {
				executeFrame();
				if (frameTimer != null) {
					while (!frameTimer.test()) {
						synchronized(frameTimer) {
							frameTimer.wait();
						}
					}
				}
			}
		} catch (InterruptedException e) {
		} catch (BreakFrameLoopException e) {
		}
	}
	
}
