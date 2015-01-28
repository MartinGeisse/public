/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import java.util.prefs.Preferences;

import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.control.Button;
import name.martingeisse.stackd.client.gui.element.Grid;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.TextLine;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;

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
		
		Preferences preferences = Preferences.userNodeForPackage(AccountApiClient.class);
		String defaultUsername = preferences.get("username", null);
		if (defaultUsername == null) {
			defaultUsername = "";
		}

		username = new LabeledTextField("Username");
		password = new LabeledTextField("Password");
		username.getTextField().setNextFocusableElement(password.getTextField()).setValue(defaultUsername).moveCursorToEnd();
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
				// TODO show a loading indicator
				getGui().addFollowupLogicAction(new Runnable() {
					@Override
					public void run() {
						login();
					}
				});
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(EXIT_BUTTON);
		
		menu.addElement(new Spacer(Gui.GRID));
		menu.addElement(new Grid(10, 5) {
			@Override
			protected GuiElement newChild(final int x, final int y) {
				Button button = new Button() {
					@Override
					protected void onClick() {
						System.out.println("* " + x + ", " + y);
						
					}
				};
				button.getTextLine().setText("foo");
				return button;
			}
		}.initialize());
		
		initializeStartmenuPage(menu);
		
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.gui.control.Page#onAttach()
	 */
	@Override
	protected void onAttach() {
		LabeledTextField initialFocus = (username.getTextField().getValue().isEmpty() ? username : password);
		getGui().setFocus(initialFocus.getTextField());
	}
	
	/* (non-Javadoc)
	 * @see name.martingeisse.miner.startmenu.AbstractStartmenuPage#onEnterPressed()
	 */
	@Override
	protected void onEnterPressed() {
		login();
	}
	
	/**
	 * 
	 */
	private void login() {
		String username = this.username.getTextField().getValue();
		String password = this.password.getTextField().getValue();
		AccountApiClient.getInstance().login(username, password);
		getGui().setRootElement(new ChooseCharacterPage());
		Preferences.userNodeForPackage(AccountApiClient.class).put("username", username);
	}

}
