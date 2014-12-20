/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui.control;

import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.element.AbstractFillElement;
import name.martingeisse.stackd.client.gui.element.FillColor;
import name.martingeisse.stackd.client.gui.element.IFocusableElement;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.OverlayStack;
import name.martingeisse.stackd.client.gui.element.TextLine;
import name.martingeisse.stackd.client.gui.element.ThinBorder;
import name.martingeisse.stackd.client.gui.util.AreaAlignment;
import name.martingeisse.stackd.client.gui.util.Color;
import name.martingeisse.stackd.client.system.Font;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

/**
 * A text input field.
 */
public final class TextField extends Control implements IFocusableElement {

	/**
	 * the textLine
	 */
	private final TextLine textLine;

	/**
	 * the outerOverlayStack
	 */
	private final OverlayStack outerOverlayStack;

	/**
	 * the margin
	 */
	private final Margin margin;

	/**
	 * the innerOverlayStack
	 */
	private final OverlayStack innerOverlayStack;

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
	 * the cursorPosition
	 */
	private int cursorPosition;

	/**
	 * Constructor.
	 */
	public TextField() {
		
		// text line and cursor
		this.textLine = new TextLine();
		this.innerOverlayStack = new OverlayStack().setAlignment(AreaAlignment.LEFT_CENTER);
		this.innerOverlayStack.addElement(textLine);
		this.innerOverlayStack.addElement(new CursorLayer());
		
		// embed into margin
		this.margin = new Margin(innerOverlayStack, 5 * Gui.MINIGRID);
		this.outerOverlayStack = new OverlayStack().setAlignment(AreaAlignment.LEFT_CENTER);
		outerOverlayStack.addElement(new FillColor(new Color(0, 0, 64, 255)));
		outerOverlayStack.addElement(margin);
		
		// add border
		this.border = new ThinBorder(outerOverlayStack);
		
		// initialize fields
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
		return outerOverlayStack.getWrappedElements().get(0);
	}

	/**
	 * Setter method for the backgroundElement.
	 * @param backgroundElement the backgroundElement to set
	 */
	public void setBackgroundElement(final GuiElement backgroundElement) {
		outerOverlayStack.replaceElement(0, backgroundElement);
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
	 * @return this for chaining
	 */
	public TextField setValue(final String value) {
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
		if (cursorPosition > value.length()) {
			cursorPosition = value.length();
		}
		return this;
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
	
	/**
	 * Getter method for the cursorPosition.
	 * @return the cursorPosition
	 */
	public int getCursorPosition() {
		return cursorPosition;
	}
	
	/**
	 * Setter method for the cursorPosition.
	 * @param cursorPosition the cursorPosition to set
	 * @return this
	 */
	public TextField setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
		return this;
	}
	
	/**
	 * Moves the cursor to the position after the last character.
	 * @return this
	 */
	public TextField moveCursorToEnd() {
		this.cursorPosition = value.length();
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
					if (cursorPosition > 0) {
						int newPosition = cursorPosition - 1;
						String prefix = value.substring(0, cursorPosition - 1);
						String suffix = value.substring(cursorPosition);
						setValue(prefix + suffix);
						cursorPosition = newPosition;
					}
				} else if (code == Keyboard.KEY_DELETE) {
					if (cursorPosition < value.length()) {
						String prefix = value.substring(0, cursorPosition);
						String suffix = value.substring(cursorPosition + 1);
						setValue(prefix + suffix);
					}
				} else if (character == '\t') {
					if (nextFocusableElement != null) {
						getGui().addFollowupLogicAction(new Runnable() {
							@Override
							public void run() {
								getGui().setFocus(nextFocusableElement);
							}
						});
					}
				} else if (code == Keyboard.KEY_LEFT) {
					if (cursorPosition > 0) {
						cursorPosition--;
					}
				} else if (code == Keyboard.KEY_RIGHT) {
					if (cursorPosition < value.length()) {
						cursorPosition++;
					}
				} else if (character >= 32) {
					String prefix = value.substring(0, cursorPosition);
					String suffix = value.substring(cursorPosition);
					setValue(prefix + character + suffix);
					cursorPosition++;
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

	/**
	 *
	 */
	private final class CursorLayer extends AbstractFillElement {
		@Override
		protected void draw() {
			if (getGui().getFocus() == TextField.this) {
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_BLEND);
				Color.WHITE.glColor();
				GL11.glLineWidth(1);
				
				String textBeforeCursor = textLine.getText().substring(0, cursorPosition);
				Font font = textLine.getEffectiveFont();
				int x = getAbsoluteX() + getGui().pixelsToUnits(font.getStringWidth(textBeforeCursor));
				int y1 = getAbsoluteY();
				int y2 = y1 + getHeight();
				
				GL11.glBegin(GL11.GL_LINES);
				GL11.glVertex2i(x, y1);
				GL11.glVertex2i(x, y2);
				GL11.glEnd();
			}
		}
	}
	
}
