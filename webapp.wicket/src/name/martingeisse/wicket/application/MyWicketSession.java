/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.application;

import name.martingeisse.common.util.ClassKeyedContainer;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

/**
 * This session adds a {@link ClassKeyedContainer} that stores global
 * data from application modules.
 */
public class MyWicketSession extends WebSession {

	/**
	 * the data
	 */
	private final ClassKeyedContainer<Object> data;

	/**
	 * Constructor.
	 * @param request the request used to initialize the session
	 */
	public MyWicketSession(Request request) {
		super(request);
		this.data = new ClassKeyedContainer<Object>();
	}
	
	/**
	 * Getter method for the data.
	 * @return the data
	 */
	public ClassKeyedContainer<Object> getData() {
		return data;
	}
	
	/**
	 * @return the session for the calling thread
	 */
	public static MyWicketSession get() {
		return (MyWicketSession)Session.get();
	}

}
