/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util;

import org.apache.wicket.IRequestListener;
import org.apache.wicket.RequestListenerInterface;

/**
 * A simple ad-hoc request listener interface to be used for various purposes.
 * This listener will not automatically render the page on listener requests. 
 */
public interface ISimpleCallbackListener extends IRequestListener {

	/** 
	 * Wicket listener registry entry
	 */
	public static final RequestListenerInterface INTERFACE = new RequestListenerInterface(ISimpleCallbackListener.class).setRenderPageAfterInvocation(false);

	/**
	 * Called when the client code invokes the callback.
	 */
	public void onSimpleCallback();
	
}
