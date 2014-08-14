/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.frontend.components;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Shows a tabbed panel that uses a page parameter to select the
 * "active" tab, and generates {@link BookmarkablePageLink}s
 * to the current page (i.e. page class and parameters) with just
 * the tab parameter replaced.
 * 
 * Subclasses must provide the list of tabs (label and parameter
 * value) as well as a method to generate the tab contents based
 * on the current parameter value.
 * 
 * This class uses Bootstrap styles. Subclasses can override the styles.
 * 
 * This class is useful because:
 * 
 * - Wicket's {@link TabbedPanel} and {@link AjaxTabbedPanel} use stateful
 *   pages, which is dangerous if the page state can contain stale data
 *   (an indication that such state should be moved from page state to models,
 *   so using this class in such a situation is a dirty quick fix).
 * 
 * - a pure Javascript+AJAX tabbed panel would forget its current tab on
 *   reloading the page
 *   
 * - a pure Javascript (non-AJAX) tabbed panel would also forget its state AND
 *   would have to render all tab contents up-front.
 * 
 */
public class PageParameterDrivenTabPanel extends Panel {

}
