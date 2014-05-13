/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.miner.account.AccountApiClient;
import name.martingeisse.stackd.client.frame.handlers.ExitHandler;
import name.martingeisse.stackd.client.gui.control.Control;
import name.martingeisse.stackd.client.gui.element.Spacer;

/**
 * The "login" menu page.
 */
public class ChooseCharacterPage extends Control {

	/**
	 * the exitHandler
	 */
	private final ExitHandler exitHandler;

	/**
	 * Constructor.
	 * @param exitHandler the exit handler
	 */
	public ChooseCharacterPage(final ExitHandler exitHandler) {
		this.exitHandler = exitHandler;
		setControlRootElement(new Spacer(1));
		JsonAnalyzer json = AccountApiClient.getInstance().fetchPlayers();
		System.out.println("* " + json.getValue());
	}

}
