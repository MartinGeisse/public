/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.control;

import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.element.FillColor;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.OverlayStack;
import name.martingeisse.stackd.client.gui.element.TextLine;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import name.martingeisse.stackd.client.gui.util.Color;

/**
 * A box with a text message and an OK button. This control can be used
 * as a popup element on a {@link Page}. When the user clicks the OK
 * button, the message box removes itself from any {@link Page} ancestor
 * control again and a subclass method gets invoked for custom logic.
 */
public class MessageBox extends Control {

	/**
	 * Shows a message box on the page that contains the specified
	 * element. Such a page must exist, otherwise this method throws
	 * an {@link IllegalArgumentException}. If the element is contained
	 * in nested pages, the message box gets shown in the innermost one.
	 * 
	 * @param element an element inside the page
	 * @param message the message
	 */
	public static void show(GuiElement element, String message) {
		while (element != null) {
			if (element instanceof Page) {
				show((Page)element, message);
				return;
			}
			element = element.getParent();
		}
		throw new IllegalArgumentException("argument element not inside a page");
	}
	
	/**
	 * Shows a message box on the specified page.
	 * 
	 * @param page the page
	 * @param message the message
	 */
	public static void show(Page page, String message) {
		page.setPopupElement(new MessageBox(message));
	}
	
	/**
	 * Constructor.
	 * @param message the message to show
	 */
	public MessageBox(final String message) {
		VerticalLayout verticalLayout = new VerticalLayout();
		
		TextLine textLine = new TextLine().setText(message);
		Margin textLineMargin = new Margin(textLine, 2 * Gui.GRID);
		OverlayStack textStack = new OverlayStack();
		textStack.addElement(new FillColor(new Color(128, 128, 128, 255)));
		textStack.addElement(textLineMargin);
		verticalLayout.addElement(textStack);
		
		Button button = new Button() {
			@Override
			protected void onClick() {
				removeFromPages();
				onClose();
			}
		};
		button.getTextLine().setText("OK");
		button.setBackgroundElement(new FillColor(Color.BLUE));
		button.addPulseEffect(new Color(255, 255, 255, 64));
		verticalLayout.addElement(button);
		
		setControlRootElement(new Margin(verticalLayout, 30 * Gui.GRID));
	}

	/**
	 * 
	 */
	private void removeFromPages() {
		for (GuiElement element = getParent(); element != null; element = element.getParent()) {
			if (element instanceof Page) {
				Page page = (Page)element;
				if (page.getPopupElement() == this) {
					page.setPopupElement(null);
				}
			}
		}
	}

	/**
	 * This method gets invoked when the user presses the "OK" button.
	 */
	protected void onClose() {
	}
	
}
