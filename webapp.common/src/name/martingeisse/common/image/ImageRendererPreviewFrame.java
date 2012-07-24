/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.image;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;

/**
 * This class allows a simple preview of a rendered image (used to
 * quickly test renderers).
 */
public class ImageRendererPreviewFrame extends Frame {

	/**
	 * the imageRenderer
	 */
	private IImageRenderer imageRenderer;
	
	/**
	 * Constructor.
	 * @param imageRenderer the image renderer
	 */
	public ImageRendererPreviewFrame(IImageRenderer imageRenderer) {
		this.imageRenderer = imageRenderer;
		setTitle("Image Renderer Preview");
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

	/**
	 */
	private class AdjustSizeException extends RuntimeException {
	}
	
	/**
	 * 
	 */
	public void adjustSize() {
		try {
			imageRenderer.render(new IImageBackendType() {
				@Override
				public IImageBackend createBackend(int width, int height) {
					ImageRendererPreviewFrame.this.setSize(width, height);
					throw new AdjustSizeException();
				}
			});
		} catch (AdjustSizeException e) {
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics graphics) {
		graphics.translate(0, 22);
		imageRenderer.render(new MyImageBackendType((Graphics2D)graphics));
		super.paint(graphics);
	}

	/**
	 * The specialized image backend type used by this previewer.
	 */
	private class MyImageBackendType implements IImageBackendType {
		
		/**
		 * the graphics
		 */
		private Graphics2D graphics;
		
		/**
		 * Constructor.
		 * @param graphics the graphics context
		 */
		public MyImageBackendType(Graphics2D graphics) {
			this.graphics = graphics;
		}

		/* (non-Javadoc)
		 * @see name.martingeisse.terra.image.IImageBackendType#createBackend(int, int)
		 */
		@Override
		public IImageBackend createBackend(int width, int height) {
			return new AbstractImageBackend() {
				
				private boolean finished = false;
				
				@Override
				public Graphics2D getGraphics() throws IllegalStateException {
					mustNotBeFinished(); 
					return graphics;
				}
				
				@Override
				public IImageBackend finish() {
					mustNotBeFinished();
					finished = true;
					return this;
				}
				

				/* (non-Javadoc)
				 * @see name.martingeisse.terra.image.IImageBackend#writeTo(java.io.OutputStream)
				 */
				@Override
				public void writeTo(OutputStream s) {
					throw new UnsupportedOperationException();
				}

				private void mustNotBeFinished() {
					if (finished) {
						throw new IllegalStateException("backend is already finished");
					}
				}

			};
		}

	}
	
}
