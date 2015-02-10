/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.gui;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.guiserver.backend.BackendHttpClient;
import name.martingeisse.guiserver.configuration.Configuration;
import name.martingeisse.guiserver.configuration.content.PieChartConfiguration;

import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

/**
 * The image resource for a dynamically rendered pie chart.
 */
public final class PieChartImageResource extends RenderedDynamicImageResource {

	/**
	 * the snippetHandle
	 */
	private int snippetHandle;

	/**
	 * the cachedConfiguration
	 */
	private transient PieChartConfiguration cachedConfiguration;
	
	/**
	 * Constructor.
	 * @param width the image width
	 * @param height the image height
	 * @param configuration the configuration object
	 */
	public PieChartImageResource(int width, int height, PieChartConfiguration configuration) {
		super(width, height);
		this.snippetHandle = configuration.getSnippetHandle();
		this.cachedConfiguration = configuration;
	}
	
	/**
	 * Getter method for the configuration.
	 * @return the configuration.
	 */
	public final PieChartConfiguration getConfiguration() {
		if (cachedConfiguration == null) {
			cachedConfiguration = (PieChartConfiguration)Configuration.getInstance().getSnippet(snippetHandle);
		}
		return cachedConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource#render(java.awt.Graphics2D, org.apache.wicket.request.resource.IResource.Attributes)
	 */
	@Override
	protected boolean render(Graphics2D graphics, Attributes attributes) {
		
		// fetch data from the backend
		JsonAnalyzer response = BackendHttpClient.getJson(getConfiguration().getBackendUrl());
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (JsonAnalyzer segment : response.analyzeMapElement("segments").analyzeList()) {
			String title = segment.analyzeMapElement("title").expectString();
			double value = segment.analyzeMapElement("value").expectDouble();
			dataset.setValue(title, value);
		}
		
		// create the chart
		boolean legend = getConfiguration().isLegend();
		JFreeChart chart = ChartFactory.createPieChart(null, dataset, legend, false, false);
		PiePlot plot = (PiePlot)chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setShadowPaint(null);
		plot.setBackgroundPaint(null);
		plot.setOutlineVisible(false);

		// draw the chart
		chart.draw(graphics, new Rectangle(0, 0, getWidth(), getHeight()), null, null);

		return true;
	}

}
