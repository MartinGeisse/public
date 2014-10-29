/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.esdk.simulation.lwjgl;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL14.glWindowPos2i;

import org.lwjgl.opengl.GL11;

import name.martingeisse.esdk.simulation.resources.Font;
import name.martingeisse.esdk.simulation.resources.StaticResources;

/**
 * Simulates a character matrix display that stores an ascii code
 * (in the least-significant byte) and an attribute byte (in the
 * second-least significant byte) per cell. 
 * 
 * TODO does not simulate blinking characters -- are these a
 * good idea anyway? Instead uses the same scheme for background
 * colors as for foreground colors.
 */
public class CharacterMatrixSimulatorGui extends Int32MatrixSimulatorGui {

	/**
	 * the COLOR_TABLE
	 */
	private static final float[] COLOR_TABLE = {
		0.0f, 0.0f, 0.0f, // 0 -- black
		0.0f, 0.0f, 0.5f, // 1 -- dark blue
		0.0f, 0.5f, 0.0f, // 2 -- dark green
		0.0f, 0.5f, 0.5f, // 3 -- dark cyan
		0.5f, 0.0f, 0.0f, // 4 -- dark red
		0.5f, 0.0f, 0.5f, // 5 -- dark purple
		0.8f, 0.4f, 0.0f, // 6 -- brown
		0.8f, 0.8f, 0.8f, // 7 -- light grey ("dark white")
		0.4f, 0.4f, 0.4f, // 8 -- dark grey ("light black")
		0.0f, 0.0f, 1.0f, // 9 -- light blue
		0.0f, 1.0f, 0.0f, // a -- light green
		0.0f, 1.0f, 1.0f, // b -- light cyan
		1.0f, 0.0f, 0.0f, // c -- light red
		1.0f, 0.0f, 1.0f, // d -- light purple
		1.0f, 1.0f, 0.0f, // e -- yellow
		1.0f, 1.0f, 1.0f, // f -- white
	};
	
	/**
	 * Constructor.
	 * @param widthInCells the display width in cells
	 * @param heightInCells the display height in cells
	 */
	public CharacterMatrixSimulatorGui(int widthInCells, int heightInCells) {
		super(widthInCells, heightInCells, 8, 16);
		StaticResources.load();
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.esdk.simulation.lwjgl.GraphicsSimulatorGui#redraw()
	 */
	@Override
	protected final void redraw() {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		int widthInCells = getWidthInCells();
		int heightInCells = getHeightInCells();
		int heightInPixels = getHeightInPixels();
		Font font = StaticResources.getInstance().getFont();
		for (int x=0; x<widthInCells; x++) {
			for (int y=0; y<heightInCells; y++) {
				int value = getCellValue(x, y);
				
				// draw background box
				glDisable(GL_BLEND);
				int backgroundBase = ((value >> 12) & 15) * 3;
				glWindowPos2i(x * 8, heightInPixels - y * 16);
				GL11.glPixelTransferf(GL11.GL_RED_BIAS, COLOR_TABLE[backgroundBase]);
				GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, COLOR_TABLE[backgroundBase + 1]);
				GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, COLOR_TABLE[backgroundBase + 2]);
				font.drawText(" ", 1.0f, Font.ALIGN_LEFT, Font.ALIGN_TOP);
				
				// draw characters
				glEnable(GL_BLEND);
				int foregroundBase = ((value >> 8) & 15) * 3;
				glWindowPos2i(x * 8, heightInPixels - y * 16);
				GL11.glPixelTransferf(GL11.GL_RED_BIAS, COLOR_TABLE[foregroundBase]);
				GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, COLOR_TABLE[foregroundBase + 1]);
				GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, COLOR_TABLE[foregroundBase + 2]);
				font.drawText(Character.toString((char)(value & 0xff)), 1.0f, Font.ALIGN_LEFT, Font.ALIGN_TOP);
				
			}
		}
		GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.0f);
		GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.0f);
		GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.0f);
	}

}
