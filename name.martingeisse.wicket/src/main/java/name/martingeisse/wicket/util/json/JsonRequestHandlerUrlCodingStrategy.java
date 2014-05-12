/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util.json;

import org.apache.log4j.Logger;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;

/**
 * This mapper creates an instance of the specified {@link AbstractJsonRequestHandler}
 * class and uses it as the request handler.
 */
public class JsonRequestHandlerUrlCodingStrategy implements IRequestMapper {

	/**
	 * the logger
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(JsonRequestHandlerUrlCodingStrategy.class);
	
	/**
	 * the requestHandlerClass
	 */
	private final Class<? extends AbstractJsonRequestHandler> requestHandlerClass;
	
	/**
	 * Constructor.
	 * @param requestHandlerClass the request handler class
	 */
	public JsonRequestHandlerUrlCodingStrategy(Class<? extends AbstractJsonRequestHandler> requestHandlerClass) {
		this.requestHandlerClass = requestHandlerClass;
	}

	/**
	 * Getter method for the requestHandlerClass.
	 * @return the requestHandlerClass
	 */
	public Class<? extends AbstractJsonRequestHandler> getRequestHandlerClass() {
		return requestHandlerClass;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#mapRequest(org.apache.wicket.request.Request)
	 */
	@Override
	public IRequestHandler mapRequest(Request request) {
		try {
			return requestHandlerClass.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#getCompatibilityScore(org.apache.wicket.request.Request)
	 */
	@Override
	public int getCompatibilityScore(Request request) {
		return 1;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestMapper#mapHandler(org.apache.wicket.request.IRequestHandler)
	 */
	@Override
	public Url mapHandler(IRequestHandler requestHandler) {
		return null;
	}

}
