/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.apidemo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import name.martingeisse.api.handler.DefaultMasterHandler;
import name.martingeisse.api.handler.misc.NotFoundHandler;
import name.martingeisse.api.servlet.ApiConfiguration;
import name.martingeisse.api.servlet.Launcher;
import name.martingeisse.apidemo.phorum.QPhorumSettings;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.common.database.MysqlDatabaseDescriptor;

import org.joda.time.DateTimeZone;

import com.google.common.base.Predicates;
import com.mysema.commons.lang.Pair;
import com.mysema.query.group.QPair;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.PredicateOperation;

/**
 * Main class.
 */
public class Main {

	/**
	 * The main method
	 * @param args ...
	 * @throws Exception on errors
	 */
	public static void main(final String[] args) throws Exception {

		final MysqlDatabaseDescriptor phorumDatabase = new MysqlDatabaseDescriptor();
		phorumDatabase.setDisplayName("Phorum database");
		phorumDatabase.setUrl("jdbc:mysql://localhost/phorum?zeroDateTimeBehavior=convertToNull&useTimezone=false");
		phorumDatabase.setUsername("root");
		phorumDatabase.setPassword("");
		phorumDatabase.setDefaultTimeZone(DateTimeZone.forID("Europe/Berlin"));
		EntityConnectionManager.initializeDatabaseDescriptors(phorumDatabase);
		
		// TODO: remove
		Expression<Pair<String, String>> pairPath = QPair.create(QPhorumSettings.phorumSettings.name, QPhorumSettings.phorumSettings.type);
		Expression<Pair<String, String>> pairConstant = Expressions.constant(Pair.of("cache", "V"));
		Predicate predicate = new PredicateOperation(Ops.EQ, pairPath, pairConstant);
		
		List<Pair<String, String>> list = new ArrayList<Pair<String,String>>();
		list.add(Pair.of("cache", "V"));
		list.add(Pair.of("display_naxme_source", "V"));
		list.add(Pair.of("file_offsite", "V"));
		Predicate predicate2 = new PredicateOperation(Ops.IN, pairPath, Expressions.constant(list));
		
		SQLQuery query = EntityConnectionManager.getConnection(phorumDatabase).createQuery();
		query.from(QPhorumSettings.phorumSettings);
		query.where(predicate2);
		System.out.println(query.list(QPhorumSettings.phorumSettings));
		System.exit(0);

		DefaultMasterHandler masterHandler = new DefaultMasterHandler();
		masterHandler.setApplicationRequestHandler(new ApplicationHandler());
		masterHandler.getInterceptHandlers().put("/favicon.ico", new NotFoundHandler(false));
		
		ApiConfiguration configuration = new ApiConfiguration();
		configuration.setMasterRequestHandler(masterHandler);
		configuration.getLocalizationConfiguration().setGlobalFallback(Locale.US);
		Launcher.launch(configuration);
		
	}

}
