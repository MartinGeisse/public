/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.webide.resources.build;

import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;

/**
 * This message gets pushed by the builder thread
 * when something in one of the workspaces changes.
 */
public class ResourceChangePushMessage implements IWebSocketPushMessage {
}
