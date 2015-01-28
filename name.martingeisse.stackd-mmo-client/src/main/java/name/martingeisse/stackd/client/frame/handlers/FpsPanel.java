/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame.handlers;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL14.glWindowPos2i;
import name.martingeisse.stackd.client.frame.AbstractFrameHandler;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.system.Font;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * Displays the current frames per second on the screen.
 */
public final class FpsPanel extends AbstractFrameHandler {

	/**
	 * the font
	 */
	private final Font font;
	
	/**
	 * the lastSamplingTime
	 */
	private long lastSamplingTime;

	/**
	 * the countedFrames
	 */
	private int countedFrames;
	
	/**
	 * the fps
	 */
	private int fps;
	
	/**
	 * the glWorkUnit
	 */
	private final GlWorkUnit glWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			
			// update FPS (in the drawing thread, so we don't count skipped frames!)
			countedFrames++;
			long now = System.currentTimeMillis();
			if ((now - lastSamplingTime) >= 1000) {
				fps = countedFrames;
				countedFrames = 0;
				lastSamplingTime = now;
			}
			
			// draw the FPS panel
			String fpsText = Float.toString(fps);
			glBindTexture(GL_TEXTURE_2D, 0);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glWindowPos2i(Display.getWidth(), Display.getHeight());
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, 1.0f);			
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 1.0f);			
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 1.0f);			
			font.drawText(fpsText, 2, Font.ALIGN_RIGHT, Font.ALIGN_TOP);
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.0f);			
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.0f);			
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.0f);
			
		}
	};
	
	/**
	 * Constructor.
	 * @param font the font used to draw text
	 */
	public FpsPanel(Font font) {
		this.font = font;
		this.lastSamplingTime = System.currentTimeMillis();
		this.countedFrames = 0;
		this.fps = 0;
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#draw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void draw(GlWorkerLoop glWorkerLoop) {
		glWorkerLoop.schedule(glWorkUnit);
	}
	
}
