/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui;

import java.util.LinkedList;
import java.util.Queue;
import name.martingeisse.stackd.client.gui.element.IFocusableElement;
import name.martingeisse.stackd.client.system.Font;


/**
 * The main container class for the GUI.
 */
public final class Gui {

	/**
	 * the width
	 */
	private final int width;

	/**
	 * the height
	 */
	private final int height;

	/**
	 * the rootElement
	 */
	private GuiElement rootElement;

	/**
	 * the layoutRequested
	 */
	private boolean layoutRequested;

	/**
	 * the mouseX
	 */
	private int mouseX;
	
	/**
	 * the mouseY
	 */
	private int mouseY;
	
	/**
	 * the currentKeyboardCharacter
	 */
	private int currentKeyboardCharacter;
	
	/**
	 * the currentKeyboardCode
	 */
	private int currentKeyboardCode;
	
	/**
	 * the time
	 */
	private int time;
	
	/**
	 * the defaultFont
	 */
	private Font defaultFont;
	
	/**
	 * the focus
	 */
	private IFocusableElement focus;
	
	/**
	 * the followupActions
	 */
	private Queue<Runnable> followupActions;

	/**
	 * Constructor.
	 * @param width the screen width
	 * @param height the screen height
	 */
	public Gui(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.followupActions = new LinkedList<Runnable>();
	}

	/**
	 * Getter method for the width.
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Getter method for the height.
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Getter method for the rootElement.
	 * @return the rootElement
	 */
	public final GuiElement getRootElement() {
		return rootElement;
	}

	/**
	 * Setter method for the rootElement.
	 * @param rootElement the rootElement to set
	 */
	public final void setRootElement(final GuiElement rootElement) {
		this.rootElement = rootElement;
		this.layoutRequested = true;
		rootElement.usedAsRootElement(this);
	}

	/**
	 * Requests that elements get layouted before rendering the next time.
	 */
	public final void requestLayout() {
		layoutRequested = true;
	}

	/**
	 * Draws the GUI.
	 */
	public final void draw() {
		if (rootElement == null) {
			return;
		}
		if (layoutRequested) {
			rootElement.requestSize(width, height);
			rootElement.setPosition(0, 0);
			layoutRequested = false;
		}
		time = (int)System.currentTimeMillis();
		rootElement.handleEvent(GuiEvent.DRAW);
	}

	/**
	 * Handles a movement of the mouse cursor.
	 * @param x the new x coordinate of the mouse cursor
	 * @param y the new y coordinate of the mouse cursor
	 */
	public void handleMouseMovement(int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
		if (rootElement != null) {
			rootElement.handleEvent(GuiEvent.MOUSE_MOVED);
		}
	}
	
	/**
	 * Getter method for the mouseX.
	 * @return the mouseX
	 */
	public int getMouseX() {
		return mouseX;
	}
	
	/**
	 * Getter method for the mouseY.
	 * @return the mouseY
	 */
	public int getMouseY() {
		return mouseY;
	}
	
	/**
	 * Handles a mouse click.
	 */
	public void handleMouseClick() {
		if (rootElement != null) {
			rootElement.handleEvent(GuiEvent.MOUSE_CLICKED);
		}
	}
	
	/**
	 * Handles a keypress event.
	 * @param character the character that was typed
	 * @param code the code of the key that was pressed
	 */
	public void handleKeyboardEvent(int character, int code) {
		this.currentKeyboardCharacter = character;
		this.currentKeyboardCode = code;
		if (rootElement != null) {
			rootElement.handleEvent(GuiEvent.KEY_PRESSED);
		}
	}
	
	/**
	 * Getter method for the currentKeyboardCharacter.
	 * @return the currentKeyboardCharacter
	 */
	public int getCurrentKeyboardCharacter() {
		return currentKeyboardCharacter;
	}
	
	/**
	 * Getter method for the currentKeyboardCode.
	 * @return the currentKeyboardCode
	 */
	public int getCurrentKeyboardCode() {
		return currentKeyboardCode;
	}
	
	/**
	 * Getter method for the current time.
	 * @return the time
	 */
	public int getTime() {
		return time;
	}
	
	/**
	 * Getter method for the defaultFont.
	 * @return the defaultFont
	 */
	public Font getDefaultFont() {
		return defaultFont;
	}
	
	/**
	 * Setter method for the defaultFont.
	 * @param defaultFont the defaultFont to set
	 */
	public void setDefaultFont(Font defaultFont) {
		this.defaultFont = defaultFont;
		requestLayout();
	}

	/**
	 * Getter method for the focus.
	 * @return the focus
	 */
	public IFocusableElement getFocus() {
		return focus;
	}
	
	/**
	 * Setter method for the focus.
	 * @param focus the focus to set
	 */
	public void setFocus(IFocusableElement focus) {
		if (focus != this.focus) {
			if (this.focus != null) {
				this.focus.notifyFocus(false);
			}
			this.focus = focus;
			if (this.focus != null) {
				this.focus.notifyFocus(true);
			}
		}
	}

	/**
	 * Adds an action to execute as part of the "followup action loop".
	 * 
	 * For example, a text field that wants to change focus in reaction to the tab key
	 * should do so in a followup action. If it changed focus directly, then handling
	 * of the tab key would continue and might reach the newly focused element later on
	 * (depending on element order) and immediately switch focus again.
	 * 
	 * @param followupAction the followup action to add
	 */
	public void addFollowupAction(Runnable followupAction) {
		followupActions.add(followupAction);
	}
	
	/**
	 * Executes all pending followup actions. This should generally be done after
	 * passing input events to the GUI.
	 */
	public void executeFollowupActions() {
		while (true) {
			Runnable action = followupActions.poll();
			if (action == null) {
				break;
			}
			action.run();
		}
	}
	
}
