/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.element;

import static org.lwjgl.opengl.GL14.glWindowPos2i;
import java.util.ArrayList;
import name.martingeisse.common.util.ParameterUtil;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.util.Color;
import name.martingeisse.stackd.client.system.Font;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

/**
 * This element draws a paragraph of text, breaking the text into lines
 * of the width requested by the enclosing element.
 */
public final class TextParagraph extends GuiElement {

	/**
	 * the NO_LINES
	 */
	private static final String[] NO_LINES = new String[0];
	
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
	 * the lines
	 */
	private String[] lines;

	/**
	 * Constructor.
	 */
	public TextParagraph() {
		this.font = null;
		this.color = Color.WHITE;
		this.text = "";
		this.lines = NO_LINES;
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
	public TextParagraph setFont(final Font font) {
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
	public TextParagraph setColor(final Color color) {
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
	public TextParagraph setText(final String text) {
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

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.GuiElement#finishLayoutSize(int, int)
	 */
	@Override
	public void requestSize(final int width, final int height) {
		
		// obtain the font
		final Font effectiveFont = getEffectiveFont();
		if (effectiveFont == null || text == null) {
			setSize(0, 0);
			return;
		}
		
		// break the text into lines
		final Gui gui = getGui();
		ArrayList<String> lines = new ArrayList<>();
		StringBuilder lineBuilder = new StringBuilder();
		for (String word : StringUtils.split(text.trim())) {
			if (lineBuilder.length() != 0) {
				lineBuilder.append(' ');
			}
			int previousCharacterCount = lineBuilder.length();
			lineBuilder.append(word);
			int newSize = gui.pixelsToUnits(effectiveFont.getStringWidth(lineBuilder.toString()));
			if (newSize > width) {
				lineBuilder.setLength(previousCharacterCount);
				lines.add(lineBuilder.toString());
				lineBuilder.setLength(0);
				lineBuilder.append(word);
			}
		}
		if (lineBuilder.length() > 0) {
			lines.add(lineBuilder.toString());
		}
		this.lines = lines.toArray(new String[lines.size()]);
		
		// determine the size of the paragraph
		final int lineHeight = gui.pixelsToUnits(effectiveFont.getCharacterHeight());
		setSize(width, lineHeight * this.lines.length);
			
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
			final Gui gui = getGui();
			final int x = gui.unitsToPixels(getAbsoluteX());
			final int y = getGui().getHeightPixels() - gui.unitsToPixels(getAbsoluteY());
			
			final int lineHeight = effectiveFont.getCharacterHeight();
			int i = 0;
			for (String line : lines) {
				glWindowPos2i(x, y - i * lineHeight);
				effectiveFont.drawText(line, 1.0f, Font.ALIGN_LEFT, Font.ALIGN_TOP);
				i++;
			}
			
		}
	}

}
