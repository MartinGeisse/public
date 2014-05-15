/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import static org.lwjgl.opengl.GL14.glWindowPos2i;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.util.Color;
import name.martingeisse.stackd.client.system.Font;
import org.lwjgl.opengl.GL11;

/**
 * This element draws a line of text. Its size depends solely on the
 * text and is not affected during layout.
 */
public final class TextLine extends GuiElement {

	/**
	 * the font
	 */
	private Font font;

	/**
	 * the color
	 */
	private Color color;

	/**
	 * the text
	 */
	private String text;

	/**
	 * Constructor.
	 */
	public TextLine() {
		this.font = null;
		this.color = Color.WHITE;
		this.text = "";
	}

	/**
	 * Getter method for the font.
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * Setter method for the font.
	 * @param font the font to set
	 * @return this for chaining
	 */
	public TextLine setFont(final Font font) {
		ParameterUtil.ensureNotNull(font, "font");
		this.font = font;
		requestLayout();
		return this;
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
	public TextLine setColor(Color color) {
		ParameterUtil.ensureNotNull(color, "color");
		this.color = color;
		return this;
	}

	/**
	 * Getter method for the text.
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter method for the text.
	 * @param text the text to set
	 * @return this for chaining
	 */
	public TextLine setText(final String text) {
		ParameterUtil.ensureNotNull(text, "text");
		this.text = text;
		requestLayout();
		return this;
	}

	/**
	 * 
	 */
	private Font getEffectiveFont() {
		if (font == null) {
			final Gui gui = getGuiOrNull();
			return (gui == null ? null : gui.getDefaultFont());
		} else {
			return font;
		}
	}

	/**
	 * 
	 */
	private void computeSize() {
		final Font effectiveFont = getEffectiveFont();
		if (effectiveFont == null || text == null) {
			setSize(0, 0);
		} else {
			Gui gui = getGui();
			int textWidth = gui.pixelsToUnits(effectiveFont.getStringWidth(text));
			int textHeight = gui.pixelsToUnits(effectiveFont.getCharacterHeight());
			setSize(textWidth, textHeight);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	public void handleEvent(final GuiEvent event) {
		if (event == GuiEvent.DRAW) {
			final Font effectiveFont = getEffectiveFont();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glPixelTransferf(GL11.GL_RED_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_SCALE, 0.0f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_SCALE, 1.0f);
			GL11.glPixelTransferf(GL11.GL_RED_BIAS, color.getRed() / 255.0f);
			GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, color.getGreen() / 255.0f);
			GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, color.getBlue() / 255.0f);
			GL11.glPixelTransferf(GL11.GL_ALPHA_BIAS, 0.0f);
			
			// TODO scale font so text doesn't become smaller with higher resolution
			Gui gui = getGui();
			int x = gui.unitsToPixels(getAbsoluteX());
			int y = getGui().getHeightPixels() - gui.unitsToPixels(getAbsoluteY());
			glWindowPos2i(x, y);
			
			effectiveFont.drawText(text, 1.0f, Font.ALIGN_LEFT, Font.ALIGN_TOP);
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayoutSize(int, int)
	 */
	@Override
	public void requestSize(final int width, final int height) {
		computeSize();
	}

}
