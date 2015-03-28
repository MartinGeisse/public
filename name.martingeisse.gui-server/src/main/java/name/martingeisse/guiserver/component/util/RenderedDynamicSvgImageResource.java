/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.component.util;

import java.nio.charset.StandardCharsets;

import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.jfree.graphics2d.svg.SVGGraphics2D;

/**
 * Specialization of {@link RenderedDynamicImageResource} that outputs SVG images.
 */
public abstract class RenderedDynamicSvgImageResource extends RenderedDynamicImageResource {

	/**
	 * Constructor.
	 * @param width the width
	 * @param height the height
	 */
	public RenderedDynamicSvgImageResource(int width, int height) {
		super(width, height, "svg+xml");
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource#render(org.apache.wicket.request.resource.IResource.Attributes)
	 */
	@Override
	protected byte[] render(Attributes attributes) {
		while (true) {
			SVGGraphics2D svgGenerator = new SVGGraphics2D(getWidth(), getHeight());
			render(svgGenerator, attributes);
			String svgElement = svgGenerator.getSVGElement();
			System.out.println(svgElement);
			return svgElement.getBytes(StandardCharsets.UTF_8);
		}

	}

}
