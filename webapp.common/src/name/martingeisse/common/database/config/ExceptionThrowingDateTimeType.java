/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.mysema.query.sql.types.AbstractType;
import com.mysema.query.sql.types.DateTimeType;

/**
 * Custom type implementation for Joda {@link DateTime} that throws an
 * exception if used. This type is useful when given precedence over
 * QueryDSL's buggy {@link DateTimeType}, so application code doesn't
 * accidentally use it. (QueryDSL's implementation is buggy because
 * it uses JDBC time zone support which is horribly broken).
 */
public class ExceptionThrowingDateTimeType extends AbstractType<DateTime> {

	/**
	 * the MESSAGE
	 */
	private static final String MESSAGE = "cannot use DateTime mapping -- no time zone";
	
	/**
	 * Constructor.
	 */
	public ExceptionThrowingDateTimeType() {
		super(Types.TIMESTAMP);
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getReturnedClass()
	 */
	@Override
	public Class<DateTime> getReturnedClass() {
		return DateTime.class;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getValue(java.sql.ResultSet, int)
	 */
	@Override
	@Nullable
	public DateTime getValue(final ResultSet rs, final int startIndex) throws SQLException {
		throw new RuntimeException(MESSAGE);
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#setValue(java.sql.PreparedStatement, int, java.lang.Object)
	 */
	@Override
	public void setValue(final PreparedStatement st, final int startIndex, final DateTime value) throws SQLException {
		throw new RuntimeException(MESSAGE);
	}

}
