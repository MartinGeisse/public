/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.admin.pages.border;


import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;

/**
 * Combines two nested {@link WebMarkupContainer} components -- typically {@link Border}s --
 * into a single one. This is necessary when two nested borders shall be added, yet the
 * markup expects only one.
 */
public class DoublePageBorder extends Border {

	/**
	 * Constructor.
	 * @param outer the outer border. Must have been created with {@link PageBorderUtil#PAGE_BORDER_ID}.
	 * @param inner the inner border. Must have been created with {@link PageBorderUtil#PAGE_BORDER_ID}.
	 */
	public DoublePageBorder(WebMarkupContainer outer, WebMarkupContainer inner) {
		super(PageBorderUtil.PAGE_BORDER_ID);
		addToBorder(outer);
		outer.add(inner);
	}

	/**
	 * Combines the specified borders into a single one, with the border at index 0
	 * being the outmost one. All borders must have been created with
	 * {@link PageBorderUtil#PAGE_BORDER_ID}.
	 * @param borders the borders to combine
	 * @return the combined border
	 */
	public static WebMarkupContainer combineBorders(WebMarkupContainer[] borders) {
		if (borders.length == 0) {
			return new WebMarkupContainer(PageBorderUtil.PAGE_BORDER_ID);
		} else if (borders.length == 1) {
			return borders[0];
		} else {
			return combineBorders(borders, borders.length);
		}
	}

	/**
	 * Helper method for combineBorders(borders).
	 */
	private static WebMarkupContainer combineBorders(WebMarkupContainer[] borders, int length) {
		if (length == 2) {
			return new DoublePageBorder(borders[0], borders[1]);
		} else {
			return new DoublePageBorder(combineBorders(borders, length - 1), borders[length - 1]);
		}
	}

}
