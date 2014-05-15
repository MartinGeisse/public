/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import org.lwjgl.input.Mouse;
import name.martingeisse.miner.Main;
import name.martingeisse.miner.ingame.IngameHandler;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;


/**
 * The "choose your name" menu page.
 */
public class ChooseNamePage extends AbstractStartmenuPage {

	/**
	 * the selectedName
	 */
	public static String selectedName;
	
	/**
	 * the username
	 */
	private final LabeledTextField name;

	/**
	 * Constructor.
	 */
	public ChooseNamePage() {
		final VerticalLayout menu = new VerticalLayout();
		this.name = new LabeledTextField("Name");
		menu.addElement(name);
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Create Character") {
			@Override
			protected void onClick() {
				selectedName = name.getTextField().getValue();
				// TODO: create character
				// TODO: use character
				System.out.println("TODO actually load this player");
				try {
					Main.frameLoop.getRootHandler().setWrappedHandler(new IngameHandler());
					Mouse.setGrabbed(true);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Back") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseFactionPage());
			}
		});
		initializeStartmenuPage(menu);
	}

}
