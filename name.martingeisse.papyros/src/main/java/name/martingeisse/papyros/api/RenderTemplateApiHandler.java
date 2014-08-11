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
import name.martingeisse.papyros.backend.RenderTemplateAction;
import name.martingeisse.papyros.entity.PreviewDataSet;
import name.martingeisse.papyros.entity.Template;

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
		
		// parse request and load data
		if (path.isEmpty()) {
			throw new MissingRequestParameterException("template key");
		}
		ApiRequestPathChain subpath1 = path.getTail();
		if (subpath1.isEmpty()) {
			throw new MissingRequestParameterException("language key");
		}
		ApiRequestPathChain subpath2 = subpath1.getTail();
		String templateKey = path.getHead();
		String languageKey = subpath1.getHead();
		int previewDataSetNumber = (subpath2.isEmpty() ? 0 : Integer.parseInt(subpath2.getHead()));
		final Template template;
		try {
			template = PapyrosDataUtil.loadTemplate(templateKey, languageKey);
		} catch (Exception e) {
			// TODO should be specific exception type so we know we may print the message to the client
			requestCycle.emitMessageResponse(400, e.getMessage());
			throw new ApiRequestHandlingFinishedException();
		}
		final PreviewDataSet previewDataSet = PapyrosDataUtil.loadPreviewDataSet(template.getTemplateFamilyId(), previewDataSetNumber);
		final Object previewData = (previewDataSet == null ? null : JSONValue.parse(previewDataSet.getData()));
		
		// fake rendering
		final RenderTemplateAction renderTemplateAction = new RenderTemplateAction();
		renderTemplateAction.setTemplate(template.getContent());
		renderTemplateAction.setData(previewData);
		final String renderedContent = renderTemplateAction.render();
		requestCycle.getResponse().setContentType("text/plain");
		requestCycle.getResponse().getWriter().write(renderedContent);
		
	}

}
