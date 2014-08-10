/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.papyros.api;

import name.martingeisse.api.handler.IApiRequestHandler;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestHandlingFinishedException;
import name.martingeisse.api.request.ApiRequestPathChain;
import name.martingeisse.api.request.MissingRequestParameterException;
import name.martingeisse.papyros.backend.PapyrosDataUtil;
import name.martingeisse.papyros.entity.Template;

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
		
		// fake rendering
		final String renderedContent = template.getContent();
		requestCycle.getResponse().setContentType("text/plain");
		requestCycle.getResponse().getWriter().write(renderedContent);
		
	}

}
