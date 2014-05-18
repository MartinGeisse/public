/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.miner.account.AccountApiClient;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;


/**
 * The "choose your name" menu page.
 */
public class ChooseNamePage extends AbstractStartmenuPage {
	
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
				String name = ChooseNamePage.this.name.getTextField().getValue();
				long playerId = AccountApiClient.getInstance().createPlayer(ChooseFactionPage.selectedFaction, name);
				getGui().setRootElement(new PlayerDetailsPage(playerId));
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
