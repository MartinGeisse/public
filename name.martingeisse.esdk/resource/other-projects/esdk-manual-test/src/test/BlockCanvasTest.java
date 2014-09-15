/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package test;

import name.martingeisse.swtlib.application.AbstractSingleWindowApplication;
import name.martingeisse.swtlib.canvas.OpenGlImageBlockCanvas;
import name.martingeisse.swtlib.canvas.OpenGlImageBlockPalette;
import name.martingeisse.swtlib.layout.CenterLayout;

/**
 * TODO: document me
 *
 */
public class BlockCanvasTest extends AbstractSingleWindowApplication {

	/**
	 * @param args ...
	 * @throws Exception ...
	 */
	public static void main(String[] args) throws Exception {
		
		BlockCanvasTest app = new BlockCanvasTest();
		app.create();
		app.getShell().setLayout(new CenterLayout(false, false));
		
		OpenGlImageBlockCanvas canvas = new OpenGlImageBlockCanvas(app.getShell(), 16, 16, 30, 30, null);
		canvas.setPalette(new OpenGlImageBlockPalette(64, "data/block$.png"));
		
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				canvas.setBlock(i, j, j*8+i);
			}
		}
		
		app.open();
		app.mainLoop();
		app.exit();
		
	}
	
}
