/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.document;

import name.martingeisse.reporting.datasource.DataSources;

import org.jfree.chart.JFreeChart;

/**
 * Adapter to embed JFreeChart-rendered charts in a document.
 */
public class ChartBlock implements IBlockItem {

	/**
	 * the chart
	 */
	private JFreeChart chart;

	/**
	 * Constructor.
	 */
	public ChartBlock() {
	}

	/**
	 * Constructor.
	 * @param chart the chart
	 */
	public ChartBlock(final JFreeChart chart) {
		this.chart = chart;
	}

	/**
	 * Getter method for the chart.
	 * @return the chart
	 */
	public JFreeChart getChart() {
		return chart;
	}

	/**
	 * Setter method for the chart.
	 * @param chart the chart to set
	 */
	public void setChart(final JFreeChart chart) {
		this.chart = chart;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public ChartBlock bindToData(DataSources dataSources) {
		return this;
	}
	
}
