/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.definition.keycount;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.document.ChartBlock;
import name.martingeisse.reporting.document.IBlockItem;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

/**
 * This item generates a {@link ChartBlock} from an {@link IKeyCountQuery}.
 */
public class UnboundChartBlock implements IBlockItem {

	/**
	 * the query
	 */
	private IKeyCountQuery query;

	/**
	 * Constructor.
	 */
	public UnboundChartBlock() {
	}

	/**
	 * Constructor.
	 * @param query the query
	 */
	public UnboundChartBlock(final IKeyCountQuery query) {
		this.query = query;
	}

	/**
	 * Getter method for the query.
	 * @return the query
	 */
	public IKeyCountQuery getQuery() {
		return query;
	}

	/**
	 * Setter method for the query.
	 * @param query the query to set
	 */
	public void setQuery(final IKeyCountQuery query) {
		this.query = query;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.reporting.document.IDataBindable#bindToData(name.martingeisse.reporting.datasource.DataSources)
	 */
	@Override
	public ChartBlock bindToData(final DataSources dataSources) {
		
		// generate the dataset from the query
		DefaultPieDataset dataset = new DefaultPieDataset();
		IKeyCountResultSet resultSet = query.bindToData(dataSources);
		while (resultSet.next()) {
			KeyCountEntry entry = resultSet.get();
			dataset.setValue(entry.getKey(), entry.getCount());
		}
		resultSet.close();
		
		// generate the chart
        JFreeChart chart = ChartFactory.createPieChart("My Title", dataset, true, true, false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setStartAngle(290);
        plot.setDirection(Rotation.CLOCKWISE);
        plot.setForegroundAlpha(0.8f);
		return new ChartBlock(chart);
		
	}

}
