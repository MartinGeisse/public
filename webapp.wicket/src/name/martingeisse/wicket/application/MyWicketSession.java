/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.application;

import java.util.Random;

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
	 * the dataContainer
	 */
	private final ClassKeyedContainer<Object> dataContainer;

	/**
	 * the pageId
	 */
	private int pageId;

	/**
	 * Constructor.
	 * @param request the request used to initialize the session
	 */
	public MyWicketSession(Request request) {
		super(request);
		this.dataContainer = new ClassKeyedContainer<Object>();
		this.pageId = (new Random().nextInt() & 0xffffff);
	}
	
	/**
	 * Getter method for the dataContainer.
	 * @return the dataContainer
	 */
	public ClassKeyedContainer<Object> getDataContainer() {
		return dataContainer;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.Session#nextPageId()
	 */
	@Override
	public synchronized int nextPageId() {
		return pageId++;
	}
	
	/**
	 * @return the session for the calling thread
	 */
	public static MyWicketSession get() {
		return (MyWicketSession)Session.get();
	}

	/**
	 * This method provides quick static access to the data container of the
	 * current session.
	 * @return the dataContainer of the current session
	 */
	public static ClassKeyedContainer<Object> getData() {
		return get().getDataContainer();
	}
	
}
