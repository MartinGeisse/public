/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import name.martingeisse.wicket.application.AbstractMyWicketApplication;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.resource.IResourceStream;

/**
 * Registry for the atlas images that contain CSS sprites. A single
 * instance of this class is typically kept by the application.
 */
public final class SpriteRegistry {

	/**
	 * the application
	 */
	private final AbstractMyWicketApplication application;
	
	/**
	 * the registry
	 */
	private final ConcurrentHashMap<ResourceReference.Key, SpriteReference> registry;
	
	/**
	 * the atlasCounter
	 */
	private int atlasCounter;
	
	/**
	 * Constructor.
	 * @param application the application
	 */
	public SpriteRegistry(AbstractMyWicketApplication application) {
		this.application = application;
		this.registry = new ConcurrentHashMap<ResourceReference.Key, SpriteReference>();
		this.atlasCounter = 0;
	}

	/**
	 * Registers a sprite atlas containing the specified image resources.
	 * When a {@link SpriteImage} uses any of those references, it will turn
	 * into a CSS sprite.
	 * 
	 * @param references the image resource references
	 */
	public void register(PackageResourceReference... references) {
		try {
			BufferedImage[] spriteImages = loadSpriteImages(references);
			ByteArrayOutputStream atlasByteArrayOutputStream = new ByteArrayOutputStream();
			SpriteReference[] spriteReferences = buildAtlas(atlasByteArrayOutputStream, spriteImages);
			byte[] serializedAtlasImage = atlasByteArrayOutputStream.toByteArray();
			SpriteAtlas atlas = new SpriteAtlas(application, "atlas" + atlasCounter + ".png", "image/png", serializedAtlasImage);
			atlasCounter++;
			for (int i=0; i<spriteReferences.length; i++) {
				spriteReferences[i] = spriteReferences[i].withAtlas(atlas);
				registry.put(references[i].getKey(), spriteReferences[i]);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Loads the individual sprite images.
	 */
	private BufferedImage[] loadSpriteImages(PackageResourceReference[] references) {
		try {
			BufferedImage[] result = new BufferedImage[references.length];
			for (int i=0; i<references.length; i++) {
				IResourceStream resourceStream = references[i].getResource().getResourceStream();
				InputStream inputStream = resourceStream.getInputStream();
				try {
					result[i] = ImageIO.read(inputStream);
				} finally {
					resourceStream.close();
				}
			}
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Builds an atlas image, writes it to the specified output stream, and returns
	 * sprite references. The sprite references have no atlas set yet.
	 */
	private SpriteReference[] buildAtlas(OutputStream outputStream, BufferedImage[] spriteImages) throws IOException {
		
		// determine the atlas size
		int sumWidth = 0, maxHeight = 0;
		for (BufferedImage image : spriteImages) {
			sumWidth += image.getWidth();
			if (maxHeight < image.getHeight()) {
				maxHeight = image.getHeight();
			}
		}
		
		// build the atlas image
		BufferedImage atlasImage = new BufferedImage(sumWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
		SpriteReference[] spriteReferences = new SpriteReference[spriteImages.length];
		{
			int x = 0, i = 0;
			Graphics g = atlasImage.getGraphics();
			for (BufferedImage image : spriteImages) {
				g.drawImage(image, x, 0, null);
				spriteReferences[i] = new SpriteReference(null, x, 0, image.getWidth(), image.getHeight());
				x += image.getWidth();
				i++;
			}
			g.dispose();
		}
		
		// write the atlas image to the output stream
		ImageIO.write(atlasImage, "png", outputStream);
		
		return spriteReferences;
	}
	
	/**
	 * Looks for the specified resource represented as a CSS sprite.
	 * @param spriteKey the resource reference key
	 * @return the sprite reference, or null if not found
	 */
	public SpriteReference lookup(ResourceReference.Key spriteKey) {
		return registry.get(spriteKey);
	}
	
}
