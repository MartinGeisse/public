/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.protocol.ws.jetty9;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.apache.wicket.protocol.ws.api.AbstractWebSocketConnection;
import org.apache.wicket.protocol.ws.api.AbstractWebSocketProcessor;
import org.apache.wicket.protocol.ws.api.IWebSocketConnection;
import org.apache.wicket.util.lang.Args;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A wrapper around Jetty9's native WebSocketConnection.
 *
 * @since 6.2
 */
public class Jetty9WebSocketConnection extends AbstractWebSocketConnection
{
	private static final Logger LOG = LoggerFactory.getLogger(Jetty9WebSocketConnection.class);

	private final Session session;

	/**
	 * Constructor.
	 *
	 * @param session
	 *            the jetty websocket connection
	 */
	public Jetty9WebSocketConnection(Session session, AbstractWebSocketProcessor webSocketProcessor)
	{
		super(webSocketProcessor);
		this.session = Args.notNull(session, "connection");
	}

	@Override
	public boolean isOpen()
	{
		return session.isOpen();
	}

	@Override
	public void close(int code, String reason)
	{
		if (isOpen())
		{
			try
			{
				session.close(code, reason);
			} catch (IOException iox)
			{
				LOG.error("An error occurred while closing WebSocket session", iox);
			}
		}
	}

	@Override
	public IWebSocketConnection sendMessage(String message) throws IOException
	{
		checkClosed();

		session.getRemote().sendString(message);
		return this;
	}

	@Override
	public IWebSocketConnection sendMessage(byte[] message, int offset, int length)
		throws IOException
	{
		checkClosed();

		ByteBuffer buf = ByteBuffer.wrap(message, offset, length);
		session.getRemote().sendBytes(buf);
		return this;
	}

	private void checkClosed()
	{
		if (!isOpen())
		{
			throw new IllegalStateException("The connection is closed.");
		}
	}
}
