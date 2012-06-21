/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.reporting.renderer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Stack;

import name.martingeisse.reporting.document.Document;
import name.martingeisse.reporting.document.FormattedCompoundInlineItem;
import name.martingeisse.reporting.document.IBlockItem;
import name.martingeisse.reporting.document.IInlineItem;
import name.martingeisse.reporting.document.Paragraph;
import name.martingeisse.reporting.document.Section;
import name.martingeisse.reporting.document.TextInlineItem;

/**
 * TODO: document me
 *
 */
public class HtmlRenderer {

	/**
	 * the out
	 */
	private PrintWriter out;

	/**
	 * the sectionStack
	 */
	private Stack<Section> sectionStack;
	
	/**
	 * Constructor.
	 */
	public HtmlRenderer() {
	}

	/**
	 * Renders the specified document.
	 * @param document the document to render
	 * @param outputFile the output file to write to
	 */
	public void render(Document document, File outputFile) {
		try {
			this.out = new PrintWriter(outputFile);
			this.sectionStack = new Stack<Section>();
			render(document);
			this.out.flush();
			this.out.close();
			this.out = null;
			this.sectionStack = null;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * @param document
	 */
	private void render(Document document) {
		out.println("<html><body>");
		render(document.getRootSection());
		out.println("</body></html>");
	}

	/**
	 * @param section
	 */
	private void render(Section section) {
		sectionStack.push(section);
		renderTitle(section);
		renderDirectContents(section);
		renderSubsections(section);
		sectionStack.pop();
	}

	/**
	 * @param section
	 */
	private void renderTitle(Section section) {
		int headlineNesting = (sectionStack.size() > 6 ? 6 : sectionStack.size());
		out.write("<h" + headlineNesting + ">");
		Section previousStackElement = null;
		for (Section stackElement : sectionStack) {
			if (previousStackElement == null) {
				previousStackElement = stackElement;
				continue;
			} else {
				int index = previousStackElement.getSubsections().indexOf(stackElement);
				out.print(1 + index);
				out.write('.');
				previousStackElement = stackElement;
			}
		}
		out.print(" ");
		printEscaped(section.getTitle());
		out.write("</h" + headlineNesting + ">");
	}

	/**
	 * @param section
	 */
	private void renderDirectContents(Section section) {
		renderBlockItems(section.getDirectContents().getSubItems());
	}

	/**
	 * @param section
	 */
	private void renderSubsections(Section section) {
		for (Section subsection : section.getSubsections()) {
			render(subsection);
		}
	}
	
	/**
	 * @param s
	 */
	private void printEscaped(String s) {
		out.print(s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;"));
	}

	/**
	 * @param section
	 */
	private void renderBlockItems(List<IBlockItem> blockItems) {
		for (IBlockItem item : blockItems) {
			renderBlockItem(item);
		}
	}

	/**
	 * @param section
	 */
	private void renderBlockItem(IBlockItem blockItem) {
		if (blockItem instanceof Paragraph) {
			Paragraph paragraph = (Paragraph)blockItem;
			renderInlineItem(paragraph.getContents());
		} else {
			throw new RuntimeException("cannot render block item: " + blockItem);
		}
	}

	/**
	 * @param section
	 */
	private void renderInlineItems(List<IInlineItem> inlineItems) {
		for (IInlineItem item : inlineItems) {
			renderInlineItem(item);
		}
	}

	/**
	 * @param item
	 */
	private void renderInlineItem(IInlineItem item) {
		if (item instanceof FormattedCompoundInlineItem) {
			FormattedCompoundInlineItem formattedCompoundInlineItem = (FormattedCompoundInlineItem)item;
			out.print("<" + formattedCompoundInlineItem.getFormattingInstruction().getHtmlElement() + ">");
			renderInlineItems(formattedCompoundInlineItem.getSubItems());
			out.print("</" + formattedCompoundInlineItem.getFormattingInstruction().getHtmlElement() + ">");
		} else if (item instanceof TextInlineItem) {
			TextInlineItem textInlineItem = (TextInlineItem)item;
			printEscaped(textInlineItem.getText());
		} else {
			throw new RuntimeException("cannot render inline item: " + item);
		}
	}
	
}
