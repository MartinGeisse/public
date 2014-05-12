/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.component.misc;

import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.AbstractLink;

/**
 * A link that uses the "referer" header field for the target URL,
 * so it links back to the referring page.
 */
public class RefererLink extends AbstractLink {

	/**
	 * the url
	 */
	private transient String url;
	
	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public RefererLink(final String id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.link.AbstractLink#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (!isLinkEnabled()) {
			url = null;
			setVisible(true);
		} else {
			Object containerRequest = getRequest().getContainerRequest();
			if (containerRequest instanceof HttpServletRequest) {
				url = ((HttpServletRequest)containerRequest).getHeader("Referer");
				setVisible(url != null);
			} else {
				url = null;
				setVisible(false);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.link.Link#onComponentTag(org.apache.wicket.markup.ComponentTag)
	 */
	@Override
	protected void onComponentTag(final ComponentTag tag) {
		super.onComponentTag(tag);
		if (url == null) {
			disableLink(tag);
		} else {
			if (tag.getName().equalsIgnoreCase("a") || tag.getName().equalsIgnoreCase("link") || tag.getName().equalsIgnoreCase("area")) {
				tag.put("href", url);
			} else {
				// the URL is client-provided, but passing "bad" values here only cause
				// the Javascript to go wild on the client and don't affect the server
				tag.put("onclick", "window.location.href='" + url + "';return false;");
			}
		}
	}

}
