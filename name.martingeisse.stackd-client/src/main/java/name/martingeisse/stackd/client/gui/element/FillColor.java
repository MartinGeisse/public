/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import name.martingeisse.stackd.client.gui.util.Color;
import org.lwjgl.opengl.GL11;

/**
 * This element fills its area with an RGBA color.
 */
public final class FillColor extends AbstractFillElement {

	/**
	 * the color
	 */
	private Color color;

	/**
	 * Constructor.
	 * @param color the color to fill with
	 */
	public FillColor(Color color) {
		this.color = color;
	}
	
	/**
	 * Getter method for the color.
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Setter method for the color.
	 * @param color the color to set
	 * @return this for chaining
	 */
	public FillColor setColor(Color color) {
		this.color = color;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.element.AbstractFillElement#draw()
	 */
	@Override
	protected void draw() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		color.glColor();
		int x = getAbsoluteX(), y = getAbsoluteY(), w = getWidth(), h = getHeight();
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2i(x, y);
		GL11.glVertex2i(x + w, y);
		GL11.glVertex2i(x + w, y + h);
		GL11.glVertex2i(x, y + h);
		GL11.glEnd();
	}
	
}
