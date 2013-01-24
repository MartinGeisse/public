/**
 * Copyright (c) 2012 Shopgate GmbH
 */

package name.martingeisse.webide.plugin;



/**
 * An instance of this class remembers the caller of its constructor
 * and is used to protect plugin bundle state: The state of a plugin bundle
 * may only be accessed by that bundle, unkless explicitly permitted
 * by that bundle.
 * 
 * Specifically, to load or save plugin bundle state of a plugin bundle,
 * one needs an access token for that bundle. Only code from that bundle
 * can create such an access token: The token is applicable to the plugin
 * bundle that contains the class which called the token constructor.
 * 
 * Note that this restricts the way in which the token can be created by
 * convenience methods, since any such convenience method would be mistaken
 * for the caller.
 * 
 * A plugin bundle can give permission to access its state to other code by
 * passing the access token.
 */
public final class PluginBundleStateAccessToken {

	/**
	 * the mySecurityManager
	 */
	private static final MySecurityManager mySecurityManager = new MySecurityManager();
	
	/**
	 * the pluginBundleId
	 */
	private final long pluginBundleId;
	
	/**
	 * Constructor.
	 */
	public PluginBundleStateAccessToken() {
		Class<?> callerClass = mySecurityManager.getCallerClass();
		this.pluginBundleId = InternalPluginBundleClassLoaderRegistry.getPluginBundleId(callerClass.getClassLoader());
	}

	/**
	 * Getter method for the pluginBundleId.
	 * @return the pluginBundleId
	 */
	public long getPluginBundleId() {
		return pluginBundleId;
	}
	
	/**
	 * Helper class to obtain the caller class.
	 */
	static class MySecurityManager extends SecurityManager {
		
		/**
		 * Returns the class that called the token constructor.
		 */
		Class<?> getCallerClass() {
			// context[0] is this method
			// context[1] is the token constructor
			// context[2] is the actual caller
			return getClassContext()[2];
		}
		
	}

}
