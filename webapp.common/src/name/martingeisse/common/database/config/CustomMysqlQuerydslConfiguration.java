/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLTemplates;

/**
 * Customized QueryDSL {@link Configuration} implementation that
 * adds tweaked Joda-Time support. QueryDSL uses an implementation
 * that (a) has broken date support that may return a wrong date
 * depending on the time zone, and (b) depends on proper date/time
 * support in JDBC which is horribly broken for MySQL.
 * 
 * This implementation instead fetches date and time values as
 * strings (to bypass the JDBC implementation), then builds
 * values directly from that string.
 * 
 * Values form the database are, by technical nature, partials.
 * The database also returns a default time zone, but this cannot
 * be trusted to be both properly implemented and properly configured.
 * Such values are, however, conceptually instants (not partials) in
 * many application databases, with an implicit default timezone.
 * This configuration directly reflects this situation: It accepts
 * a default time zone to construct instants from the values returned
 * by the database, and supports partials out-of-the-box.
 */
public class CustomMysqlQuerydslConfiguration extends Configuration {

	/**
	 * Constructor.
	 * @param templates the SQL templates
	 */
	public CustomMysqlQuerydslConfiguration(SQLTemplates templates) {
		super(templates);
	}
	
}
