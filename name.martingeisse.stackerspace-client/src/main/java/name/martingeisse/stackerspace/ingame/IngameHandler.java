/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.ingame;

import java.util.List;
import java.util.Map;
import name.martingeisse.stackd.client.frame.AbstractFrameHandler;
import name.martingeisse.stackd.client.frame.BreakFrameLoopException;
import name.martingeisse.stackd.client.frame.handlers.FlashMessageHandler;
import name.martingeisse.stackd.client.frame.handlers.FpsPanel;
import name.martingeisse.stackd.client.frame.handlers.HandlerList;
import name.martingeisse.stackd.client.frame.handlers.SelectedCubeHud;
import name.martingeisse.stackd.client.frame.handlers.SwappableHandler;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.util.MouseUtil;
import name.martingeisse.stackerspace.Main;
import name.martingeisse.stackerspace.gamegui.GameMenuHandler;
import name.martingeisse.stackerspace.ingame.network.StackerspaceProtocolClient;
import name.martingeisse.stackerspace.ingame.network.PlayerResumedMessage;
import name.martingeisse.stackerspace.ingame.network.SendPositionToServerHandler;
import name.martingeisse.stackerspace.ingame.player.PlayerProxy;

/**
 * The in-game frame handler
 */
public class IngameHandler extends HandlerList {

	/**
	 * the cubeWorldHandler
	 */
	public static CubeWorldHandler cubeWorldHandler;

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
	public static StackerspaceProtocolClient protocolClient;

	/**
	 * the flashMessageHandler
	 */
	public static FlashMessageHandler flashMessageHandler;
	
	/**
	 * the gameMenuHandler
	 */
	public static GameMenuHandler gameMenuHandler;
	
	/**
	 * the ingameMenuHandlerWrapper
	 */
	public static SwappableHandler gameMenuHandlerWrapper;

	/**
	 * Constructor.
	 * @throws Exception on errors
	 */
	public IngameHandler() throws Exception {

		// determine server name
		serverName = "localhost";

		// connect to the server
		StackerspaceResources resources = StackerspaceResources.getInstance();
		flashMessageHandler = new FlashMessageHandler(resources.getFont());
		protocolClient = new StackerspaceProtocolClient();
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
					cubeWorldHandler.getPlayer().getPosition().copyFrom(playerResumedMessage.getPosition());
					cubeWorldHandler.getPlayer().getOrientation().copyFrom(playerResumedMessage.getOrientation());
					protocolClient.getSectionGridLoader().setViewerPosition(cubeWorldHandler.getPlayer().getSectionId());
				}
			}
		});
		add(flashMessageHandler);
		
		// the in-game menu
		gameMenuHandler = new GameMenuHandler();
		gameMenuHandlerWrapper = new SwappableHandler();
		add(gameMenuHandlerWrapper);

		// prepare running the game
		protocolClient.setFlashMessageHandler(flashMessageHandler);
		protocolClient.waitUntilReady();
		MouseUtil.grab();
		
	}

}
