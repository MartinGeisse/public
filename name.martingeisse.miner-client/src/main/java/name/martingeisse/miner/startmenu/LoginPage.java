/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.launcher.assets.LauncherAssets;
import name.martingeisse.miner.account.AccountApiClient;
import name.martingeisse.stackd.client.frame.handlers.ExitHandler;
import name.martingeisse.stackd.client.gui.GuiEvent;
import name.martingeisse.stackd.client.gui.control.MessageBox;
import name.martingeisse.stackd.client.gui.control.Page;
import name.martingeisse.stackd.client.gui.element.FillTexture;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import name.martingeisse.stackd.client.system.StackdTexture;
import org.lwjgl.input.Keyboard;

/**
 * The "login" menu page.
 */
public class LoginPage extends Page {

	/**
	 * the exitHandler
	 */
	private final ExitHandler exitHandler;
	
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
	 * @param exitHandler the exit handler
	 */
	public LoginPage(final ExitHandler exitHandler) {
		this.exitHandler = exitHandler;
		
		username = new LabeledTextField("Username");
		password = new LabeledTextField("Password");
		username.getTextField().setNextFocusableElement(password.getTextField());
		password.getTextField().setNextFocusableElement(username.getTextField());
		password.getTextField().setPasswordCharacter('*');
		
		final VerticalLayout menu = new VerticalLayout();
		menu.addElement(username);
		menu.addElement(new Spacer(20));
		menu.addElement(password);
		menu.addElement(new Spacer(20));
		menu.addElement(new StartmenuButton("Log in") {
			@Override
			protected void onClick() {
				login();
			}
		});
		menu.addElement(new Spacer(20));
		menu.addElement(new StartmenuButton("Quit") {
			@Override
			protected void onClick() {
				exitHandler.setProgrammaticExit(true);
			}
		});
		StackdTexture backgroundTexture = new StackdTexture(LauncherAssets.class, "dirt.png", false);
		initializePage(new FillTexture(backgroundTexture), new Margin(menu, 200, 300));
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
			getGui().setRootElement(new ChooseCharacterPage(exitHandler));
		} catch (RuntimeException e) {
			MessageBox.show(LoginPage.this, e.getMessage());
		}
	}

}
