/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.server.tools;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * This class allows a simple preview of a rendered image.
 */
public class ImagePreviewFrame extends Frame {

	/**
	 * the BufferedImage image
	 */
	private BufferedImage image;
	
	/**
	 * Constructor.
	 * @param image the image
	 */
	public ImagePreviewFrame(BufferedImage image) {
		this.image = image;
		setTitle("Image Preview");
		setBackground(Color.orange);
		setSize(800, 600);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics graphics) {
		graphics.translate(0, 22);
		graphics.drawImage(image, 0, 0, null);
		super.paint(graphics);
	}

}
