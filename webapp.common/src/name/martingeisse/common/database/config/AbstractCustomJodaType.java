/**
 * Copyright (c) 2011 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Nullable;
import name.martingeisse.common.util.ParameterUtil;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.mysema.query.sql.types.AbstractType;

/**
 * Base class for Joda-based QueryDSL types.
 * @param <T> the Joda type
 */
public abstract class AbstractCustomJodaType<T> extends AbstractType<T> {

	/**
	 * the sqlType
	 */
	private final int sqlType;

	/**
	 * the timeZone
	 */
	private final DateTimeZone timeZone;

	/**
	 * the pattern
	 */
	private final String pattern;

	/**
	 * the javaClass
	 */
	private final Class<T> javaClass;

	/**
	 * Constructor.
	 * @param sqlType the SQL type
	 * @param timeZone the default time zone, used to create instants from partial datetimes
	 * @param pattern the Joda pattern for database values
	 * @param javaClass the class object for the Joda type T
	 */
	public AbstractCustomJodaType(final int sqlType, final DateTimeZone timeZone, final String pattern, final Class<T> javaClass) {
		super(sqlType);
		this.sqlType = sqlType;
		this.timeZone = timeZone;
		this.pattern = ParameterUtil.ensureNotNull(pattern, "pattern");
		this.javaClass = ParameterUtil.ensureNotNull(javaClass, "javaClass");
		;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getReturnedClass()
	 */
	@Override
	public final Class<T> getReturnedClass() {
		return javaClass;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getValue(java.sql.ResultSet, int)
	 */
	@Override
	@Nullable
	public final T getValue(final ResultSet resultSet, final int startIndex) throws SQLException {
		final String stringValue = resultSet.getString(startIndex);
		if (stringValue == null) {
			return null;
		} else {
			final T result = parse(getFormatter(), stringValue);
			return result;
		}
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#setValue(java.sql.PreparedStatement, int, java.lang.Object)
	 */
	@Override
	public final void setValue(final PreparedStatement st, final int startIndex, final T value) throws SQLException {
		if (value == null) {
			st.setNull(startIndex, sqlType);
		} else {
			final String result = print(getFormatter(), value);
			st.setString(startIndex, result);
		}
	}

	/**
	 * Obtains a date-time formatter for this type using the default time zone.
	 */
	private final DateTimeFormatter getFormatter() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
		return (timeZone == null ? formatter : formatter.withZone(timeZone));
	}

	/**
	 * This method must be implemented to invoke the correct print method on the
	 * formatter.
	 * @param formatter the formatter
	 * @param value the value to print
	 * @return the printed value
	 */
	protected abstract String print(DateTimeFormatter formatter, T value);

	/**
	 * This method must be implemented to invoke the correct parse method on the
	 * formatter.
	 * @param formatter the formatter
	 * @param value the value to parse
	 * @return the parsed value
	 */
	protected abstract T parse(DateTimeFormatter formatter, String value);

}
