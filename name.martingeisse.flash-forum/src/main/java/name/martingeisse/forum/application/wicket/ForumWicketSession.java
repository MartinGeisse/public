/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.forum.application.wicket;

import name.martingeisse.wicket.application.MyWicketSession;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * The session implementation.
 */
public class ForumWicketSession extends MyWicketSession {

	/**
	 * Getter method for the current session.
	 * @return the current session
	 */
	public static ForumWicketSession get() {
		return (ForumWicketSession)WebSession.get();
	}
	
	/**
	 * Constructor.
	 * @param request the request that creates this session
	 */
	public ForumWicketSession(Request request) {
		super(request);
	}
	
}