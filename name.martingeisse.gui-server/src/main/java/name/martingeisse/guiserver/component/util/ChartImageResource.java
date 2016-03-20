/**
 * Copyright (c) 2013 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.component.util;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.jfree.chart.JFreeChart;

/**
 * Base image resource implementation for a dynamically rendered chart,
 * using SVG format.
 */
public abstract class ChartImageResource extends RenderedDynamicSvgImageResource {

	/**
	 * Constructor.
	 * @param width the image width
	 * @param height the image height
	 */
	public ChartImageResource(int width, int height) {
		super(width, height);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource#render(java.awt.Graphics2D, org.apache.wicket.request.resource.IResource.Attributes)
	 */
	@Override
	protected boolean render(Graphics2D graphics, Attributes attributes) {
		JFreeChart chart = createChart(attributes);
		if (chart == null) {
			return false;
		} else {
			chart.draw(graphics, new Rectangle(0, 0, getWidth(), getHeight()), null, null);
			return true;
		}
	}
	
	/**
	 * Creates the chart object.
	 * @param attributes the resource request attributes
	 * @return the chart, or null to re-start rendering (used if the size was changed by the rendering code)
	 */
	protected abstract JFreeChart createChart(Attributes attributes);

}
