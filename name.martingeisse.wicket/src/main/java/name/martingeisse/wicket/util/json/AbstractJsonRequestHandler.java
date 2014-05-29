/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.wicket.util.json;

import javax.servlet.http.HttpServletResponse;
import name.martingeisse.common.javascript.JavascriptAssembler;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;

/**
 * Abstract superclass for resources that query data and return it as JSON objects.
 * Resources of this type are by default non-cacheable.
 */
public abstract class AbstractJsonRequestHandler implements IRequestHandler {

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestHandler#respond(org.apache.wicket.request.IRequestCycle)
	 */
	@Override
	public void respond(IRequestCycle requestCycle) {
		WebRequest request = (WebRequest)requestCycle.getRequest();
		WebResponse response = (WebResponse)requestCycle.getResponse();
		
		JavascriptAssembler assembler = new JavascriptAssembler();
		generateJson(assembler, request);

		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setContentType(getContentType());
		((HttpServletResponse)response.getContainerResponse()).setCharacterEncoding("utf-8");
		response.write(assembler.getAssembledCode());
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.request.IRequestHandler#detach(org.apache.wicket.request.IRequestCycle)
	 */
	@Override
	public void detach(IRequestCycle requestCycle) {
	}

	/**
	 * @return the content type to use
	 */
	protected String getContentType() {
		return "application/json; ";
	}
	
	/**
	 * Generates the resulting JSON.
	 * @param request the request used to generate JSON
	 */
	protected abstract void generateJson(JavascriptAssembler assembler, WebRequest request);

}
