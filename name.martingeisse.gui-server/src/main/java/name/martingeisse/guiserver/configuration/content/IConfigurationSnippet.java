/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.guiserver.configuration.content;

/**
 * Instances of this interface are configuration-related objects that
 * are assigned a globally unique handle which can later be used
 * to retrieve them from the configuration, without knowing where they
 * are stored exactly.
 * 
 * Such handles are used to allow Wicket components to refer to the
 * snippets that define them without them being stateful components
 * nor traversing the whole configuration just to find the defining
 * snippet.
 * 
 * This interface doesn't define the actual methods needed to do
 * something with the snippets -- it only defines methods for
 * automated parser definition. The concrete type used by a snippet
 * and its components is different each time, and the snippets get
 * cast to that type. This interface only ensures that related code
 * doesn't confuse the snippets with other objects. 
 */
public interface IConfigurationSnippet {

	/**
	 * Notifies this snippet about the handle assigned to it.
	 * @param handle the handle
	 */
	public void setSnippetHandle(int handle);
	
}
