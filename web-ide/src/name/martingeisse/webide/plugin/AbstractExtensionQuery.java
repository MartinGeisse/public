/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;


/**
 * This is the common base class for {@link UserExtensionQuery}
 * and {@link WorkspaceExtensionQuery}.
 */
public abstract class AbstractExtensionQuery {

	/**
	 * the extensionPointName
	 */
	private final String extensionPointName;

	/**
	 * Constructor.
	 * @param extensionPointName the name of the extension point
	 */
	public AbstractExtensionQuery(final String extensionPointName) {
		this.extensionPointName = extensionPointName;
	}

	/**
	 * Getter method for the extensionPointName.
	 * @return the extensionPointName
	 */
	public String getExtensionPointName() {
		return extensionPointName;
	}

	/**
	 * Represents a single extension that was found by an extension query.
	 */
	public static final class Result {

		/**
		 * the descriptor
		 */
		private final Object descriptor;

		/**
		 * the pluginBundleId
		 */
		private final long pluginBundleId;

		/**
		 * Constructor.
		 */
		Result(final Object descriptor, final long pluginBundleId) {
			this.descriptor = descriptor;
			this.pluginBundleId = pluginBundleId;
		}
		
		/**
		 * Getter method for the descriptor.
		 * @return the descriptor
		 */
		public Object getDescriptor() {
			return descriptor;
		}

		/**
		 * Getter method for the pluginBundleId.
		 * @return the pluginBundleId
		 */
		public long getPluginBundleId() {
			return pluginBundleId;
		}

	}

}
