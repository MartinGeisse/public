/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.papyros.api;

import name.martingeisse.api.handler.IApiRequestHandler;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestHandlingFinishedException;
import name.martingeisse.api.request.ApiRequestMethod;
import name.martingeisse.api.request.ApiRequestPathChain;
import name.martingeisse.api.request.MissingRequestParameterException;
import name.martingeisse.slave_services.entity.Template;
import name.martingeisse.slave_services.papyros.backend.PapyrosDataUtil;
import name.martingeisse.slave_services.papyros.backend.RenderTemplateAction;

import org.json.simple.JSONValue;

/**
 * This request handler renders templates.
 */
public class RenderTemplateApiHandler implements IApiRequestHandler {

	/**
	 * Constructor.
	 */
	public RenderTemplateApiHandler() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IApiRequestHandler#handle(name.martingeisse.api.request.ApiRequestCycle, name.martingeisse.api.request.ApiRequestPathChain)
	 */
	@Override
	public void handle(final ApiRequestCycle requestCycle, final ApiRequestPathChain path) throws Exception {
		
		// parse request URI and load data
		if (path.isEmpty()) {
			throw new MissingRequestParameterException("template key");
		}
		ApiRequestPathChain subpath1 = path.getTail();
		if (subpath1.isEmpty()) {
			throw new MissingRequestParameterException("language key");
		}
		String templateKey = path.getHead();
		String languageKey = subpath1.getHead();
		final Template template;
		try {
			template = PapyrosDataUtil.loadTemplate(templateKey, languageKey);
		} catch (Exception e) {
			// TODO should be specific exception type so we know we may print the message to the client
			requestCycle.emitMessageResponse(400, e.getMessage());
			throw new ApiRequestHandlingFinishedException();
		}
		
		// parse the request body, containing the template data
		final String requestContentType = requestCycle.getRequest().getContentType();
		final Object previewData;
		if (requestCycle.getRequestMethod() != ApiRequestMethod.POST || requestContentType == null) {
			// TODO should be specific exception type so we know we may print the message to the client
			requestCycle.emitMessageResponse(400, "missing request content type (or not a POST request)");
			throw new ApiRequestHandlingFinishedException();
		} else if (requestContentType.equals("application/json") || requestContentType.equals("text/json")) {
			previewData = JSONValue.parse(requestCycle.getBodyAsReader());
		} else if (requestContentType.equals("application/x-www-form-urlencoded")) {
			previewData = JSONValue.parse(requestCycle.getParameters().getString("data", true));
		} else {
			// TODO should be specific exception type so we know we may print the message to the client
			requestCycle.emitMessageResponse(400, "invalid request content type: " + requestContentType);
			throw new ApiRequestHandlingFinishedException();
		}
		
		// fake rendering
		final RenderTemplateAction renderTemplateAction = new RenderTemplateAction();
		renderTemplateAction.setTemplate(template.getContent());
		renderTemplateAction.setData(previewData);
		final String renderedContent = renderTemplateAction.render();
		requestCycle.getResponse().setContentType("text/plain");
		requestCycle.getResponse().getWriter().write(renderedContent);
		
	}

}
