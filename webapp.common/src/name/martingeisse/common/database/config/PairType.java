/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.common.database.config;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import javax.annotation.Nullable;
import com.mysema.commons.lang.Pair;
import com.mysema.query.sql.types.Type;

/**
 * This is not a complete {@link Type} implementation, but just
 * enough to make QueryDSL serialize {@link Pair} constants.
 */
public class PairType implements Type<Pair<?, ?>> {

	/**
	 * the configuration
	 */
	private CustomMysqlQuerydslConfiguration configuration;
	
	/**
	 * Constructor.
	 * @param configuration the configuration
	 */
	public PairType(CustomMysqlQuerydslConfiguration configuration) {
		this.configuration = configuration;
	}
	
	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getSQLTypes()
	 */
	@Override
	public int[] getSQLTypes() {
		return new int[] {Types.OTHER, Types.OTHER};
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getReturnedClass()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class<Pair<?, ?>> getReturnedClass() {
		return (Class<Pair<?, ?>>)(Class<?>)Pair.class;
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#getValue(java.sql.ResultSet, int)
	 */
	@Override
	@Nullable
	public Pair<?, ?> getValue(final ResultSet rs, final int startIndex) throws SQLException {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see com.mysema.query.sql.types.Type#setValue(java.sql.PreparedStatement, int, java.lang.Object)
	 */
	@Override
	public void setValue(final PreparedStatement st, final int startIndex, final Pair<?, ?> value) throws SQLException {
		configuration.set(st, null, startIndex, value.getFirst());
		configuration.set(st, null, startIndex + 1, value.getSecond());
	}

}
