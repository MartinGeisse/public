/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.miner.ingame;

import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_Q;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_R;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_GEN_T;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4ub;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRasterPos3f;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.glVertex3i;
import static org.lwjgl.opengl.GL14.glWindowPos2i;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import name.martingeisse.common.util.ThreadUtil;
import name.martingeisse.miner.common.MinerCommonConstants;
import name.martingeisse.miner.common.MinerCubeTypes;
import name.martingeisse.miner.ingame.player.Player;
import name.martingeisse.miner.ingame.player.PlayerProxy;
import name.martingeisse.stackd.client.engine.EngineParameters;
import name.martingeisse.stackd.client.engine.FrameRenderParameters;
import name.martingeisse.stackd.client.engine.WorldWorkingSet;
import name.martingeisse.stackd.client.engine.renderer.DefaultSectionRenderer;
import name.martingeisse.stackd.client.frame.AbstractIntervalFrameHandler;
import name.martingeisse.stackd.client.frame.FrameDurationSensor;
import name.martingeisse.stackd.client.glworker.GlWorkUnit;
import name.martingeisse.stackd.client.glworker.GlWorkerLoop;
import name.martingeisse.stackd.client.network.CubeModificationPacketBuilder;
import name.martingeisse.stackd.client.network.SectionGridLoader;
import name.martingeisse.stackd.client.sound.RegularSound;
import name.martingeisse.stackd.client.system.Font;
import name.martingeisse.stackd.client.util.MouseUtil;
import name.martingeisse.stackd.client.util.RayAction;
import name.martingeisse.stackd.client.util.RayActionSupport;
import name.martingeisse.stackd.common.StackdConstants;
import name.martingeisse.stackd.common.geometry.AxisAlignedDirection;
import name.martingeisse.stackd.common.geometry.RectangularRegion;
import name.martingeisse.stackd.common.util.ProfilingHelper;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

/**
 * TODO: document me
 *
 */
public class CubeWorldHandler {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(CubeWorldHandler.class);

	/**
	 * the MAX_STAIRS_HEIGHT
	 */
	public static final double MAX_STAIRS_HEIGHT = 0.8;

	/**
	 * the player
	 */
	private final Player player;

	/**
	 * the resources
	 */
	private final MinerResources resources;

	/**
	 * the workingSet
	 */
	private final WorldWorkingSet workingSet;

	/**
	 * the infoButtonPressed
	 */
	private boolean infoButtonPressed;

	/**
	 * the rayActionSupport
	 */
	private final RayActionSupport rayActionSupport;
	
	/**
	 * the captureRayActionSupport
	 */
	private boolean captureRayActionSupport;

	/**
	 * the wireframe
	 */
	private boolean wireframe;

	/**
	 * the grid
	 */
	private boolean grid;

	/**
	 * the screenWidth
	 */
	private final int screenWidth;

	/**
	 * the screenHeight
	 */
	private final int screenHeight;

	/**
	 * the aspectRatio
	 */
	private final float aspectRatio;

	/**
	 * the currentCubeType
	 */
	private byte currentCubeType = 1;

	/**
	 * the frameDurationSensor
	 */
	private final FrameDurationSensor frameDurationSensor;

	/**
	 * the playerProxies
	 */
	private List<PlayerProxy> playerProxies;

	/**
	 * the playerNames
	 */
	private Map<Integer, String> playerNames;

	/**
	 * the minusPressed
	 */
	private boolean minusPressed;

	/**
	 * the flashMessageCounter
	 */
	private int flashMessageCounter = 0;

	/**
	 * the footstepSound
	 */
	private RegularSound footstepSound;

	/**
	 * the walking
	 */
	private boolean walking;

	/**
	 * the cooldownFinishTime
	 */
	private long cooldownFinishTime;

	/**
	 * the previousConnectionProblemInstant
	 */
	private Instant previousConnectionProblemInstant = new Instant();

