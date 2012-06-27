/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting;

import java.io.File;

import name.martingeisse.reporting.datasource.DataSources;
import name.martingeisse.reporting.datasource.JdbcDataSource;
import name.martingeisse.reporting.document.Document;
import name.martingeisse.reporting.parser.ReportDefinitionParser;
import name.martingeisse.reporting.renderer.HtmlRenderer;

/**
 * The test main method.
 */
public class Main {

	/**
	 * @param args ...
	 * @throws Exception ...
	 */
	public static void main(String[] args) throws Exception {
		
		// read the report definition
		Document document = ReportDefinitionParser.parse(new File("report.xml"));
		
		// bind to the data sources
		DataSources dataSources = new DataSources();
		dataSources.put("default", new JdbcDataSource("jdbc:mysql://localhost/phpbb", "root", ""));
		dataSources.connect();
		Document boundDocument = document.bindToData(dataSources);
		dataSources.disconnect();
		
		// render as HTML
		HtmlRenderer renderer = new HtmlRenderer();
		renderer.render(boundDocument, new File("test.html"));
		
	}
	
}
