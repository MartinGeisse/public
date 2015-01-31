/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

/**
 * A simple content element that shows a fixed HTML snippet.
 */
public final class HtmlContentConfiguration implements ContentElementConfiguration {

	/**
	 * the html
	 */
	private final String html;

	/**
	 * Constructor.
	 * @param html the HTML snippet
	 */
	public HtmlContentConfiguration(String html) {
		this.html = html;
	}

	/**
	 * Getter method for the html.
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}

}
