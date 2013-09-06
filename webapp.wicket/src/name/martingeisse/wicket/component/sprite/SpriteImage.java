/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.sprite;

import name.martingeisse.wicket.application.AbstractMyWicketApplication;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.PackageResource;
import org.apache.wicket.request.resource.ResourceReference;

/**
 * {@link Image} subclass that supports CSS sprites. This class operates purely
 * on resource references. Even {@link PackageResource} is not supported
 * because we cannot obtain the locale from it. Parameters are also ignored.
 * 
 * Internal notes:
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

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.image.Image#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(ComponentTag tag) {
		AbstractMyWicketApplication application = (AbstractMyWicketApplication)getApplication();
		
		// check for allowed tags
		final String originalTagName = tag.getName();
		if (!originalTagName.equalsIgnoreCase("img") && !originalTagName.equalsIgnoreCase("span") && !originalTagName.equalsIgnoreCase("div")) {
			String msg = String.format("Component [%s] (path = [%s]) must be applied to a tag of type IMG, SPAN or DIV, not: %s", getId(), getPath(), tag.toUserDebugString());
			findMarkupStream().throwMarkupException(msg);
		}
		
		// make the image generate its resource reference (e.g. from a specified path), ignoring the tag name for now
		try {
			tag.setName("img");
			super.onComponentTag(tag);
		} finally {
			tag.setName(originalTagName);
		}

		// localize the resource reference (including a fallback mechanism for nonexisting resource files)
		ResourceReference resourceReference = getImageResourceReference();
		ResourceReference.UrlAttributes urlAttributes = resourceReference.getUrlAttributes();
		ResourceReference.Key spriteKey = new ResourceReference.Key(resourceReference.getScope().getName(), resourceReference.getName(), urlAttributes.getLocale(), urlAttributes.getStyle(), urlAttributes.getVariation());
		
		// look up the sprite and fall back to default behavior if not found
		SpriteReference spriteReference = application.getSpriteRegistry().lookup(spriteKey);
		if (spriteReference == null) {
			super.onComponentTag(tag);
			return;
		}
		
		// IMG tags cannot handle CSS sprites. They're inline, so we convert to a SPAN.
		if (tag.getName().equalsIgnoreCase("img")) {
			tag.setName("span");
		}
		
		// remove the SRC attribute -- it doesn't hurt, but it's ugly
		tag.getAttributes().remove("src");
		
		// prepare modification of the style attribute
		StringBuilder styleBuilder;
		String previousStyle = tag.getAttribute("style");
		if (previousStyle == null) {
			styleBuilder = new StringBuilder();
		} else {
			styleBuilder = new StringBuilder(previousStyle).append("; ");
		}
		
		// SPAN tags must be styled as inline-block to support width/height properly
		if (tag.getName().equalsIgnoreCase("span")) {
			styleBuilder.append("display: inline-block; ");
		}
		
		// modify the style attribute according to the sprite reference
		styleBuilder.append("background-image: url(").append(spriteReference.getAtlas().getUrl());
		styleBuilder.append("); background-position: -").append(spriteReference.getX());
		styleBuilder.append("px -").append(spriteReference.getY());
		styleBuilder.append("px; width: ").append(spriteReference.getWidth());
		styleBuilder.append("px; height: ").append(spriteReference.getHeight());
		styleBuilder.append("px; ");
		
		// store modified style
		tag.getAttributes().put("style", styleBuilder.toString());
		
	}
	
}
