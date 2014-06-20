/**
 * Copyright (c) 2012 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.stackerspace.ingame;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4ub;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import name.martingeisse.stackd.client.system.FixedWidthFont;
import name.martingeisse.stackd.client.system.Font;
import name.martingeisse.stackd.client.system.StackdTexture;
import name.martingeisse.stackerspace.assets.Assets;
import name.martingeisse.stackerspace.common.StackerspaceCubeTypes;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.ResourceLoader;

/**
 * 
 */
public class StackerspaceResources {

	/**
	 * the KEY_COLOR
	 */
	public static final int[] KEY_COLOR = {255, 0, 255};
	
	/**
	 * the instance
	 */
	private static StackerspaceResources instance;
	
	/**
	 * Initializes the instance of this class and loads all resources.
	 * @throws IOException on I/O errors
	 */
	public synchronized static void initializeInstance() throws IOException {
		instance = new StackerspaceResources();
	}
	
	/**
	 * Getter method for the instance.
	 * @return the instance
	 */
	public static StackerspaceResources getInstance() {
		return instance;
	}
	
	/**
	 * the cubeTextures
	 */
	private final StackdTexture[] cubeTextures;

	/**
	 * the clouds
	 */
	private final StackdTexture clouds;

	/**
	 * the font
	 */
	private final Font font;
	
	/**
	 * the footstep
	 */
	private final Audio footstep;
	
	/**
	 * the hitCube
	 */
	private final Audio hitCube;

	/**
	 * the landOnGround
	 */
	private final Audio landOnGround;

	/**
	 * Constructor. The resources are immediately loaded.
	 * @throws IOException on I/O errors
	 */
	private StackerspaceResources() throws IOException {

		// load cube textures
		final String[] cubeTextureNames = StackerspaceCubeTypes.CUBE_TEXTURE_FILENAMES;
		cubeTextures = new StackdTexture[cubeTextureNames.length];
		for (int i = 0; i < cubeTextures.length; i++) {
			cubeTextures[i] = new StackdTexture(Assets.class, cubeTextureNames[i], false);
		}

		// load special textures
		clouds = new StackdTexture(Assets.class, "clouds.png", false);
		font = new FixedWidthFont(loadImage("font.png"), 8, 16);

		// load sounds
		ResourceLoader.addResourceLocation(new ClasspathLocation());
		footstep = loadOggSound("footstep-1.ogg");
		hitCube = loadOggSound("hit-cube-1.ogg");
		landOnGround = loadOggSound("land.ogg");
		
	}

	/**
	 * @param filename the filename of the OGG, relative to the assets folder
	 * @return the sound
	 * @throws IOException on I/O errors
	 */
	private Audio loadOggSound(final String filename) throws IOException {
		try (InputStream inputStream = Assets.class.getResourceAsStream(filename)) {
			return AudioLoader.getAudio("OGG", inputStream);
		}
	}

	/**
	 * Loads a PNG image the AWT way.
	 * @param filename the filename of the PNG, relative to the assets folder
	 * @return the luminance buffer
	 * @throws IOException on I/O errors
	 */
	private BufferedImage loadImage(final String filename) throws IOException {
		try (InputStream inputStream = Assets.class.getResourceAsStream(filename)) {
			return  ImageIO.read(inputStream);
		}
	}
	
	/**
	 * Getter method for the cubeTextures.
	 * @return the cubeTextures
	 */
	public StackdTexture[] getCubeTextures() {
		return cubeTextures;
	}
	
	/**
	 * Getter method for the clouds.
	 * @return the clouds
	 */
	public StackdTexture getClouds() {
		return clouds;
	}

	/**
	 * Getter method for the font.
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * Draws text to the current raster position.
	 * @param s the text to draw
	 * @param zoom the zoom factor for the text
	 */
	public void drawText(final String s, final float zoom) {
		glColor4ub((byte)255, (byte)255, (byte)255, (byte)255);
		glBindTexture(GL_TEXTURE_2D, 0);
		font.drawText(s, zoom, Font.ALIGN_CENTER, Font.ALIGN_CENTER);
	}

	/**
	 * Getter method for the footstep.
	 * @return the footstep
	 */
	public Audio getFootstep() {
		return footstep;
	}
	
	/**
	 * Getter method for the hitCube.
	 * @return the hitCube
	 */
	public Audio getHitCube() {
		return hitCube;
	}

	/**
	 * Getter method for the landOnGround.
	 * @return the landOnGround
	 */
	public Audio getLandOnGround() {
		return landOnGround;
	}
	
}
