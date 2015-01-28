/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.frame.handlers;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2d;
import name.martingeisse.stackd.client.frame.AbstractFrameHandler;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.system.StackdTexture;
import name.martingeisse.stackd.common.cubetype.CubeType;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;

/**
 * Draws a HUD element that shows a "selected" cube, whatever that
 * means -- the cube to display is simply set in this object.
 */
public final class SelectedCubeHud extends AbstractFrameHandler {

	/**
	 * the textures
	 */
	private final StackdTexture[] textures;
	
	/**
	 * the cubeTypes
	 */
	private final CubeType[] cubeTypes;

	/**
	 * the cubeTypeIndex
	 */
	private int cubeTypeIndex;

	/**
	 * the glWorkUnit
	 */
	private final GlWorkUnit glWorkUnit = new GlWorkUnit() {
		@Override
		public void execute() {
			
			glMatrixMode(GL_MODELVIEW);
			glLoadIdentity();
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glEnable(GL_TEXTURE_2D);
			
			double w = 1.0, h = 1.6, d = 0.5;
			double x = -0.8, y = 0.8;
			double scale = 0.10;
			w *= scale;
			h *= scale;
			d *= scale;
			
			CubeType cubeType = cubeTypes[cubeTypeIndex];

			textures[cubeType.getCubeFaceTextureIndex(AxisAlignedDirection.POSITIVE_Z)].glBindTexture();
			glBegin(GL_QUADS);
			glTexCoord2f(0.0f, 0.0f);
			glVertex2d(x, y-h);
			glTexCoord2f(0.0f, 1.0f);
			glVertex2d(x, y);
			glTexCoord2f(1.0f, 1.0f);
			glVertex2d(x-w, y+d);
			glTexCoord2f(1.0f, 0.0f);
			glVertex2d(x-w, y-h+d);
			glEnd();
			
			textures[cubeType.getCubeFaceTextureIndex(AxisAlignedDirection.POSITIVE_X)].glBindTexture();
			glBegin(GL_QUADS);
			glTexCoord2f(0.0f, 0.0f);
			glVertex2d(x, y);
			glTexCoord2f(0.0f, 1.0f);
			glVertex2d(x, y-h);
			glTexCoord2f(1.0f, 1.0f);
			glVertex2d(x+w, y-h+d);
			glTexCoord2f(1.0f, 0.0f);
			glVertex2d(x+w, y+d);
			glEnd();

			textures[cubeType.getCubeFaceTextureIndex(AxisAlignedDirection.POSITIVE_Y)].glBindTexture();
			glBegin(GL_QUADS);
			glTexCoord2f(0.0f, 0.0f);
			glVertex2d(x, y);
			glTexCoord2f(0.0f, 1.0f);
			glVertex2d(x+w, y+d);
			glTexCoord2f(1.0f, 1.0f);
			glVertex2d(x, y+d+d);
			glTexCoord2f(1.0f, 0.0f);
			glVertex2d(x-w, y+d);
			glEnd();
			
		}
	};
	
	/**
	 * Constructor.
	 * @param textures the textures
	 * @param cubeTypes the cube types
	 */
	public SelectedCubeHud(final StackdTexture[] textures, final CubeType[] cubeTypes) {
		this.textures = textures;
		this.cubeTypes = cubeTypes;
	}
	
	/**
	 * Getter method for the textures.
	 * @return the textures
	 */
	public StackdTexture[] getTextures() {
		return textures;
	}
	
	/**
	 * Getter method for the cubeTypes.
	 * @return the cubeTypes
	 */
	public CubeType[] getCubeTypes() {
		return cubeTypes;
	}

	/**
	 * Getter method for the cubeTypeIndex.
	 * @return the cubeTypeIndex
	 */
	public int getCubeTypeIndex() {
		return cubeTypeIndex;
	}
	
	/**
	 * Setter method for the cubeTypeIndex.
	 * @param cubeTypeIndex the cubeTypeIndex to set
	 */
	public void setCubeTypeIndex(int cubeTypeIndex) {
		this.cubeTypeIndex = cubeTypeIndex;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#draw(name.martingeisse.glworker.GlWorkerLoop)
	 */
	@Override
	public void draw(GlWorkerLoop glWorkerLoop) {
		glWorkerLoop.schedule(glWorkUnit);
	}

}
