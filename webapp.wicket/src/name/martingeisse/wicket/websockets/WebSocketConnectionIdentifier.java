/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.websockets;

import java.io.Serializable;

import name.martingeisse.common.util.ParameterUtil;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.ws.IWebSocketSettings;
import org.apache.wicket.protocol.ws.api.IWebSocketConnectionRegistry;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

/**
 * This class encapsulates the application/sessionId/pageId
 * tuple commonly identifying websocket connections in Wicket.
 * It supports equals/hashCode.
 * 
 * Note that if a page with a {@link WebSocketBehavior} is
 * rendered multiple times (especially when loading a page multiple
 * times in different browser tabs), then the same
 * {@link WebSocketConnectionIdentifier} will be used for all
 * of them. This reflects the fact that Wicket itself doesn't distinguish
 * between the "renderings", and only keeps an open websocket to the
 * last rendering, so the {@link WebSocketConnectionIdentifier} still
 * identifies only a single connection.
 */
public final class WebSocketConnectionIdentifier implements Serializable {

	/**
	 * the application
	 */
	private final Application application;

	/**
	 * the sessionId
	 */
	private final String sessionId;

	/**
	 * the pageId
	 */
	private final int pageId;

	/**
	 * Constructor.
	 * @param component the component (must be attached to a page)
	 */
	public WebSocketConnectionIdentifier(final Component component) {
		this(component.getPage());
	}
	
	/**
	 * Constructor.
	 * @param page the page
	 */
	public WebSocketConnectionIdentifier(final Page page) {
		this(page.getApplication(), page.getSession().getId(), page.getPageId());
		System.err.println("connecting for session id: " + page.getSession().getId());
	}
	
	/**
	 * Constructor.
	 * @param application the application
	 * @param sessionId the session ID
	 * @param pageId the page ID
	 */
	public WebSocketConnectionIdentifier(final Application application, final String sessionId, final int pageId) {
		this.application = ParameterUtil.ensureNotNull(application, "application");
		this.sessionId = ParameterUtil.ensureNotNull(sessionId, "sessionId");
		this.pageId = ParameterUtil.ensureNotNull(pageId, "pageId");
	}

	/**
	 * Getter method for the application.
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 * Getter method for the sessionId.
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Getter method for the pageId.
	 * @return the pageId
	 */
	public int getPageId() {
		return pageId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object untypedOther) {
		if (untypedOther instanceof WebSocketConnectionIdentifier) {
			WebSocketConnectionIdentifier other = (WebSocketConnectionIdentifier)untypedOther;
			return (other.application.equals(application) && other.sessionId.equals(sessionId) && other.pageId == pageId);
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(application).append(sessionId).append(pageId).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "{WebsocketConnectionIdentifier " + application + " / " + sessionId + " / " + pageId + "}";
	}
	
	/**
	 * Pushes the specified message through the websocket connection that is
	 * indicated by this connection identifier. This will cause event
	 * handling and possibly rendering in the connection's page.
	 * 
	 * @param message the message to push
	 */
	public void push(IWebSocketPushMessage message) {
		IWebSocketConnectionRegistry connectionRegistry = IWebSocketSettings.Holder.get(application).getConnectionRegistry();
		ConnectedMessage connectedMessage = new ConnectedMessage(application, sessionId, pageId);
		new WebSocketPushBroadcaster(connectionRegistry).broadcast(connectedMessage, message);
	}
	
}