	/**
	 * The sectionLoadHandler -- checks often (100 ms), but doesn't re-request frequently (5 sec)
	 * to avoid re-requesting a section again and again while the server is loading it.
	 * 
	 * TODO should be resettable for edge cases where frequent reloading is needed, such as
	 * falling down from high places.
	 * This handler checks if sections must be loaded.
	 */
	private AbstractIntervalFrameHandler sectionLoadHandler = new AbstractIntervalFrameHandler(100) {

		private int requestCooldown = 0;

		@Override
		protected void onIntervalTimerExpired() {
			if (requestCooldown == 0) {
				IngameHandler.protocolClient.getSectionGridLoader().setViewerPosition(player.getSectionId());
				if (IngameHandler.protocolClient.getSectionGridLoader().update()) {
					requestCooldown = 50;
				}
			} else {
				requestCooldown--;
			}
		}
	};

	/**
	 * Constructor.
	 * @param width the width of the framebuffer
	 * @param height the height of the framebuffer
	 * @param resources the resources
	 * @throws IOException on I/O errors while loading the textures
	 */
	public CubeWorldHandler(final int width, final int height, final MinerResources resources) throws IOException {

		// the resources (textures)
		this.resources = resources;

		// the world
		final DefaultSectionRenderer sectionRenderer = new DefaultSectionRenderer();
		sectionRenderer.prepareForTextures(resources.getCubeTextures());
		final EngineParameters engineParameters = new EngineParameters(sectionRenderer, resources.getCubeTextures(), MinerCubeTypes.CUBE_TYPES);
		workingSet = new WorldWorkingSet(engineParameters, MinerCommonConstants.CLUSTER_SIZE);

		// the player
		player = new Player(workingSet);
		player.getPosition().setX(0);
		player.getPosition().setY(10);
		player.getPosition().setZ(0);

		// other stuff
		rayActionSupport = new RayActionSupport(width, height);
		screenWidth = width;
		screenHeight = height;
		aspectRatio = (float)width / (float)height;
		frameDurationSensor = new FrameDurationSensor();
		playerProxies = new ArrayList<PlayerProxy>();
		playerNames = new HashMap<Integer, String>();
		footstepSound = new RegularSound(resources.getFootstep(), 500);
		cooldownFinishTime = System.currentTimeMillis();

		// TODO: implement better checking for connection problems: only stall when surrounding sections
		// are missing AND the player is in that half of the current section. currently using collider
		// radius 2 to avoid "connection problems" when crossing a section boundary
		IngameHandler.protocolClient.setSectionGridLoader(new SectionGridLoader(workingSet, IngameHandler.protocolClient, 3, 2));

	}

	/**
	 * Getter method for the resources.
	 * @return the resources
	 */
	public MinerResources getResources() {
		return resources;
	}

	/**
	 * Getter method for the currentCubeType.
	 * @return the currentCubeType
	 */
	public byte getCurrentCubeType() {
		return currentCubeType;
	}

	/**
	 * Getter method for the workingSet.
	 * @return the workingSet
	 */
	public WorldWorkingSet getWorkingSet() {
		return workingSet;
	}

	/**
	 * Getter method for the player.
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Getter method for the playerProxies.
	 * @return the playerProxies
	 */
	public List<PlayerProxy> getPlayerProxies() {
		return playerProxies;
	}

	/**
	 * Setter method for the playerProxies.
	 * @param playerProxies the playerProxies to set
	 */
	public void setPlayerProxies(final List<PlayerProxy> playerProxies) {
		this.playerProxies = playerProxies;
	}

	/**
	 * Getter method for the playerNames.
	 * @return the playerNames
	 */
	public Map<Integer, String> getPlayerNames() {
		return playerNames;
	}

	/**
	 * Setter method for the playerNames.
	 * @param playerNames the playerNames to set
	 */
	public void setPlayerNames(final Map<Integer, String> playerNames) {
		this.playerNames = playerNames;
	}

