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
import name.martingeisse.reporting.document.FormattedCompoundInlineItem;
import name.martingeisse.reporting.document.InlineFormattingInstruction;
import name.martingeisse.reporting.document.Paragraph;
import name.martingeisse.reporting.document.Section;
import name.martingeisse.reporting.document.TextInlineItem;
import name.martingeisse.reporting.renderer.HtmlRenderer;

/**
 * TODO: document me
 *
 */
public class Main {

	/**
	 * @param args ...
	 */
	public static void main(String[] args) throws Exception {
		
		Section rootSection = new Section();
		rootSection.setTitle("The Document");

		Section section1 = new Section();
		rootSection.getSubsections().add(section1);
		section1.setTitle("Section One");
		Paragraph section1Paragraph = new Paragraph(new TextInlineItem("This is section 1 content"));
		section1.getDirectContents().getSubItems().add(section1Paragraph);

		Section section2 = new Section();
		rootSection.getSubsections().add(section2);
		section2.setTitle("Section Two");
		Paragraph section2Paragraph = new Paragraph(new TextInlineItem("This is section 2 content"));
		section2.getDirectContents().getSubItems().add(section2Paragraph);

		Section section2sub1 = new Section();
		section2.getSubsections().add(section2sub1);
		section2sub1.setTitle("Section Two (First Subsection)");
		Paragraph section2sub1Paragraph = new Paragraph(new TextInlineItem("This is section 2 sub 1 content"));
		section2sub1.getDirectContents().getSubItems().add(section2sub1Paragraph);

		FormattedCompoundInlineItem compound = new FormattedCompoundInlineItem(InlineFormattingInstruction.NONE);
		compound.getSubItems().add(new TextInlineItem("This is section 2 "));
		compound.getSubItems().add(new FormattedCompoundInlineItem(InlineFormattingInstruction.BOLD, new TextInlineItem("sub 2")));
		compound.getSubItems().add(new TextInlineItem(" content"));
		
		Section section2sub2 = new Section();
		section2.getSubsections().add(section2sub2);
		section2sub2.setTitle("Section Two (Second Subsection)");
		Paragraph section2sub2Paragraph = new Paragraph(compound);
		section2sub2.getDirectContents().getSubItems().add(section2sub2Paragraph);
		
		Section section3 = new Section();
		rootSection.getSubsections().add(section3);
		section3.setTitle("Section Three");
		Paragraph section3Paragraph = new Paragraph(new TextInlineItem("This is section 3 content"));
		section3.getDirectContents().getSubItems().add(section3Paragraph);
		
		Document document = new Document(rootSection);
		
		DataSources dataSources = new DataSources();
		dataSources.put("default", new JdbcDataSource("jdbc:mysql://localhost/phpbb", "root", ""));
		dataSources.connect();
		Document boundDocument = document.bindToData(dataSources);
		dataSources.disconnect();
		
		HtmlRenderer renderer = new HtmlRenderer();
		renderer.render(boundDocument, new File("test.html"));
	}
	
}
