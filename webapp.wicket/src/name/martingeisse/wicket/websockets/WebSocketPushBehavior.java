/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.websockets;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.protocol.ws.api.WebSocketBehavior;
import org.apache.wicket.protocol.ws.api.WebSocketRequestHandler;
import org.apache.wicket.protocol.ws.api.event.WebSocketPushPayload;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

/**
 * Use this behavior on a page or component instead of {@link WebSocketBehavior}
 * to support push-based event handling and rendering.
 */
public class WebSocketPushBehavior extends WebSocketBehavior {
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.protocol.ws.api.WebSocketBehavior#onEvent(org.apache.wicket.Component, org.apache.wicket.event.IEvent)
	 */
	@Override
	public void onEvent(final Component component, final IEvent<?> event) {
		final Object payload = event.getPayload();
		if (payload instanceof WebSocketPushPayload) {
			final WebSocketPushPayload pushPayload = (WebSocketPushPayload)payload;
			final WebSocketRequestHandler webSocketRequestHandler = pushPayload.getHandler();
			final IWebSocketPushMessage pushMessage = pushPayload.getMessage();
			onPush(webSocketRequestHandler, pushMessage);
		} else {
			super.onEvent(component, event);
		}
	}
	
	/**
	 * This method is invoked after pushing a message.
	 * 
	 * @param webSocketRequestHandler the request handler (implements {@link AjaxRequestTarget})
	 * @param pushMessage the pushed message
	 */
	protected void onPush(final WebSocketRequestHandler webSocketRequestHandler, final IWebSocketPushMessage pushMessage) {
	}

}
