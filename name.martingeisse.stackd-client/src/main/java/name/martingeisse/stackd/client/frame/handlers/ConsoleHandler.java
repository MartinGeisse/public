/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame.handlers;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_VIEWPORT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL14.glWindowPos2i;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import name.martingeisse.stackd.client.console.Console;
import name.martingeisse.stackd.client.frame.AbstractVisiblityToggleFrameHandler;
import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.system.Font;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * Shows a {@link Console}.
 */
public class ConsoleHandler extends AbstractVisiblityToggleFrameHandler {

	/**
	 * the console
	 */
	private final Console console;

	/**
	 * the font
	 */
	private final Font font;

	/**
	 * the screenHeight
	 */
	private final int screenHeight;

	/**
	 * the glWorkUnit
	 */
	private final GlWorkUnit glWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			
			// draw background
			glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPushMatrix();
			glLoadIdentity();
			glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPushMatrix();
			glLoadIdentity();
			glPolygonMode(GL_FRONT_AND_BACK, GL11.GL_FILL);
			glDisable(GL_TEXTURE_2D);
			glDisable(GL_DEPTH_TEST);
			GL11.glColor3f(0.4f, 0.4f, 0.4f);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex3i(-1, +1, 0);
			GL11.glVertex3i(+1, +1, 0);
			GL11.glVertex3i(+1, 0, 0);
			GL11.glVertex3i(-1, 0, 0);
			GL11.glEnd();
			glMatrixMode(GL11.GL_PROJECTION);
			GL11.glPopMatrix();
			glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glPopMatrix();

			// draw text
			glBindTexture(GL_TEXTURE_2D, 0);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			GL11.glPixelTransferf(GL11.GL_RED_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.7f);
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 1.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.7f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_BIAS, 0.0f);
			int i = 0;
			for (final String line : console.getCurrentOutputLines()) {
				glWindowPos2i(10, screenHeight - 10 - i * (font.getCharacterHeight() + 2));
				font.drawText(line, 1, Font.ALIGN_LEFT, Font.ALIGN_TOP);
				i++;
			}
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, 1.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.7f);
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.7f);
			glWindowPos2i(10, screenHeight/2 + font.getCharacterHeight() + 4);
			font.drawText(console.getCurrentInputLine().toString(), 1, Font.ALIGN_LEFT, Font.ALIGN_TOP);
			GL11.glPixelTransferf(GL11.GL_RED_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.0f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_BIAS, 0.0f);
			
		}
	};
	
	/**
	 * Constructor.
	 * @param console the console
	 * @param font the font used to draw the console
	 * @param toggleKey the key that toggles visibility
	 */
	public ConsoleHandler(final Console console, final Font font, final int toggleKey) {
		super(toggleKey);
		this.console = console;
		this.font = font;

		final IntBuffer buffer = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder()).asIntBuffer();
		glGetInteger(GL_VIEWPORT, buffer);
		this.screenHeight = buffer.get(3);
	}

	/**
	 * Getter method for the console.
	 * @return the console
	 */
	public Console getConsole() {
		return console;
	}

	/**
	 * Getter method for the font.
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#draw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void draw(GlWorkerLoop glWorkerLoop) {
		if (isVisible()) {
			glWorkerLoop.schedule(glWorkUnit);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractVisiblityToggleFrameHandler#handleStep()
	 */
	@Override
	public void handleStep() throws BreakFrameLoopException {
		super.handleStep();
		while (Keyboard.next()) {
			if (isVisible() && Keyboard.getEventKeyState()) {
				char c = Keyboard.getEventCharacter();
				console.consumeInputCharacter(c);
			} // else: consume characters anyway
		}
	}

}
