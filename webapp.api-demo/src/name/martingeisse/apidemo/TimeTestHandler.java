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
import name.martingeisse.common.database.JdbcEntityDatabaseConnection;
import name.martingeisse.common.database.config.CustomMysqlQuerydslConfiguration;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

import com.mysema.query.sql.MySQLTemplates;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.SQLQueryImpl;

/**
 *
 */
public class TimeTestHandler implements IRequestHandler {

	/* (non-Javadoc)
	 * @see name.martingeisse.api.handler.IRequestHandler#handle(name.martingeisse.api.request.RequestCycle, name.martingeisse.api.request.RequestPathChain)
	 */
	@Override
	public void handle(RequestCycle requestCycle, RequestPathChain path) throws Exception {

		// TODO: does not work yet (does not use configuration timezone)
		// but at least uses Joda classes
		
		JdbcEntityDatabaseConnection entityConnection = (JdbcEntityDatabaseConnection)EntityConnectionManager.getConnection();
		SQLQuery query = new SQLQueryImpl(entityConnection.getJdbcConnection(), new CustomMysqlQuerydslConfiguration(new MySQLTemplates(), DateTimeZone.forID("Europe/Moscow")));
		
		QPhorumSearch qSearch = QPhorumSearch.phorumSearch;
		List<PhorumSearch> results = query.from(qSearch).list(qSearch);
		PhorumSearch entry = results.get(0);
		System.out.println("* " + entry.getDatetimeTest().getClass());
		System.out.println("* " + DateTimeFormat.fullDateTime().print(entry.getDatetimeTest()));
		
	}
}
