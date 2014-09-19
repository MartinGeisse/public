/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.slave_services.babel.api;

import java.util.List;

import name.martingeisse.api.handler.IApiRequestHandler;
import name.martingeisse.api.request.ApiRequestCycle;
import name.martingeisse.api.request.ApiRequestPathChain;
import name.martingeisse.api.request.MissingRequestParameterException;
import name.martingeisse.slave_services.babel.backend.BabelDataUtil;
import name.martingeisse.slave_services.entity.MessageFamily;
import name.martingeisse.slave_services.entity.MessageTranslation;

import org.apache.commons.lang3.tuple.Pair;

/**
 * This request handler generates a CakePHP .po file download.
 */
public class DownloadCakeTranslationFileApiHandler implements IApiRequestHandler {

	/**
	 * Constructor.
	 */
	public DownloadCakeTranslationFileApiHandler() {
	}

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IApiRequestHandler#handle(name.martingeisse.api.request.ApiRequestCycle, name.martingeisse.api.request.ApiRequestPathChain)
	 */
	@Override
	public void handle(final ApiRequestCycle requestCycle, final ApiRequestPathChain path) throws Exception {
		
		// parse request URI and load data
		if (path.isEmpty()) {
			throw new MissingRequestParameterException("domain");
		}
		ApiRequestPathChain subpath1 = path.getTail();
		if (subpath1.isEmpty()) {
			throw new MissingRequestParameterException("language key");
		}
		String domain = path.getHead();
		String languageKey = subpath1.getHead();
		// TODO what about missing ones?
		List<Pair<MessageFamily, MessageTranslation>> translationEntries = BabelDataUtil.loadDomainTranslations(domain, languageKey, false);
		
		// parse options
		if ("1".equals(requestCycle.getRequest().getParameter("download"))) {
			requestCycle.getResponse().addHeader("Content-Disposition", "attachment; filename=" + domain + ".po");
		}
		
		// build the response
		requestCycle.getResponse().addHeader("Content-Type", "text/plain");
		StringBuilder builder = new StringBuilder();
		for (Pair<MessageFamily, MessageTranslation> translationEntry : translationEntries) {
			// TODO quote escaping
			builder.append("msgid \"").append(translationEntry.getLeft().getMessageKey());
			builder.append("\"\nmsgstr \"").append(translationEntry.getRight().getText());
			builder.append("\"\n\n");
			requestCycle.getResponse().getWriter().print(builder);
			builder.setLength(0);
		}
		
	}

}
