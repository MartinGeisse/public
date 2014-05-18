/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.startmenu;

import name.martingeisse.common.javascript.analyze.JsonAnalyzer;
import name.martingeisse.miner.Main;
import name.martingeisse.miner.account.AccountApiClient;
import name.martingeisse.miner.common.Faction;
import name.martingeisse.miner.ingame.IngameHandler;
import name.martingeisse.stackd.client.gui.Gui;
import name.martingeisse.stackd.client.gui.GuiElement;
import name.martingeisse.stackd.client.gui.control.MessageBox;
import name.martingeisse.stackd.client.gui.element.FillColor;
import name.martingeisse.stackd.client.gui.element.Margin;
import name.martingeisse.stackd.client.gui.element.OverlayStack;
import name.martingeisse.stackd.client.gui.element.Sizer;
import name.martingeisse.stackd.client.gui.element.Spacer;
import name.martingeisse.stackd.client.gui.element.TextParagraph;
import name.martingeisse.stackd.client.gui.element.ThinBorder;
import name.martingeisse.stackd.client.gui.element.VerticalLayout;
import name.martingeisse.stackd.client.gui.util.Color;
import org.lwjgl.input.Mouse;

/**
 * The "character details" menu page.
 */
public class PlayerDetailsPage extends AbstractStartmenuPage {

	/**
	 * the playerId
	 */
	private final long playerId;
	
	/**
	 * the faction
	 */
	private final Faction faction;
	
	/**
	 * the name
	 */
	private final String name;

	/**
	 * the coins
	 */
	private final int coins;
	
	/**
	 * Constructor.
	 * @param playerId the player ID
	 */
	public PlayerDetailsPage(long playerId) {
		
		// fetch player data
		this.playerId = playerId;
		JsonAnalyzer json = AccountApiClient.getInstance().fetchPlayerDetails(playerId);
		this.faction = Faction.values()[json.analyzeMapElement("faction").expectInteger()];
		this.name = json.analyzeMapElement("name").expectString();
		this.coins = json.analyzeMapElement("coins").expectInteger();
		
		// build the layout
		final VerticalLayout menu = new VerticalLayout();
		menu.addElement(buildInfoBox());
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new Sizer(new StartmenuButton("Play!") {
			@Override
			protected void onClick() {
				play();
			}
		}, -1, 10 * Gui.GRID));
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new StartmenuButton("Delete Character") {
			@Override
			protected void onClick() {
				new MessageBox("Really delete this character?", MessageBox.YES_NO) {
					@Override
					protected void onClose(int buttonIndex) {
						if (buttonIndex == 0) {
							AccountApiClient.getInstance().deletePlayer(PlayerDetailsPage.this.playerId);
							getGui().setRootElement(new ChooseCharacterPage());
						}
					};
				}.show(PlayerDetailsPage.this);
			}
		});
		menu.addElement(new Spacer(2 * Gui.GRID));
		menu.addElement(new Sizer(new StartmenuButton("Back") {
			@Override
			protected void onClick() {
				getGui().setRootElement(new ChooseCharacterPage());
			}
		}, -1, 5 * Gui.GRID));
		initializeStartmenuPage(menu);
		
	}
	
	/**
	 * 
	 */
	private GuiElement buildInfoBox() {
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.addElement(new TextParagraph().setText("--- " + name + " ---"));
		verticalLayout.addElement(new Spacer(Gui.GRID));
		verticalLayout.addElement(new TextParagraph().setText("Faction: " + faction.getDisplayName()));
		verticalLayout.addElement(new TextParagraph().setText("Coins: " + coins));
		OverlayStack stack = new OverlayStack();
		stack.addElement(new FillColor(new Color(128, 128, 128, 255)));
		stack.addElement(new Margin(verticalLayout, Gui.GRID));
		return new ThinBorder(stack).setColor(new Color(192, 192, 192, 255));
	}

	/**
	 * 
	 */
	private void play() {
		System.out.println("selected player #" + playerId);
		// TODO 
		System.out.println("TODO actually load this player");
		try {
			Main.frameLoop.getRootHandler().setWrappedHandler(new IngameHandler());
			Mouse.setGrabbed(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
