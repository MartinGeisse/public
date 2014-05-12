/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui;

import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glGetInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import name.martingeisse.stackd.client.frame.AbstractFrameHandler;
import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * This handler draws the GUI and sends events to it.
 */
public final class GuiFrameHandler extends AbstractFrameHandler {

	/**
	 * the gui
	 */
	private final Gui gui;

	/**
	 * the drawWorkUnit
	 */
	private final GlWorkUnit drawWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			drawInternal();
		}
	};
	
	/**
	 * Constructor.
	 */
	public GuiFrameHandler() {
		final IntBuffer buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		glGetInteger(GL_VIEWPORT, buffer);
		int width = buffer.get(2);
		int height = buffer.get(3);
		this.gui = new Gui(width, height);
	}
	
	/**
	 * Getter method for the gui.
	 * @return the gui
	 */
	public Gui getGui() {
		return gui;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#draw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void draw(final GlWorkerLoop glWorkerLoop) {
		glWorkerLoop.schedule(drawWorkUnit);
	}
	
	/**
	 * Called in the OpenGL thread.
	 */
	private void drawInternal() {

		// render nifty
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 600, 0, -1, 1);
		gui.draw();
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
		
		// dispatch keyboard events
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				gui.handleKeyboardEvent(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
		}
		
		// dispatch mouse events
		while (Mouse.next()) {
			if (Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0) {
				gui.handleMouseMovement(Mouse.getEventX(), gui.getHeight() - Mouse.getEventY());
			}
			if (Mouse.getEventButton() != -1 && Mouse.getEventButtonState()) {
				gui.handleMouseClick();
			}
		}
		
		// handle pending followup actions
		gui.executeFollowupActions();
		
	}
	
}
