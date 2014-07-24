/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.wicket.Session;
import org.apache.wicket.request.Request;

/**
 * Stores Wicket sessions to a Cassandra database.
 */
public final class CassandraSessionStore extends AbstractSessionStore {

	private final com.datastax.driver.core.Session cassandraSession;
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#getAttribute(org.apache.wicket.request.Request, java.lang.String)
	 */
	@Override
	public Serializable getAttribute(Request request, String name) {
		String sessionId = getSessionIdFromRequest(request);
		if (sessionId == null) {
			return null;
		}
		// TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#getAttributeNames(org.apache.wicket.request.Request)
	 */
	@Override
	public List<String> getAttributeNames(Request request) {
		String sessionId = getSessionIdFromRequest(request);
		if (sessionId == null) {
			return new ArrayList<>();
		}
		// TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#setAttribute(org.apache.wicket.request.Request, java.lang.String, java.io.Serializable)
	 */
	@Override
	public void setAttribute(Request request, String name, Serializable value) {
		String sessionId = getSessionIdFromRequest(request);
		if (sessionId == null) {
			return;
		}
		// TODO
		// if no session exists, this call should not create one but do nothing
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#removeAttribute(org.apache.wicket.request.Request, java.lang.String)
	 */
	@Override
	public void removeAttribute(Request request, String name) {
		String sessionId = getSessionIdFromRequest(request);
		if (sessionId == null) {
			return;
		}
		// TODO
		// if no session exists, this call should not create one but do nothing
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#invalidate(org.apache.wicket.request.Request)
	 */
	@Override
	public void invalidate(Request request) {
		String sessionId = getSessionIdFromRequest(request);
		if (sessionId == null) {
			return;
		}
		// TODO
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#getSessionId(org.apache.wicket.request.Request, boolean)
	 */
	@Override
	public String getSessionId(Request request, boolean create) {
		// TODO
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#lookup(org.apache.wicket.request.Request)
	 */
	@Override
	public Session lookup(Request request) {
		// TODO
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#bind(org.apache.wicket.request.Request, org.apache.wicket.Session)
	 */
	@Override
	public void bind(Request request, Session newSession) {
		// TODO
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#flushSession(org.apache.wicket.request.Request, org.apache.wicket.Session)
	 */
	@Override
	public void flushSession(Request request, Session session) {
		// TODO
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#destroy()
	 */
	@Override
	public void destroy() {
	}

}
