/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps the class loaders of all loaded plugin bundles.
 * 
 * It should be considered internal to the plugin system.
 */
public class InternalPluginBundleClassLoaderRegistry {

	/**
	 * the classLoaderByBundleId
	 */
	private static final Map<Long, ClassLoader> classLoaderByBundleId = new HashMap<Long, ClassLoader>();

	/**
	 * the bundleIdByClassLoader
	 */
	private static final Map<ClassLoader, Long> bundleIdByClassLoader = new HashMap<ClassLoader, Long>();
	
	/**
	 * Static initializer: Associates plugin bundle ID 1 with the application class loader.
	 */
	static {
		ClassLoader classLoader = InternalPluginBundleClassLoaderRegistry.class.getClassLoader();
		classLoaderByBundleId.put(1L, classLoader);
		bundleIdByClassLoader.put(classLoader, 1L);
	}

	/**
	 * Prevent instantiation.
	 */
	private InternalPluginBundleClassLoaderRegistry() {
	}

	/**
	 * Registers a class loader.
	 */
	static synchronized void register(long pluginBundleId, ClassLoader classLoader) {
		classLoaderByBundleId.put(pluginBundleId, classLoader);
		bundleIdByClassLoader.put(classLoader, pluginBundleId);
	}

	/**
	 * Obtains the class loader for the plugin bundle with the specified id.
	 * Returns null if no class loader is registered for that id.
	 * @param pluginBundleId the plugin bundle id
	 * @return the class loader or null
	 */
	public static synchronized ClassLoader getRegisteredClassLoader(long pluginBundleId) {
		return classLoaderByBundleId.get(pluginBundleId);
	}

	/**
	 * Obtains the class loader for the plugin bundle with the specified id.
	 * Loads the plugin bundle and creates a class loader if necessary.
	 * @param pluginBundleId the plugin bundle id
	 * @return the class loader
	 */
	public static synchronized ClassLoader getOrCreateClassLoader(long pluginBundleId) {
		ClassLoader classLoader = classLoaderByBundleId.get(pluginBundleId);
		if (classLoader != null) {
			return classLoader;
		}
		try {
			final URL url = new URL("bundle", null, Long.toString(pluginBundleId));
			classLoader = new URLClassLoader(new URL[] {url});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		register(pluginBundleId, classLoader);
		return classLoader;
	}
	
	/**
	 * Obtains the plugin bundle id for the plugin bundle with the specified class loader.
	 * Returns null if the class loader is not registered
	 * @param classLoader the class loader
	 * @return the plugin bundle id or null
	 */
	public static synchronized Long getPluginBundleId(ClassLoader classLoader) {
		return bundleIdByClassLoader.get(classLoader);
	}
	
}
