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
	 * Constructor.
	 * @param id the wicket id
	 */
	public JsonEncodingContainer(String id) {
		super(id);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.MarkupContainer#onRender()
	 */
	@Override
	protected void onRender() {
		Response previousResponse = getResponse();
		StringResponse fakeResponse = new StringResponse();
		getRequestCycle().setResponse(fakeResponse);
		super.onRender();
		previousResponse.write(JavascriptAssemblerUtil.formatStringLiteral(fakeResponse.toString()));
		getRequestCycle().setResponse(previousResponse);
	}
	
}
