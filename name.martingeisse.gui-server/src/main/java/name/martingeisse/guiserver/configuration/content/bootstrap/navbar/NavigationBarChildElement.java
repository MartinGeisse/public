/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content.bootstrap.navbar;

import name.martingeisse.guiserver.configuration.content.basic.LinkConfiguration;

/**
 * Represents the child elements for a navigation bar. These objects are created
 * by the XML databinding and passed to the {@link NavigationBarConfiguration}
 * constructor.
 */
public abstract class NavigationBarChildElement {

	/**
	 * Constructor.
	 */
	private NavigationBarChildElement() {
	}

	/**
	 * Represents the brand link.
	 */
	public static final class BrandLink extends NavigationBarChildElement {

		/**
		 * the linkConfiguration
		 */
		private final LinkConfiguration linkConfiguration;

		/**
		 * Constructor.
		 * @param linkConfiguration the link configuration
		 */
		public BrandLink(LinkConfiguration linkConfiguration) {
			this.linkConfiguration = linkConfiguration;
		}

		/**
		 * Getter method for the linkConfiguration.
		 * @return the linkConfiguration
		 */
		public LinkConfiguration getLinkConfiguration() {
			return linkConfiguration;
		}
		
	}

	/**
	 * Represents a navigation link.
	 */
	public static final class NavigationLink extends NavigationBarChildElement {

		/**
		 * the linkConfiguration
		 */
		private final LinkConfiguration linkConfiguration;

		/**
		 * Constructor.
		 * @param linkConfiguration the link configuration
		 */
		public NavigationLink(LinkConfiguration linkConfiguration) {
			this.linkConfiguration = linkConfiguration;
		}

		/**
		 * Getter method for the linkConfiguration.
		 * @return the linkConfiguration
		 */
		public LinkConfiguration getLinkConfiguration() {
			return linkConfiguration;
		}
		
	}

}
