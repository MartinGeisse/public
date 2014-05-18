/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.miner.account.AccountApiClient;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.control.MessageBox;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import org.lwjgl.input.Keyboard;

/**
 * The "login" menu page.
 */
public class LoginPage extends AbstractStartmenuPage {

	/**
	 * the username
	 */
	private final LabeledTextField username;
	
	/**
	 * the password
	 */
	private final LabeledTextField password;
	
	/**
	 * Constructor.
	 */
	public LoginPage() {
		
		username = new LabeledTextField("Username");
		password = new LabeledTextField("Password");
		username.getTextField().setNextFocusableElement(password.getTextField());
		password.getTextField().setNextFocusableElement(username.getTextField());
		password.getTextField().setPasswordCharacter('*');
		
		final VerticalLayout menu = new VerticalLayout();
		menu.addElement(username);
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(password);
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Log in") {
			@Override
			protected void onClick() {
				login();
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(EXIT_BUTTON);
		initializeStartmenuPage(menu);
		
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.control.Control#handleEvent(name.martingeisse.stackd.client.gui.GuiEvent)
	 */
	@Override
	public void handleEvent(GuiEvent event) {
		super.handleEvent(event);
		if (event == GuiEvent.KEY_PRESSED && Keyboard.getEventCharacter() == '\r') {
			login();
		}
	}
	
	/**
	 * 
	 */
	private void login() {
		String username = this.username.getTextField().getValue();
		String password = this.password.getTextField().getValue();
		try {
			AccountApiClient.getInstance().login(username, password);
			getGui().setRootElement(new ChooseCharacterPage());
		} catch (RuntimeException e) {
			new MessageBox(e.getMessage()).show(LoginPage.this);
		}
	}

}
