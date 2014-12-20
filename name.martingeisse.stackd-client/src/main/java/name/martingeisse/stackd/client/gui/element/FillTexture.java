/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.system.StackdTexture;
import org.lwjgl.opengl.GL11;

/**
 * This element fills its area with a texture.
 */
public final class FillTexture extends AbstractFillElement {

	/**
	 * the texture
	 */
	private StackdTexture texture;

	/**
	 * the repetitionLengthX
	 */
	private int repetitionLengthX;

	/**
	 * the repetitionLengthY
	 */
	private int repetitionLengthY;

	/**
	 * Constructor.
	 * @param texture the texture to fill with
	 */
	public FillTexture(final StackdTexture texture) {
		setTexture(texture);
		this.repetitionLengthX = -1;
		this.repetitionLengthY = -1;
	}

	/**
	 * Constructor.
	 * @param texture the texture to fill with
	 * @param repetitionLengthX the x length to repeat the texture at
	 * @param repetitionLengthY the y length to repeat the texture at
	 */
	public FillTexture(final StackdTexture texture, final int repetitionLengthX, final int repetitionLengthY) {
		setTexture(texture);
		setRepetitionLengthX(repetitionLengthX);
		setRepetitionLengthY(repetitionLengthY);
	}

	/**
	 * Setter method for the repetitionLengthX.
	 * @param repetitionLengthX the repetitionLengthX to set
	 * @return this for chaining
	 */
	public FillTexture setRepetitionLengthX(final int repetitionLengthX) {
		if (repetitionLengthX < 1) {
			throw new IllegalArgumentException("invalid x repetition length: " + repetitionLengthX);
		}
		this.repetitionLengthX = repetitionLengthX;
		return this;
	}

	/**
	 * Setter method for the repetitionLengthY.
	 * @param repetitionLengthY the repetitionLengthY to set
	 * @return this for chaining
	 */
	public FillTexture setRepetitionLengthY(final int repetitionLengthY) {
		if (repetitionLengthY < 1) {
			throw new IllegalArgumentException("invalid y repetition length: " + repetitionLengthY);
		}
		this.repetitionLengthY = repetitionLengthY;
		return this;
	}

	/**
	 * Setter method for the repetitionLengths.
	 * @param repetitionLengthX the repetitionLengthX to set
	 * @param repetitionLengthY the repetitionLengthY to set
	 * @return this for chaining
	 */
	public FillTexture setRepetitionLengths(final int repetitionLengthX, final int repetitionLengthY) {
		setRepetitionLengthX(repetitionLengthX);
		setRepetitionLengthY(repetitionLengthY);
		return this;
	}

	/**
	 * Getter method for the texture.
	 * @return the texture
	 */
	public StackdTexture getTexture() {
		return texture;
	}

	/**
	 * Setter method for the texture.
	 * @param texture the texture to set
	 * @return this for chaining
	 */
	public FillTexture setTexture(final StackdTexture texture) {
		ParameterUtil.ensureNotNull(texture, "texture");
		this.texture = texture;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.element.AbstractFillElement#draw()
	 */
	@Override
	protected void draw() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		texture.glBindTexture();
		final int x = getAbsoluteX(), y = getAbsoluteY(), w = getWidth(), h = getHeight();
		final Gui gui = getGui();
		final float effectiveRepetitionLengthX = (repetitionLengthX < 1 ? gui.pixelsToUnitsInt(texture.getWidth()) : repetitionLengthX);
		final float effectiveRepetitionLengthY = (repetitionLengthY < 1 ? gui.pixelsToUnitsInt(texture.getHeight()) : repetitionLengthY);
		final float s = (w / effectiveRepetitionLengthX);
		final float t = (h / effectiveRepetitionLengthY);

		GL11.glColor3ub((byte)255, (byte)255, (byte)255);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex2i(x, y);
		GL11.glTexCoord2f(s, 0.0f);
		GL11.glVertex2i(x + w, y);
		GL11.glTexCoord2f(s, t);
		GL11.glVertex2i(x + w, y + h);
		GL11.glTexCoord2f(0.0f, t);
		GL11.glVertex2i(x, y + h);
		GL11.glEnd();
	}

}
