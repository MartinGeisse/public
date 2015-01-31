/**
 * Copyright (c) 2015 Martin Geisse
 */

package name.martingeisse.guiserver.configuration;

/**
 * Base implementation of {@link ConfigurationElement}.
 */
public abstract class AbstractConfigurationElement implements ConfigurationElement {

	/**
	 * the initialized
	 */
	private boolean initialized;

	/**
	 * the parentNamespace
	 */
	private ConfigurationNamespace parentNamespace;

	/**
	 * the key
	 */
	private String key;

	/**
	 * the cachedPath
	 */
	private String cachedPath;

	/**
	 * 
	 */
	void initializeRoot() {
		initialized = true;
		parentNamespace = null;
		key = null;
		cachedPath = "/";
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.ConfigurationElement#initialize(name.martingeisse.guiserver.configuration.ConfigurationNamespace, java.lang.String)
	 */
	@Override
	public void initialize(ConfigurationNamespace parentNamespace, String key) {
		if (parentNamespace == null) {
			throw new IllegalArgumentException("parentNamespace argument cannot be null");
		}
		if (key == null) {
			throw new IllegalArgumentException("key argument cannot be null");
		}
		if (initialized) {
			throw new IllegalStateException("cannot re-initialize a configuration element");
		}
		this.initialized = true;
		this.parentNamespace = parentNamespace;
		this.key = key;
		this.cachedPath = null;
	}

	/**
	 * 
	 */
	private void mustBeInitialized() {
		if (!initialized) {
			throw new IllegalStateException("this configuration element has not been initialized yet");
		}
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.ConfigurationElement#getParentNamespace()
	 */
	@Override
	public final ConfigurationNamespace getParentNamespace() {
		mustBeInitialized();
		return parentNamespace;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.ConfigurationElement#getKey()
	 */
	@Override
	public final String getKey() {
		mustBeInitialized();
		return key;
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.guiserver.configuration.ConfigurationElement#getPath()
	 */
	@Override
	public final String getPath() {
		mustBeInitialized();
		if (cachedPath == null) {
			String parentPath = parentNamespace.getPath();
			if (!parentPath.endsWith("/")) {
				parentPath += '/';
			}
			cachedPath = parentPath + key;
		}
		return cachedPath;
	}

}
