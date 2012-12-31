/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.plugin;

import java.io.Serializable;

/**
 * This class represents a plugin bundle. It is only a "handle" in the
 * sense that multiple instances may refer to the same plugin bundle.
 * 
 * The main purpose of this class is to allow plugins to obtain classes
 * from other bundles.
 */
public final class PluginBundleHandle implements Serializable {

	/**
	 * the pluginBundleId
	 */
	private long pluginBundleId;
	
	/**
	 * the classLoader
	 */
	private transient ClassLoader classLoader;
	
	/**
	 * Constructor.
	 */
	PluginBundleHandle(long pluginBundleId) {
		this.pluginBundleId = pluginBundleId;
	}
	
	/**
	 * Getter method for the classLoader.
	 * @return the classLoader
	 */
	public ClassLoader getClassLoader() {
		if (classLoader == null) {
			classLoader = InternalPluginUtil.getPluginBundleClassLoader(pluginBundleId);
		}
		return classLoader;
	}

	/**
	 * Loads a class from this bundle.
	 * @param name the fully qualified class name
	 * @return the class
	 * @throws ClassNotFoundException if the class could not be found 
	 */
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return getClassLoader().loadClass(name);
	}

	/**
	 * Convenience method to load a class from this bundle, then create an instance using
	 * the no-argument constructor.
	 * 
	 * @param className the name of the class
	 * @return the instance
	 * @throws ClassNotFoundException if the class could not be found 
	 * @throws InstantiationException if the class cannot be instantiated (e.g. because it is abstract)
	 * @throws IllegalAccessException if the constructor is not accessible
	 */
	public Object createObject(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return loadClass(className).newInstance();
	}


	/**
	 * Convenience method to load a class from this bundle, then create an instance using
	 * the no-argument constructor. The class is expected to be a subtype of T, and the
	 * instance is returned as an instance of T.
	 * 
	 * @param supertype the expected supertype of the class to load
	 * @param className the name of the class
	 * @return the instance
	 * @throws ClassNotFoundException if the class could not be found 
	 * @throws InstantiationException if the class cannot be instantiated (e.g. because it is abstract)
	 * @throws IllegalAccessException if the constructor is not accessible
	 * @throws ClassCastException if the class is not a subtype of T
	 */
	public <T> T createObject(Class<T> supertype, String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ClassCastException {
		return supertype.cast(loadClass(className).newInstance());
	}
	
}
