/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.sprite;

import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * {@link Image} subclass that supports CSS sprites.
 * 
 * Note that the image and its resource reference will resolve the
 * localized image file *before* rendering the IMG tag, then generate
 * a localized URL for the file depending on the session locale, locale
 * override in the resource reference, and actually available localized
 * image files. Fallback logic in the HTTP request for the image is
 * supported, but no such fallback and no localization is used by the
 * {@link Image} class; it's all done while rendering the markup.
 * This is good for us because we need localized information while
 * rendering the IMG tag too, to look up the sprite reference and
 * generate coordinates, so we just need to hook into the existing
 * process.
 * 
 * TODO: find out *how* to hook into that process.
 */
public class SpriteImage extends Image {

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param model the model
	 */
	public SpriteImage(String id, IModel<?> model) {
		super(id, model);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param imageResource the image resource
	 */
	public SpriteImage(String id, IResource imageResource) {
		super(id, imageResource);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param resourceReference the image resource reference
	 * @param resourceParameters the image resource parameters
	 */
	public SpriteImage(String id, ResourceReference resourceReference, PageParameters resourceParameters) {
		super(id, resourceReference, resourceParameters);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param resourceReference the image resource reference
	 */
	public SpriteImage(String id, ResourceReference resourceReference) {
		super(id, resourceReference);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 * @param string the image URL
	 */
	public SpriteImage(String id, String string) {
		super(id, string);
	}

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public SpriteImage(String id) {
		super(id);
	}

	
}
