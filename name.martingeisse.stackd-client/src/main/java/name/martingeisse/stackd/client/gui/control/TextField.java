/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.control;

import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.element.FillColor;
import name.martingeisse.stackd.client.gui.element.IFocusableElement;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.OverlayStack;
import name.martingeisse.stackd.client.gui.element.TextLine;
import name.martingeisse.stackd.client.gui.element.ThinBorder;
import name.martingeisse.stackd.client.gui.util.AreaAlignment;
import name.martingeisse.stackd.client.gui.util.Color;
import org.lwjgl.input.Keyboard;

/**
 * A text input field.
 */
public final class TextField extends Control implements IFocusableElement {

	/**
	 * the textLine
	 */
	private final TextLine textLine;

	/**
	 * the overlayStack
	 */
	private final OverlayStack overlayStack;

	/**
	 * the margin
	 */
	private final Margin margin;

	/**
	 * the border
	 */
	private final ThinBorder border;

	/**
	 * the value
	 */
	private String value;

	/**
	 * the passwordCharacter
	 */
	private char passwordCharacter;
	
	/**
	 * the nextFocusableElement
	 */
	private IFocusableElement nextFocusableElement;

	/**
	 * Constructor.
	 */
	public TextField() {
		this.textLine = new TextLine();
		this.margin = new Margin(textLine, 5 * Gui.MINIGRID);
		this.overlayStack = new OverlayStack().setAlignment(AreaAlignment.LEFT_CENTER);
		overlayStack.addElement(new FillColor(new Color(0, 0, 64, 255)));
		overlayStack.addElement(margin);
		this.border = new ThinBorder(overlayStack);
		this.value = "";
		this.passwordCharacter = (char)0;
		setControlRootElement(border);
	}

	/**
	 * Getter method for the textLine.
	 * @return the textLine
	 */
	public TextLine getTextLine() {
		return textLine;
	}

	/**
	 * Getter method for the margin.
	 * @return the margin
	 */
	public Margin getMargin() {
		return margin;
	}

	/**
	 * Getter method for the border.
	 * @return the border
	 */
	public ThinBorder getBorder() {
		return border;
	}

	/**
	 * Getter method for the backgroundElement.
	 * @return the backgroundElement
	 */
	public GuiElement getBackgroundElement() {
		return overlayStack.getWrappedElements().get(0);
	}

	/**
	 * Setter method for the backgroundElement.
	 * @param backgroundElement the backgroundElement to set
	 */
	public void setBackgroundElement(final GuiElement backgroundElement) {
		overlayStack.replaceElement(0, backgroundElement);
	}

	/**
	 * Getter method for the displayed text.
	 * @return the displayed text
	 */
	public String getDisplayedText() {
		return textLine.getText();
	}

	/**
	 * Getter method for the actual value of the text field
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Setter method for the value.
	 * @param value the value to set
	 */
	public void setValue(final String value) {
		if (value == null) {
			throw new IllegalArgumentException("value is null");
		}
		this.value = value;
		if (passwordCharacter == 0) {
			textLine.setText(value);
		} else {
			final StringBuilder tempBuilder = new StringBuilder();
			for (int i = 0; i < value.length(); i++) {
				tempBuilder.append(passwordCharacter);
			}
			textLine.setText(tempBuilder.toString());
		}
	}

	/**
	 * Getter method for the passwordCharacter.
	 * @return the passwordCharacter
	 */
	public char getPasswordCharacter() {
		return passwordCharacter;
	}

	/**
	 * Setter method for the passwordCharacter.
	 * @param passwordCharacter the passwordCharacter to set
	 */
	public void setPasswordCharacter(final char passwordCharacter) {
		this.passwordCharacter = passwordCharacter;
		setValue(value);
	}
	
	/**
	 * Getter method for the nextFocusableElement.
	 * @return the nextFocusableElement
	 */
	public IFocusableElement getNextFocusableElement() {
		return nextFocusableElement;
	}
	
	/**
	 * Setter method for the nextFocusableElement.
	 * @param nextFocusableElement the nextFocusableElement to set
	 * @return this for chaining
	 */
	public TextField setNextFocusableElement(IFocusableElement nextFocusableElement) {
		this.nextFocusableElement = nextFocusableElement;
		return this;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.control.Control#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	@SuppressWarnings("incomplete-switch")
	public void handleEvent(GuiEvent event) {
		switch (event) {
		
		case MOUSE_BUTTON_PRESSED:
			if (isMouseInside()) {
				getGui().setFocus(this);
			}
			break;
			
		case KEY_PRESSED:
			if (getGui().getFocus() == this) {
				char character = Keyboard.getEventCharacter();
				int code = Keyboard.getEventKey();
				if (code == Keyboard.KEY_BACK) {
					if (!value.isEmpty()) {
						setValue(value.substring(0, value.length() - 1));
					}
				} else if (code == Keyboard.KEY_DELETE) {
				} else if (character == '\t') {
					if (nextFocusableElement != null) {
						getGui().addFollowupLogicAction(new Runnable() {
							@Override
							public void run() {
								getGui().setFocus(nextFocusableElement);
							}
						});
					}
				} else if (character >= 32) {
					setValue(value + character);
				}
			}
			break;
			
		}
		super.handleEvent(event);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.element.IFocusableElement#notifyFocus(boolean)
	 */
	@Override
	public void notifyFocus(boolean focused) {
		border.setColor(focused ? new Color(128, 128, 255, 255) : Color.WHITE);
	}

}
