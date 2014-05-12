/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.launcher.assets.LauncherAssets;
import name.martingeisse.stackd.client.frame.handlers.ExitHandler;
import name.martingeisse.stackd.client.frame.handlers.HandlerList;
import name.martingeisse.stackd.client.gui.GuiFrameHandler;
import name.martingeisse.stackd.client.system.FixedWidthFont;
import name.martingeisse.stackd.client.util.ResourceLoader;

/**
 * The handler for the start menu. This menu does not run in parallel with the
 * actual game.
 */
public class StartmenuHandler extends HandlerList {

	/**
	 * Constructor.
	 */
	public StartmenuHandler() {
		// TODO share resources properly
		ExitHandler exitHandler = new ExitHandler(true, null);
		GuiFrameHandler guiFrameHandler = new GuiFrameHandler();
		guiFrameHandler.getGui().setDefaultFont(new FixedWidthFont(ResourceLoader.loadAwtImage(LauncherAssets.class, "font.png"), 8, 16));
		guiFrameHandler.getGui().setRootElement(new LoginPage(exitHandler));
		add(guiFrameHandler);
		add(exitHandler);
	}

}
