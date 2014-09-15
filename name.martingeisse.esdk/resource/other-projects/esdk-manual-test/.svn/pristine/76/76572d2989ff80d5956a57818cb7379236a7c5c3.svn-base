/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.picotetris.model.canvas;

import java.io.IOException;

import org.eclipse.swt.widgets.Composite;

import name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler;
import name.martingeisse.swtlib.canvas.OpenGlImageBlockCanvas;
import name.martingeisse.swtlib.canvas.OpenGlImageBlockPalette;

/**
 * This class wraps the block canvas and the port handlers for the Tetris display.
 * 
 * The display contents are controlled by the PicoBlaze using two port handlers.
 * The first one is a one-address port handler that sets the current row.
 * The second one is a 64-address port handler, of which the first 40 are
 * valid, that can be used to set blocks.
 * 
 * Reading either register is currently not supported.
 */
public class TetrisBlockDisplay {

	/**
	 * the canvas
	 */
	private OpenGlImageBlockCanvas canvas;
	
	/**
	 * the rowRegisterPortHandler
	 */
	private final IPicoblazePortHandler rowRegisterPortHandler;
	
	/**
	 * the currentRow
	 */
	private int currentRow;
	
	/**
	 * the rowContentsPortHandler
	 */
	private final IPicoblazePortHandler rowContentsPortHandler;

	/**
	 * Constructor.
	 */
	public TetrisBlockDisplay() {
		this.canvas = null;
		this.rowRegisterPortHandler = new RowRegisterPortHandler();
		this.currentRow = 0;
		this.rowContentsPortHandler = new RowContentsPortHandler();
	}
	
	/**
	 * Creates the canvas widget.
	 * @param parent the parent composite
	 * @throws IOException on I/O errors
	 */
	public void createCanvas(Composite parent) throws IOException {
		// cannot pass the palette right in the constructor because OpenGL might not have been initialized yet
		canvas = new OpenGlImageBlockCanvas(parent, 16, 16, 40, 30, null);
		// now it *is* initialized
		canvas.setPalette(new OpenGlImageBlockPalette(64, "data/block$.png"));
	}
	
	/**
	 * Getter method for the canvas.
	 * @return the canvas
	 */
	public OpenGlImageBlockCanvas getCanvas() {
		return canvas;
	}
	
	/**
	 * Getter method for the rowRegisterPortHandler.
	 * @return the rowRegisterPortHandler
	 */
	public IPicoblazePortHandler getRowRegisterPortHandler() {
		return rowRegisterPortHandler;
	}
	
	/**
	 * Getter method for the rowContentsPortHandler.
	 * @return the rowContentsPortHandler
	 */
	public IPicoblazePortHandler getRowContentsPortHandler() {
		return rowContentsPortHandler;
	}
	
	/**
	 * Port handler implementation for the row register.
	 */
	private class RowRegisterPortHandler implements IPicoblazePortHandler {

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler#handleInput(int)
		 */
		@Override
		public int handleInput(int address) {
			return 0;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler#handleOutput(int, int)
		 */
		@Override
		public void handleOutput(int address, int value) {
			currentRow = value;
		}
		
	}

	/**
	 * Port handler implementation for the current row contents.
	 */
	private class RowContentsPortHandler implements IPicoblazePortHandler {

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler#handleInput(int)
		 */
		@Override
		public int handleInput(int address) {
			return 0;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.esdk.picoblaze.simulator.IPicoblazePortHandler#handleOutput(int, int)
		 */
		@Override
		public void handleOutput(int address, int value) {
			if (currentRow >= 0 && currentRow < 30 && address >= 0 && address < 40) {
				canvas.setBlock(address, currentRow, value);
				canvas.updateBlock(address, currentRow);
			}
		}
		
	}
	
}