	/**
	 * 
	 */
	public void step() {
		final boolean keysEnabled = IngameHandler.gameMenuHandlerWrapper.getWrappedHandler() == null;
		final boolean mouseMovementEnabled = IngameHandler.gameMenuHandlerWrapper.getWrappedHandler() == null;

		// first, handle the stuff that already works without the world being loaded "enough"
		frameDurationSensor.tick();
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_I)) {
			if (!infoButtonPressed) {
				player.dump();
			}
			infoButtonPressed = true;
		} else {
			infoButtonPressed = false;
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_P)) {
			player.setObserverMode(false);
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_O)) {
			player.setObserverMode(true);
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_G)) {
			grid = true;
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_H)) {
			grid = false;
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_K)) {
			MouseUtil.ungrab();
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_L)) {
			MouseUtil.grab();
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_SLASH) && !minusPressed) {
			IngameHandler.flashMessageHandler.addMessage("foobar! " + flashMessageCounter);
			flashMessageCounter++;
		}
		minusPressed = keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_SLASH);
		wireframe = keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_F);
		player.setWantsToJump(keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_SPACE));
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_1)) {
			currentCubeType = 1;
		} else if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_2)) {
			currentCubeType = 2;
		} else if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_3)) {
			currentCubeType = 3;
		} else if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_4)) {
			currentCubeType = 50;
		}
		if (mouseMovementEnabled) {
			player.getOrientation().setHorizontalAngle(player.getOrientation().getHorizontalAngle() - Mouse.getDX() * 0.5);
			double newUpAngle = player.getOrientation().getVerticalAngle() + Mouse.getDY() * 0.5;
			newUpAngle = (newUpAngle > 90) ? 90 : (newUpAngle < -90) ? -90 : newUpAngle;
			player.getOrientation().setVerticalAngle(newUpAngle);
		}
		sectionLoadHandler.handleStep();

		// process keyboard events -- needed for flawless GUI toggling
		// TODO properly disable all keyboard / mouse handling in the CubeWorldHandler when the GUI is active
		if (keysEnabled) {
			while (Keyboard.next()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
					IngameHandler.gameMenuHandlerWrapper.setWrappedHandler(IngameHandler.gameMenuHandler);
					MouseUtil.ungrab();
				}
			}
		}

		// check if the world is loaded "enough"
		workingSet.acceptLoadedSections();
		if (!workingSet.hasAllRenderModels(player.getSectionId(), 1) || !workingSet.hasAllColliders(player.getSectionId(), 1)) {
			final Instant now = new Instant();
			if (new Duration(previousConnectionProblemInstant, now).getMillis() >= 1000) {
				logger.warn("connection problems");
				ThreadUtil.dumpThreads(Level.INFO);
				previousConnectionProblemInstant = now;
			}
			return;
		}

		// ---------------------------------------------------------------------------------------------------
		// now, handle the stuff that only works with enough information from the world
		// ---------------------------------------------------------------------------------------------------

		// normal movement: If on the ground, we move the player step-up, then front/side, then step-down.
		// This way the player can climb stairs while walking. In the air, this boils down to front/side movement.
		// We also keep track if the player is walking (front/side) for a "walking" sound effect.
		double speed = keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_TAB) ? 10.0 : keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? 3.0 : 1.5;
		speed *= frameDurationSensor.getMultiplier();
		walking = false;
		double forward = 0, right = 0;
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_A)) {
			right = -speed;
			walking = true;
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_D)) {
			right = speed;
			walking = true;
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_W)) {
			forward = speed;
			walking = true;
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_S)) {
			forward = -speed;
			walking = true;
		}
		player.moveHorizontal(forward, right, player.isOnGround() ? MAX_STAIRS_HEIGHT : 0);

		// special movement
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_C)) {
			player.moveUp(-speed);
		}
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_E)) {
			player.moveUp(speed);
		}

		// cube placement
		captureRayActionSupport = false;
		final long now = System.currentTimeMillis();
		if (now >= cooldownFinishTime) {
			if (mouseMovementEnabled && Mouse.isButtonDown(0)) {
				captureRayActionSupport = true;
				rayActionSupport.execute(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new RayAction(false) {
					@Override
					public void handleImpact(final int x, final int y, final int z, final double distance) {
						if (distance < 3.0) {
							final byte effectiveCubeType;
							if (currentCubeType == 50) {
								final int angle = ((int)player.getOrientation().getHorizontalAngle() % 360 + 360) % 360;
								if (angle < 45) {
									effectiveCubeType = 52;
								} else if (angle < 45 + 90) {
									effectiveCubeType = 50;
								} else if (angle < 45 + 180) {
									effectiveCubeType = 53;
								} else if (angle < 45 + 270) {
									effectiveCubeType = 51;
								} else {
									effectiveCubeType = 52;
								}
							} else {
								effectiveCubeType = currentCubeType;
							}

							/* TODO: The call to breakFree() will remove a stairs cube if the player is standing
							 * on the lower step, because the player's bounding box intersects with the cube's
							 * bounding box. Solution 1: Remove breakFree(), don't place a cube if the player then
							 * collides. Solution 2: Make breakFree() more accurate.
							 */
							final CubeModificationPacketBuilder builder = new CubeModificationPacketBuilder();
							builder.addModification(x, y, z, effectiveCubeType);
							breakFree(builder);
							IngameHandler.protocolClient.send(builder.getPacket());
							// cooldownFinishTime = now + 1000;
							cooldownFinishTime = now + 200;
						}
					}
				});
			} else if (mouseMovementEnabled && Mouse.isButtonDown(1)) {
				captureRayActionSupport = true;
				rayActionSupport.execute(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), new RayAction(true) {
					@Override
					public void handleImpact(final int x, final int y, final int z, final double distance) {
						if (distance < 2.0) {
							IngameHandler.protocolClient.sendDigNotification(x, y, z);
							resources.getHitCube().playAsSoundEffect(1.0f, 1.0f, false);
							// cooldownFinishTime = now + 1000;
							cooldownFinishTime = now + 200;
						}
					}
				});
			}
		}

		// special actions
		if (keysEnabled && Keyboard.isKeyDown(Keyboard.KEY_B)) {
			final CubeModificationPacketBuilder builder = new CubeModificationPacketBuilder();
			breakFree(builder);
			IngameHandler.protocolClient.send(builder.getPacket());
		}

		// handle player logic
		player.step(frameDurationSensor.getMultiplier());

		// handle sound effects
		if (player.isOnGround() && walking) {
			footstepSound.handleActiveTime();
		} else {
			footstepSound.reset();
		}
		if (player.isJustLanded()) {
			resources.getLandOnGround().playAsSoundEffect(1.0f, 1.0f, false);
		}

	}

	/**
	 * 
	 */
	private void breakFree(final CubeModificationPacketBuilder builder) {
		final RectangularRegion region = player.createCollisionRegion();
		for (int x = region.getStartX(); x < region.getEndX(); x++) {
			for (int y = region.getStartY(); y < region.getEndY(); y++) {
				for (int z = region.getStartZ(); z < region.getEndZ(); z++) {
					builder.addModification(x, y, z, (byte)0);
				}
			}
		}
	}

	/**
	 * 
	 */
	public void purge() {
	}

	/**
	 * @param glWorkerLoop the OpenGL worker loop
	 */
	public void draw(final GlWorkerLoop glWorkerLoop) {

		// determine player's position as integers
		final int playerX = (int)(Math.floor(player.getPosition().getX()));
		final int playerY = (int)(Math.floor(player.getPosition().getY()));
		final int playerZ = (int)(Math.floor(player.getPosition().getZ()));

		// set the GL worker loop for the section renderer
		((DefaultSectionRenderer)workingSet.getEngineParameters().getSectionRenderer()).setGlWorkerLoop(glWorkerLoop);

		// run preparation code in the OpenGL worker thread
		glWorkerLoop.schedule(new GlWorkUnit() {
			@Override
			public void execute() {

				// profiling
				ProfilingHelper.start();

				// set up projection matrix
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				gluPerspective(60, aspectRatio, 0.1f, 10000.0f);

				// set up modelview matrix
				final int geometryDetailFactor = StackdConstants.GEOMETRY_DETAIL_FACTOR;
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity(); // model transformation (direct)
				glRotatef((float)player.getOrientation().getVerticalAngle(), -1, 0, 0); // view transformation (reversed)
				glRotatef((float)player.getOrientation().getHorizontalAngle(), 0, -1, 0); // ...
				glTranslated(-player.getPosition().getX() * geometryDetailFactor, -player.getPosition().getY() * geometryDetailFactor, -player.getPosition().getZ() * geometryDetailFactor); // ...

				// clear the screen
				glDepthMask(true);
				glClearColor(0.5f, 0.5f, 1.0f, 1.0f);
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

				// some more preparation
				glDepthFunc(GL_LESS);
				glEnable(GL_DEPTH_TEST);
				((DefaultSectionRenderer)workingSet.getEngineParameters().getSectionRenderer()).setWireframe(wireframe);
				((DefaultSectionRenderer)workingSet.getEngineParameters().getSectionRenderer()).setTexturing(IngameHandler.enableTexturing);
				((DefaultSectionRenderer)workingSet.getEngineParameters().getSectionRenderer()).setTextureCoordinateGeneration(IngameHandler.enableTexGen);

			}
		});

		// actually draw the world TODO pass the GL worker
		workingSet.draw(new FrameRenderParameters(playerX, playerY, playerZ));

		// post-draw code, again in the GL worker thread
		glWorkerLoop.schedule(new GlWorkUnit() {
			@Override
			public void execute() {
				final int geometryDetailFactor = StackdConstants.GEOMETRY_DETAIL_FACTOR;

				// measure visible distance in the center of the crosshair, with only the world visible (no HUD or similar)
				// TODO only call if needed, this stalls the rendering pipeline --> 2x frame rate possible!
				if (captureRayActionSupport) {
					rayActionSupport.capture();
				} else {
					rayActionSupport.release();
				}

				// draw the sky
				glDisable(GL_TEXTURE_GEN_S);
				glDisable(GL_TEXTURE_GEN_T);
				glDisable(GL_TEXTURE_GEN_Q);
				glDisable(GL_TEXTURE_GEN_R);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glEnable(GL_TEXTURE_2D);
				resources.getClouds().glBindTexture();
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				final float tex = 10.0f;
				glColor3f(1.0f, 1.0f, 1.0f);
				glBegin(GL_QUADS);
				glTexCoord2f(0, 0);
				glVertex3i(-100000, 1000, -100000);
				glTexCoord2f(tex, 0);
				glVertex3i(+100000, 1000, -100000);
				glTexCoord2f(tex, tex);
				glVertex3i(+100000, 1000, +100000);
				glTexCoord2f(0, tex);
				glVertex3i(-100000, 1000, +100000);
				glEnd();
				glDisable(GL_BLEND);

				// draw the grid
				if (grid) {
					glDisable(GL_TEXTURE_2D);
					glColor3f(1.0f, 1.0f, 1.0f);
					final int sectionX = playerX >> MinerCommonConstants.CLUSTER_SIZE.getShiftBits();
					final int sectionY = playerY >> MinerCommonConstants.CLUSTER_SIZE.getShiftBits();
					final int sectionZ = playerZ >> MinerCommonConstants.CLUSTER_SIZE.getShiftBits();
					final int distance = 48;
					glLineWidth(2.0f);
					glBegin(GL_LINES);
					for (int u = -3; u <= 4; u++) {
						for (int v = -3; v <= 4; v++) {
							for (final AxisAlignedDirection direction : AxisAlignedDirection.values()) {
								if (direction.isNegative()) {
									continue;
								}
								final int x = geometryDetailFactor * MinerCommonConstants.CLUSTER_SIZE.getSize() * (sectionX + direction.selectByAxis(0, u, v));
								final int dx = geometryDetailFactor * direction.selectByAxis(distance, 0, 0);
								final int y = geometryDetailFactor * MinerCommonConstants.CLUSTER_SIZE.getSize() * (sectionY + direction.selectByAxis(v, 0, u));
								final int dy = geometryDetailFactor * direction.selectByAxis(0, distance, 0);
								final int z = geometryDetailFactor * MinerCommonConstants.CLUSTER_SIZE.getSize() * (sectionZ + direction.selectByAxis(u, v, 0));
								final int dz = geometryDetailFactor * direction.selectByAxis(0, 0, distance);
								glVertex3f(x + dx, y + dy, z + dz);
								glVertex3f(x - dx, y - dy, z - dz);
							}
						}
					}
					glEnd();
				}

				// draw player proxies (i.e. other players)
				glBindTexture(GL_TEXTURE_2D, 0);
				glEnable(GL_BLEND);
				glMatrixMode(GL_MODELVIEW);
				for (final PlayerProxy playerProxy : playerProxies) {
					if (playerProxy.getId() != IngameHandler.protocolClient.getSessionId()) {

						// set a color that is computed from the player's session ID
						final Random random = new Random(playerProxy.getId());
						glColor4ub((byte)random.nextInt(255), (byte)random.nextInt(255), (byte)random.nextInt(255), (byte)127);

						// Set up inverse modelview matrix, draw, then restore previous matrix.
						// Also set the raster position for drawing the name.
						glPushMatrix();
						glTranslated(geometryDetailFactor * playerProxy.getPosition().getX(), geometryDetailFactor * playerProxy.getPosition().getY(), geometryDetailFactor * playerProxy.getPosition().getZ());
						glRotatef((float)playerProxy.getOrientation().getHorizontalAngle(), 0, 1, 0);
						glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
						new Sphere().draw(0.8f, 10, 10);
						glRasterPos3f(0f, 1.5f, 0f);
						glPopMatrix();

						// compute the distance between the players
						final double dx = player.getPosition().getX() - playerProxy.getPosition().getX();
						final double dy = player.getPosition().getY() - playerProxy.getPosition().getY();
						final double dz = player.getPosition().getZ() - playerProxy.getPosition().getZ();
						final double distance = geometryDetailFactor * Math.sqrt(dx * dx + dy * dy + dz * dz);
						final double zoom = 5.0 / (Math.sqrt(distance) + 0.5);

						// draw the player's name
						String name = playerNames.get(playerProxy.getId());
						if (name == null) {
							name = "...";
						}
						glBindTexture(GL_TEXTURE_2D, 0);
						glDisable(GL_BLEND);
						glDepthFunc(GL_ALWAYS);
						GL11.glPixelTransferf(GL11.GL_RED_BIAS, 1.0f);
						GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 1.0f);
						GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 1.0f);
						resources.drawText(name, (float)zoom);
						GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.0f);
						GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.0f);
						GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.0f);
						glDepthFunc(GL_LESS);

					}
				}
				glDisable(GL_BLEND);

				// draw the crosshair
				glLineWidth(1.0f);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				glDisable(GL_DEPTH_TEST);
				glDisable(GL_TEXTURE_2D);
				glColor3f(1.0f, 1.0f, 1.0f);
				glBegin(GL_LINES);
				glVertex2f(-0.1f, 0.0f);
				glVertex2f(+0.1f, 0.0f);
				glVertex2f(0.0f, -0.1f);
				glVertex2f(0.0f, +0.1f);
				glEnd();

				// draw the HUD
				glBindTexture(GL_TEXTURE_2D, 0);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glWindowPos2i(screenWidth, screenHeight - 30);
				GL11.glPixelTransferf(GL11.GL_RED_BIAS, 1.0f);
				GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 1.0f);
				GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 1.0f);
				resources.getFont().drawText("coins: " + IngameHandler.protocolClient.getCoins(), 2, Font.ALIGN_RIGHT, Font.ALIGN_TOP);
				GL11.glPixelTransferf(GL11.GL_RED_BIAS, 0.0f);
				GL11.glPixelTransferf(GL11.GL_GREEN_BIAS, 0.0f);
				GL11.glPixelTransferf(GL11.GL_BLUE_BIAS, 0.0f);

				// profiling
				ProfilingHelper.checkRelevant("draw", 50);

			}
		});

	}

}
