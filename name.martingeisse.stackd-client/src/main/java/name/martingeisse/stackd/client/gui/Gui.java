/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackd.client.gui;

import java.util.LinkedList;
import java.util.Queue;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import name.martingeisse.stackd.client.gui.element.IFocusableElement;
import name.martingeisse.stackd.client.system.Font;


/**
 * The main container class for the GUI.
 * 
 * The GUI uses an integer coordinate system where the total height
 * of the GUI is a hundred thousand units, and the total width a determined such
 * that a unit has the same length along either axis.
 */
public final class Gui {

	/**
	 * the HEIGHT_UNITS
	 */
	public static final int HEIGHT_UNITS = 100000;
	
	/**
	 * The "normal" grid to align things. The total height is 100 grid clicks.
	 */
	public static final int GRID = 1000;
	
	/**
	 * The "mini" grid to align things. The total height is 1000 minigrid clicks. 
	 */
	public static final int MINIGRID = 100;
	
	/**
	 * the widthPixels
	 */
	private final int widthPixels;
	
	/**
	 * the heightPixels
	 */
	private final int heightPixels;
	
	/**
	 * the widthUnits
	 */
	private final int widthUnits;
	
	/**
	 * the rootElement
	 */
	private GuiElement rootElement;

	/**
	 * the layoutRequested
	 */
	private boolean layoutRequested;
	
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
	 * the followupLogicActions
	 */
	private Queue<Runnable> followupLogicActions;

	/**
	 * the followupOpenglActions
	 */
	private Queue<Runnable> followupOpenglActions;
	
	/**
	 * Constructor.
	 * @param widthPixels the width of the GUI in pixels
	 * @param heightPixels the height of the GUI in pixels
	 */
	public Gui(int widthPixels, int heightPixels) {
		this.widthPixels = widthPixels;
		this.heightPixels = heightPixels;
		this.widthUnits = pixelsToUnits(widthPixels);
		this.followupLogicActions = new LinkedList<Runnable>();
		this.followupOpenglActions = new LinkedList<Runnable>();
	}

	/**
	 * Getter method for the widthPixels.
	 * @return the widthPixels
	 */
	public int getWidthPixels() {
		return widthPixels;
	}
	
	/**
	 * Getter method for the heightPixels.
	 * @return the heightPixels
	 */
	public int getHeightPixels() {
		return heightPixels;
	}
	
	/**
	 * Getter method for the widthUnits.
	 * @return the widthUnits
	 */
	public int getWidthUnits() {
		return widthUnits;
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
		if (rootElement != null) {
			rootElement.usedAsRootElement(this);
		}
	}

	/**
	 * Requests that elements get layouted before rendering the next time.
	 */
	public final void requestLayout() {
		layoutRequested = true;
	}

	/**
	 * Fires the specified event.
	 * @param event the event
	 */
	public void fireEvent(GuiEvent event) {
		if (rootElement == null) {
			return;
		}
		if (event == GuiEvent.DRAW) {
			if (layoutRequested) {
				rootElement.requestSize(widthUnits, HEIGHT_UNITS);
				rootElement.setPosition(0, 0);
				layoutRequested = false;
			}
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0, widthUnits, HEIGHT_UNITS, 0, -1, 1);
		}
		time = (int)System.currentTimeMillis();
		rootElement.handleEvent(event);
	}
	
	/**
	 * Getter method for the mouseX.
	 * @return the mouseX
	 */
	public int getMouseX() {
		return pixelsToUnits(Mouse.getX());
	}
	
	/**
	 * Getter method for the mouseY.
	 * @return the mouseY
	 */
	public int getMouseY() {
		return pixelsToUnits(heightPixels - Mouse.getY());
	}
	
	/**
	 * Converts coordinate units to pixels.
	 * @param units the units
	 * @return the pixels
	 */
	public int unitsToPixels(int units) {
		return units * heightPixels / HEIGHT_UNITS;
	}
	
	/**
	 * Converts pixels to coordinate units.
	 * @param pixels the pixels
	 * @return the coordinate units
	 */
	public int pixelsToUnits(int pixels) {
		return pixels * HEIGHT_UNITS / heightPixels;
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
	 * Adds an action to execute as part of the "followup logic action loop".
	 * 
	 * For example, a text field that wants to change focus in reaction to the tab key
	 * should do so in a followup logic action. If it changed focus directly, then handling
	 * of the tab key would continue and might reach the newly focused element later on
	 * (depending on element order) and immediately switch focus again.
	 * 
	 * @param followupLogicAction the followup logic action to add
	 */
	public void addFollowupLogicAction(Runnable followupLogicAction) {
		followupLogicActions.add(followupLogicAction);
	}
	
	/**
	 * Executes all pending followup logic actions. This should generally be done after
	 * passing input events to the GUI.
	 */
	public void executeFollowupLogicActions() {
		while (true) {
			Runnable action = followupLogicActions.poll();
			if (action == null) {
				break;
			}
			action.run();
		}
	}
	
	/**
	 * Adds an action to execute as part of the "followup OpenGL action loop". This is
	 * similar to followup logic actions, except they're executed in the OpenGL
	 * worker thread.
	 * 
	 * Followup OpenGL actions are needed for special cases during the interaction
	 * between GUI and the in-game OpenGL code. This comment cannot describe a
	 * typical use case because there is none.
	 * 
	 * @param followupOpenglAction the followup OpenGL action to add
	 */
	public void addFollowupOpenglAction(Runnable followupOpenglAction) {
		followupOpenglActions.add(followupOpenglAction);
	}
	
	/**
	 * Executes all pending followup OpenGL actions. This should generally be done after
	 * firing the DRAW event.
	 */
	public void executeFollowupOpenglActions() {
		while (true) {
			Runnable action = followupOpenglActions.poll();
			if (action == null) {
				break;
			}
			action.run();
		}
	}
	
}
