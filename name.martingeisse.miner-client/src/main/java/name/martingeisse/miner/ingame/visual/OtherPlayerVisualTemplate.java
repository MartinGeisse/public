/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.miner.ingame.visual;

import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4ub;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRasterPos3f;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.util.Random;

import name.martingeisse.miner.ingame.CubeWorldHandler;
import name.martingeisse.miner.ingame.MinerResources;
import name.martingeisse.miner.ingame.player.Player;
import name.martingeisse.miner.ingame.player.PlayerProxy;
import name.martingeisse.stackd.client.glworker.AbstractSingleWorkUnitVisualTemplate;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

/**
 * Represents another player (i.e. a {@link PlayerProxy}).
 */
public final class OtherPlayerVisualTemplate extends AbstractSingleWorkUnitVisualTemplate<PlayerProxy> {

	/**
	 * the resources
	 */
	private final MinerResources resources;

	/**
	 * the ownPlayer
	 */
	private final Player ownPlayer;

	/**
	 * the playerNamesContainer
	 */
	private final CubeWorldHandler playerNamesContainer;

	/**
	 * Constructor.
	 * @param resources the resources
	 * @param ownPlayer this process's own player (used to compute the distance to
	 * the other player)
	 * @param playerNamesContainer the object that contains all players' names
	 */
	public OtherPlayerVisualTemplate(MinerResources resources, Player ownPlayer, CubeWorldHandler playerNamesContainer) {
		this.resources = resources;
		this.ownPlayer = ownPlayer;
		this.playerNamesContainer = playerNamesContainer;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.stackd.client.glworker.SingleWorkUnitVisualTemplate#renderEmbedded(java.lang.Object)
	 */
	@Override
	public void renderEmbedded(PlayerProxy playerProxy) {

		// set a color that is computed from the player's session ID
		final Random random = new Random(playerProxy.getId());
		glColor4ub((byte)random.nextInt(255), (byte)random.nextInt(255), (byte)random.nextInt(255), (byte)127);

		// Set up inverse modelview matrix, draw, then restore previous matrix.
		// Also set the raster position for drawing the name.
		glPushMatrix();
		glTranslated(playerProxy.getPosition().getX(), playerProxy.getPosition().getY(), playerProxy.getPosition().getZ());
		glRotatef((float)playerProxy.getOrientation().getHorizontalAngle(), 0, 1, 0);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		new Sphere().draw(0.8f, 10, 10);
		glRasterPos3f(0f, 1.5f, 0f);
		glPopMatrix();

		// compute the distance between the players
		final double dx = ownPlayer.getPosition().getX() - playerProxy.getPosition().getX();
		final double dy = ownPlayer.getPosition().getY() - playerProxy.getPosition().getY();
		final double dz = ownPlayer.getPosition().getZ() - playerProxy.getPosition().getZ();
		final double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);
		final double zoom = 5.0 / (Math.sqrt(distance) + 0.5);

		// draw the player's name
		String name = playerNamesContainer.getPlayerNames().get(playerProxy.getId());
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
