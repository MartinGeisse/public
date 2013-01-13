/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.contextmenu;

import name.martingeisse.wicket.util.AjaxRequestUtil;

/**
 * A menu item that causes a file to be downloaded.
 * 
 * @param <A> the anchor type
 */
public abstract class DownloadMenuItem<A> extends SimpleContextMenuItem<A> {

	/**
	 * Constructor.
	 */
	public DownloadMenuItem() {
		super();
	}

	/**
	 * Constructor.
	 * @param name the displayed name of this menu item
	 */
	public DownloadMenuItem(final String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.webide.workbench.components.contextmenu.SimpleContextMenuItem#onSelect(java.lang.Object)
	 */
	@Override
	protected void onSelect(final A anchor) {
		String url = determineUrl(anchor);
		if (url != null) {
			AjaxRequestUtil.getAjaxRequestTarget().appendJavaScript("window.location.href = '" + url + "';");
		}
	}

	/**
	 * This method determines the URL from which to download. Note that the URL must
	 * return the Content-Disposition header to trigger a background download --
	 * HTTP demands that the referred resource says it'S a download.
	 * 
	 * @return the URL
	 */
	protected abstract String determineUrl(final A anchor);

}
