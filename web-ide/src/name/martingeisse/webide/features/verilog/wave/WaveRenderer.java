/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.features.verilog.wave;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.List;

import name.martingeisse.common.image.IImageBackend;
import name.martingeisse.common.image.IImageBackendType;
import name.martingeisse.common.image.IImageRenderer;

/**
 * Renders a list of value changes to a PNG image. The times of the
 * value changes are interpreted as x pixel positions.
 * 
 * The width and height of the image are specified at construction
 * time. the starting position and the height of the wave graph
 * can be changed afterwards.
 * 
 * The list of value changes must be pre-sorted for this class
 * to work correctly.
 */
public class WaveRenderer implements IImageRenderer, Serializable {

	/**
	 * the imageWidth
	 */
	private int imageWidth;

	/**
	 * the imageHeight
	 */
	private int imageHeight;

	/**
	 * the valueChanges
	 */
	private List<ValueChange> valueChanges;

	/**
	 * the startX
	 */
	private int startX;

	/**
	 * the startY
	 */
	private int startY;

	/**
	 * the waveHeight
	 */
	private int waveHeight;

	/**
	 * Constructor.
	 * @param imageWidth the width of the output image
	 * @param imageHeight the height of the output image
	 * @param valueChanges the list of value changes (must be pre-sorted by time)
	 */
	public WaveRenderer(final int imageWidth, final int imageHeight, final List<ValueChange> valueChanges) {
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		this.valueChanges = valueChanges;
		this.startX = 0;
		this.startY = 0;
		this.waveHeight = imageHeight;
	}

	/**
	 * Getter method for the imageWidth.
	 * @return the imageWidth
	 */
	public int getImageWidth() {
		return imageWidth;
	}

	/**
	 * Setter method for the imageWidth.
	 * @param imageWidth the imageWidth to set
	 */
	public void setImageWidth(final int imageWidth) {
		this.imageWidth = imageWidth;
	}

	/**
	 * Getter method for the imageHeight.
	 * @return the imageHeight
	 */
	public int getImageHeight() {
		return imageHeight;
	}

	/**
	 * Setter method for the imageHeight.
	 * @param imageHeight the imageHeight to set
	 */
	public void setImageHeight(final int imageHeight) {
		this.imageHeight = imageHeight;
	}

	/**
	 * Getter method for the valueChanges.
	 * @return the valueChanges
	 */
	public List<ValueChange> getValueChanges() {
		return valueChanges;
	}

	/**
	 * Setter method for the valueChanges.
	 * @param valueChanges the valueChanges to set
	 */
	public void setValueChanges(final List<ValueChange> valueChanges) {
		this.valueChanges = valueChanges;
	}

	/**
	 * Getter method for the startX.
	 * @return the startX
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * Setter method for the startX.
	 * @param startX the startX to set
	 */
	public void setStartX(final int startX) {
		this.startX = startX;
	}

	/**
	 * Getter method for the startY.
	 * @return the startY
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * Setter method for the startY.
	 * @param startY the startY to set
	 */
	public void setStartY(final int startY) {
		this.startY = startY;
	}

	/**
	 * Getter method for the waveHeight.
	 * @return the waveHeight
	 */
	public int getWaveHeight() {
		return waveHeight;
	}

	/**
	 * Setter method for the waveHeight.
	 * @param waveHeight the waveHeight to set
	 */
	public void setWaveHeight(final int waveHeight) {
		this.waveHeight = waveHeight;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.common.image.IImageRenderer#render(name.martingeisse.common.image.IImageBackendType)
	 */
	@Override
	public IImageBackend render(IImageBackendType imageBackendType) {
		IImageBackend image = imageBackendType.createBackend(imageWidth, imageHeight);
		image.getGraphics().setColor(Color.BLACK);
		image.getGraphics().fillRect(0, 0, imageWidth, imageHeight);
		long currentTime = 0;
		Object currentValue = null;
		for (ValueChange change : valueChanges) {
			if (change.getTime() > currentTime) {
				render(image, currentTime, change.getTime(), currentValue);
				currentTime = change.getTime();
			}
			if (startX + currentTime >= imageWidth) {
				break;
			}
			currentValue = change.getValue();
		}
		if (startX + currentTime < imageWidth) {
			render(image, currentTime, imageWidth - startX + 1, currentValue);
		}
		return image;
	}
	
	/**
	 * Renders a span of time with a single value.
	 */
	private void render(IImageBackend image, long startTime, long endTime, Object value) {
		
		int x1 = startX + (int)startTime;
		int x2 = startX + (int)endTime;
		int w = (int)(endTime - startTime);
		int y1 = startY;
		int ym = startY + waveHeight / 2;
		int y2 = startY + waveHeight - 1;
		int h = waveHeight;

		Graphics2D g = image.getGraphics();
		g.setColor(Color.GREEN);
		g.drawLine(x1, y1, x1, y2);
		
		if (value == null) {
			g.setColor(Color.RED);
			g.drawLine(x1 + 1, ym, x2, ym);
		} else if (value instanceof Boolean) {
			boolean booleanValue = (Boolean)value;
			int y = booleanValue ? y1 : y2;
			g.drawLine(x1, y, x2, y);
		} else if (value == ValueChange.CHAOS) {
			g.drawLine(x1, y1, x2, y1);
			g.drawLine(x1, y2, x2, y2);
			g.setColor(new Color(0, 128, 0));
			g.fillRect(x1 + 1, y1 + 1, w - 1, h - 2);
		}
	}
	
}
