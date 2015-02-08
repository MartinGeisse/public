/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.guiserver.application.wicket;

import name.martingeisse.wicket.application.MyWicketSession;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * The session implementation.
 */
public class GuiWicketSession extends MyWicketSession {

	/**
	 * Getter method for the current session.
	 * @return the current session
	 */
	public static GuiWicketSession get() {
		return (GuiWicketSession)WebSession.get();
	}
	
	/**
	 * Constructor.
	 * @param request the request that creates this session
	 */
	public GuiWicketSession(Request request) {
		super(request);
	}
	
}
