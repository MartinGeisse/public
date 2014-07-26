/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.application;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.request.Request;
import org.apache.wicket.session.ISessionStore;

/**
 * Base class for session store implementations.
 */
public abstract class AbstractSessionStore implements ISessionStore {

	/**
	 * the SESSION_ID_COOKIE_NAME
	 */
	public static final String SESSION_ID_COOKIE_NAME = "sessionId";
	
	/**
	 * the SESSION_COOKIE_OVERRIDE_ATTRIBUTE_NAME
	 */
	public static final String SESSION_COOKIE_OVERRIDE_ATTRIBUTE_NAME = "sessionCookieOverride";

	/**
	 * the bindListeners
	 */
	private final Set<BindListener> bindListeners = new CopyOnWriteArraySet<BindListener>();

	/**
	 * the unboundListeners
	 */
	private final Set<UnboundListener> unboundListeners = new CopyOnWriteArraySet<UnboundListener>();

	/**
	 * Constructor.
	 */
	public AbstractSessionStore() {
	}

	/**
	 * Returns the servlet request.
	 */
	protected final HttpServletRequest getHttpServletRequest(final Request request) {
		Object containerRequest = request.getContainerRequest();
		if (containerRequest instanceof HttpServletRequest) {
			return (HttpServletRequest)containerRequest;
		} else {
			throw new IllegalArgumentException("This session store can only be used with an HttpServletRequest as the container request");
		}
	}

	/**
	 * Returns the session ID that was sent as part of a request, or null if not found.
	 */
	protected final String getSessionIdFromRequest(final Request request) {
		HttpServletRequest httpRequest = getHttpServletRequest(request);
		Object sessionCookieOverride = httpRequest.getAttribute(SESSION_COOKIE_OVERRIDE_ATTRIBUTE_NAME);
		if (sessionCookieOverride != null) {
			return sessionCookieOverride.toString();
		}
		Cookie[] cookies = httpRequest.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(SESSION_ID_COOKIE_NAME)) {
				return cookie.getValue();
			}
		}
		return null;
	}
	
	/**
	 * Sets a fake session cookie that gets returned by {@link #getSessionIdFromRequest(Request)}
	 * instead of the real session cookie.
	 */
	protected final void overrideSessionCookie(final Request request, final String value) {
		getHttpServletRequest(request).setAttribute(SESSION_COOKIE_OVERRIDE_ATTRIBUTE_NAME, value);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#registerBindListener(org.apache.wicket.session.ISessionStore.BindListener)
	 */
	@Override
	public final void registerBindListener(BindListener listener) {
		bindListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#unregisterBindListener(org.apache.wicket.session.ISessionStore.BindListener)
	 */
	@Override
	public final void unregisterBindListener(BindListener listener) {
		bindListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#getBindListeners()
	 */
	@Override
	public final Set<BindListener> getBindListeners() {
		return Collections.unmodifiableSet(bindListeners);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#registerUnboundListener(org.apache.wicket.session.ISessionStore.UnboundListener)
	 */
	@Override
	public final void registerUnboundListener(final UnboundListener listener) {
		unboundListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#unregisterUnboundListener(org.apache.wicket.session.ISessionStore.UnboundListener)
	 */
	@Override
	public final void unregisterUnboundListener(final UnboundListener listener) {
		unboundListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#getUnboundListener()
	 */
	@Override
	public final Set<UnboundListener> getUnboundListener() {
		return Collections.unmodifiableSet(unboundListeners);
	}

}
