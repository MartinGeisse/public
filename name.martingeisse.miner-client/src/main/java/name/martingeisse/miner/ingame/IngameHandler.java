/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame;

import java.util.List;
import java.util.Map;
import name.martingeisse.miner.Main;
import name.martingeisse.miner.ingame.gui.IngameMenuHandler;
import name.martingeisse.stackd.client.frame.AbstractFrameHandler;
import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import name.martingeisse.stackd.client.frame.handlers.FlashMessageHandler;
import name.martingeisse.stackd.client.frame.handlers.FpsPanel;
import name.martingeisse.stackd.client.frame.handlers.HandlerList;
import name.martingeisse.stackd.client.frame.handlers.SelectedCubeHud;
import name.martingeisse.stackd.client.frame.handlers.SwappableHandler;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;

/**
 * The in-game frame handler
 */
public class IngameHandler extends HandlerList {

	/**
	 * the cubeWorldHandler
	 */
	public static CubeWorldHandler cubeWorldHandler;

	/**
	 * the serverBaseUrl
	 */
	public static String serverBaseUrl;

	/**
	 * the serverName
	 */
	public static String serverName;

	/**
	 * the enableTexGen
	 */
	public static boolean enableTexGen = true;

	/**
	 * the enableTexturing
	 */
	public static boolean enableTexturing = true;

	/**
	 * the protocolClient
	 */
	public static MinerProtocolClient protocolClient;

	/**
	 * the flashMessageHandler
	 */
	public static FlashMessageHandler flashMessageHandler;
	
	/**
	 * the ingameMenuHandler
	 */
	public static IngameMenuHandler ingameMenuHandler;
	
	/**
	 * the ingameMenuHandlerWrapper
	 */
	public static SwappableHandler ingameMenuHandlerWrapper;

	/**
	 * Constructor.
	 * @throws Exception on errors
	 */
	public IngameHandler() throws Exception {

		// determine server base URL (old HTTP protocol)
		serverBaseUrl = System.getProperty("name.martingeisse.miner.serverBaseUrl");
		if (serverBaseUrl == null) {
			serverBaseUrl = "http://localhost:8080";
		} else if (serverBaseUrl.equals("LIVE")) {
			serverBaseUrl = "http://vshg03.mni.fh-giessen.de:8080";
		}

		// determine server name (new binary protocol)
		serverName = System.getProperty("name.martingeisse.miner.serverName");
		if (serverName == null) {
			serverName = "localhost";
		} else if (serverName.equals("LIVE")) {
			serverName = "vshg03.mni.fh-giessen.de";
		}

		// connect to the server
		SimpleResources resources = new SimpleResources();
		flashMessageHandler = new FlashMessageHandler(resources.getFont());
		protocolClient = new MinerProtocolClient();
		protocolClient.setFlashMessageHandler(flashMessageHandler);

		// build the cube world handler
		cubeWorldHandler = new CubeWorldHandler(Main.screenWidth, Main.screenHeight, resources);
		add(new AbstractFrameHandler() {

			/* (non-Javadoc)
			 * @see name.martingeisse.stackd.frame.AbstractFrameHandler#handleStep()
			 */
			@Override
			public void handleStep() throws BreakFrameLoopException {
				cubeWorldHandler.step();
				
				// TODO avoid filling up the render queue, should detect when the logic thread is running too fast
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
				}
				
			}

			/* (non-Javadoc)
			 * @see name.martingeisse.stackd.frame.AbstractFrameHandler#onAfterHandleStep()
			 */
			@Override
			public void onAfterHandleStep() {
				cubeWorldHandler.purge();
			}

			/* (non-Javadoc)
			 * @see name.martingeisse.stackd.client.frame.AbstractFrameHandler#draw(name.martingeisse.glworker.GlWorkerLoop)
			 */
			@Override
			public void draw(GlWorkerLoop glWorkerLoop) {
				cubeWorldHandler.draw(glWorkerLoop);
			}

		});
		add(new FpsPanel(resources.getFont()));
		// add(new ExitHandler(true, Keyboard.KEY_ESCAPE));
		final SelectedCubeHud selectedCubeHud = new SelectedCubeHud(cubeWorldHandler.getResources().getCubeTextures(), cubeWorldHandler.getWorkingSet().getEngineParameters().getCubeTypes());
		add(selectedCubeHud);
		add(new AbstractFrameHandler() {
			@Override
			public void onBeforeDraw(GlWorkerLoop glWorkerLoop) {
				selectedCubeHud.setCubeTypeIndex(cubeWorldHandler.getCurrentCubeType());
			}
		});
		add(new SendPositionToServerHandler(cubeWorldHandler.getPlayer()));
		add(new AbstractFrameHandler() {
			@Override
			public void handleStep() throws BreakFrameLoopException {
				
				// TODO race condition: should not start the game until the player has been resumed,
				// would be wrong and also load wrong sections
				
				final List<PlayerProxy> updatedPlayerProxies = protocolClient.fetchUpdatedPlayerProxies();
				if (updatedPlayerProxies != null) {
					cubeWorldHandler.setPlayerProxies(updatedPlayerProxies);
				}
				final Map<Integer, String> updatedPlayerNames = protocolClient.fetchUpdatedPlayerNames();
				if (updatedPlayerNames != null) {
					cubeWorldHandler.setPlayerNames(updatedPlayerNames);
				}
				final PlayerResumedMessage playerResumedMessage = protocolClient.fetchPlayerResumedMessage();
				if (playerResumedMessage != null) {
					cubeWorldHandler.getPlayer().setX(playerResumedMessage.getX());
					cubeWorldHandler.getPlayer().setY(playerResumedMessage.getY());
					cubeWorldHandler.getPlayer().setZ(playerResumedMessage.getZ());
					protocolClient.getSectionGridLoader().setViewerPosition(cubeWorldHandler.getPlayer().getSectionId());
				}
			}
		});
		add(flashMessageHandler);
		
		/*
		console.setCommandHandler(new IConsoleCommandHandler() {
			@Override
			public void handleCommand(Console console, String command, String[] args) {
				if (command.equals("quit")) {
					SimpleWorkerScheme.requestStop();
					throw new BreakFrameLoopException();
				} else if (command.equals("initworld") || command.equals("help") || command.equals("itemtypes") || command.equals("wish") || command.equals("equip") || command.equals("unequip") || command.equals("trash") || command.equals("give") || command.equals("inventory")) {
					protocolClient.sendConsoleCommand(command, args);
				} else if (command.equals("echo")) {
					for (String arg : args) {
						console.println(arg);
					}
				} else {
					console.println("unknown command: " + command);
				}
			}
		});
		*/
		
		// the in-game menu
		ingameMenuHandler = new IngameMenuHandler();
		ingameMenuHandlerWrapper = new SwappableHandler();
		add(ingameMenuHandlerWrapper);

		// prepare running the game
		protocolClient.setFlashMessageHandler(flashMessageHandler);
		protocolClient.waitUntilReady();
		
	}

}
