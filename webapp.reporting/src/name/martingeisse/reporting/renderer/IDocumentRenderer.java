/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.renderer;

import name.martingeisse.reporting.document.ChartBlock;
import name.martingeisse.reporting.document.Document;
import name.martingeisse.reporting.document.FormattedCompoundInlineItem;
import name.martingeisse.reporting.document.Paragraph;
import name.martingeisse.reporting.document.Section;
import name.martingeisse.reporting.document.Table;

/**
 * This interface is used to render documents. The document itself
 * loops through all its contents and invokes appropriate methods
 * of the renderer for everything it encounters. Note that some
 * document items cannot be rendered and throw an exception
 * instead of using this interface.
 */
public interface IDocumentRenderer {

	/**
	 * This method is invoked when rendering a document begins.
	 * @param document the document being rendered
	 */
	public void onBeginDocument(Document document);
	
	/**
	 * This method is invoked when rendering a document ends.
	 * @param document the document being rendered
	 */
	public void onEndDocument(Document document);

	/**
	 * This method is invoked when rendering a section begins.
	 * @param section the section being rendered
	 */
	public void onBeginSection(Section section);
	
	/**
	 * This method is invoked when rendering a section ends.
	 * @param section the section being rendered
	 */
	public void onEndSection(Section section);

	/**
	 * This method is invoked when rendering a paragraph begins.
	 * @param paragraph the paragraph being rendered
	 */
	public void onBeginParagraph(Paragraph paragraph);
	
	/**
	 * This method is invoked when rendering a paragraph ends.
	 * @param paragraph the paragraph being rendered
	 */
	public void onEndParagraph(Paragraph paragraph);

	/**
	 * This method is invoked when rendering a formatted inline item begins.
	 * @param item the formatted inline item being rendered
	 */
	public void onBeginFormattedInlineItem(FormattedCompoundInlineItem item);
	
	/**
	 * This method is invoked when rendering a formatted inline item ends.
	 * @param item the formatted inline item being rendered
	 */
	public void onEndFormattedInlineItem(FormattedCompoundInlineItem item);

	/**
	 * This method is invoked for text content.
	 * @param text the text content
	 */
	public void onTextContent(String text);

	/**
	 * This method is invoked for a chart block.
	 * @param chartBlock the chart block
	 */
	public void onChartBlock(ChartBlock chartBlock);

	/**
	 * This method is invoked for a table.
	 * @param table the table
	 */
	public void onTable(Table table);
	
}
