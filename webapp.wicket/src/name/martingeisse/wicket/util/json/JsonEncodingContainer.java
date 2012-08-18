/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util.json;

import name.martingeisse.common.javascript.JavascriptAssemblerUtil;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.Response;
import org.apache.wicket.response.StringResponse;

/**
 * This container renders its contents as usual, then encodes
 * the resulting text as a JSON string and writes that to
 * the response.
 */
public class JsonEncodingContainer extends WebMarkupContainer {

	/**
	 * the previousResponse
	 */
	private transient Response previousResponse;

	/**
	 * the fakeResponse
	 */
	private transient StringResponse fakeResponse;

	/**
	 * Constructor.
	 * @param id the wicket id
	 */
	public JsonEncodingContainer(String id) {
		super(id);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		previousResponse = getResponse();
		fakeResponse = new StringResponse();
		getRequestCycle().setResponse(fakeResponse);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.Component#onAfterRender()
	 */
	@Override
	protected void onAfterRender() {
		super.onAfterRender();
		previousResponse.write(JavascriptAssemblerUtil.formatStringLiteral(fakeResponse.toString()));
		getRequestCycle().setResponse(previousResponse);
		previousResponse = null;
		fakeResponse = null;
	}
	
	
}
