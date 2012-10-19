/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import java.util.List;

import name.martingeisse.api.handler.IRequestHandler;
import name.martingeisse.api.request.RequestCycle;
import name.martingeisse.api.request.RequestPathChain;
import name.martingeisse.apidemo.phorum.PhorumSearch;
import name.martingeisse.apidemo.phorum.QPhorumSearch;
import name.martingeisse.common.database.EntityConnectionManager;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import com.mysema.query.sql.SQLQuery;

/**
 *
 */
public class TimeTestHandler implements IRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {
		SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		QPhorumSearch qSearch = QPhorumSearch.phorumSearch;
		List<PhorumSearch> results = query.from(qSearch).list(qSearch);
		PhorumSearch entry = results.get(0);
		requestCycle.preparePlainTextResponse();
		requestCycle.getWriter().println("* " + entry.getDatetimeTest().getClass());
		requestCycle.getWriter().println("* " + DateTimeFormat.fullDateTime().print(entry.getDatetimeTest()));
		requestCycle.getWriter().println("* " + DateTimeFormat.fullDateTime().withZone(DateTimeZone.forID("Europe/Berlin")).print(entry.getDatetimeTest()));
		requestCycle.getWriter().println("* " + DateTimeFormat.fullDateTime().withZone(DateTimeZone.forID("Asia/Aden")).print(entry.getDatetimeTest()));
		requestCycle.getWriter().println("* " + DateTimeFormat.fullDateTime().withZone(DateTimeZone.forID("Europe/Moscow")).print(entry.getDatetimeTest()));
	}
	
}
