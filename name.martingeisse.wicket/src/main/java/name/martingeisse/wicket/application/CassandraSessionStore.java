/**
 * Copyright (c) 2013 Shopgate GmbH
 */

package name.martingeisse.wicket.application;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.IRequestLogger;
import org.apache.wicket.request.Request;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Assignment;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Builder;

/**
 * Stores Wicket sessions to a Cassandra database.
 */
public final class CassandraSessionStore extends AbstractSessionStore {

	/**
	 * the cassandraSession
	 */
	private final com.datastax.driver.core.Session cassandraSession;

	/**
	 * the tableName
	 */
	private final String tableName;

	/**
	 * Constructor.
	 * @param cassandraSession the Cassandra session used to talk to the database
	 * @param tableName the name of the session table
	 */
	public CassandraSessionStore(final com.datastax.driver.core.Session cassandraSession, final String tableName) {
		this.cassandraSession = cassandraSession;
		this.tableName = tableName;
	}

	/**
	 * Returns a Cassandra Clause object for equality to the specified session ID.
	 */
	private final Clause getSessionIdClauseFromSessionId(final String sessionId) {
		return QueryBuilder.eq("id", sessionId);
	}
	
	/**
	 * Returns a Cassandra Clause object for equality to the session ID
	 * that was sent as part of a request, or null if not found.
	 */
	private final Clause getSessionIdClauseFromRequest(final Request request) {
		String id = getSessionIdFromRequest(request);
		return (id == null ? null : getSessionIdClauseFromSessionId(id));
	}
	
	/**
	 * Returns the database row for the specified clause, or null if the row wasn't found. If a field is specified,
	 * only that field gets fetched, otherwise all fields get fetched.
	 */
	private Row fetchRow(Clause sessionIdClause, final String field) {
		final Builder builder = (field == null ? QueryBuilder.select().all() : QueryBuilder.select(field));
		return cassandraSession.execute(builder.from(tableName).where(sessionIdClause)).one();
	}
	
	/**
	 * Returns the database row for the session ID that was sent as part of a request, or null
	 * if either the ID or the row wasn't found. If a field is specified, only that field gets
	 * fetched, otherwise all fields get fetched.
	 */
	private Row fetchRow(final Request request, final String field) {
		final Clause sessionIdClause = getSessionIdClauseFromRequest(request);
		return (sessionIdClause == null ? null : fetchRow(sessionIdClause, field));
	}

	/**
	 * Unserializes a single object field from a row.
	 */
	private Serializable unserializeObjectField(final Row row, final int index) {
		final ByteBuffer buffer = row.getBytes(index);
		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		return SerializationUtils.deserialize(data);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#getAttribute(org.apache.wicket.request.Request, java.lang.String)
	 */
	@Override
	public Serializable getAttribute(final Request request, final String name) {
		if (name.equals("id")) {
			return getSessionId(request, false);
		}
		final Row row = fetchRow(request, name);
		return (row == null ? null : unserializeObjectField(row, 0));
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#getAttributeNames(org.apache.wicket.request.Request)
	 */
	@Override
	public List<String> getAttributeNames(final Request request) {
		final Row row = fetchRow(request, null);
		if (row == null) {
			return new ArrayList<>();
		}
		final ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
		final List<String> result = new ArrayList<>();
		for (int i = 0; i < columnDefinitions.size(); i++) {
			result.add(columnDefinitions.getName(i));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#setAttribute(org.apache.wicket.request.Request, java.lang.String, java.io.Serializable)
	 */
	@Override
	public void setAttribute(final Request request, final String name, final Serializable value) {
		if (name.equals("id")) {
			throw new IllegalArgumentException("cannot set the 'id' attribute of a session row");
		}
		final Clause sessionIdClause = getSessionIdClauseFromRequest(request);
		if (sessionIdClause != null) {
			final Assignment assignment = QueryBuilder.set(name, value);
			cassandraSession.execute(QueryBuilder.update(tableName).with(assignment).where(sessionIdClause));
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#removeAttribute(org.apache.wicket.request.Request, java.lang.String)
	 */
	@Override
	public void removeAttribute(final Request request, final String name) {
		if (name.equals("id")) {
			throw new IllegalArgumentException("cannot remove the 'id' attribute of a session row");
		}
		final Clause sessionIdClause = getSessionIdClauseFromRequest(request);
		if (sessionIdClause != null) {
			cassandraSession.execute(QueryBuilder.delete(name).from(tableName).where(sessionIdClause));
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#invalidate(org.apache.wicket.request.Request)
	 */
	@Override
	public void invalidate(final Request request) {
		
		TODO check
		
		Session session = lookup(request);
		if (session != null) {
			session.onInvalidate();
		}
		final String sessionId = getSessionId(request, false);
		if (sessionId != null) {
			for (final UnboundListener listener : getUnboundListener()) {
				listener.sessionUnbound(sessionId);
			}
		}
		final Clause sessionIdClause = getSessionIdClauseFromRequest(request);
		if (sessionIdClause != null) {
			cassandraSession.execute(QueryBuilder.delete().from(tableName).where(sessionIdClause));
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#getSessionId(org.apache.wicket.request.Request, boolean)
	 */
	@Override
	public String getSessionId(final Request request, final boolean create) {
		final Row row = fetchRow(request, "id");
		if (row != null) {
			return row.getString(0);
		} else if (create) {
			// TODO avoid collision better than just using a random session ID
			final String id = RandomStringUtils.randomAlphanumeric(64);
			final Insert insert = QueryBuilder.insertInto(tableName).value("id", id);
			cassandraSession.execute(insert);
			final IRequestLogger logger = Application.get().getRequestLogger();
			if (logger != null) {
				logger.sessionCreated(id);
			}
			return id;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#lookup(org.apache.wicket.request.Request)
	 */
	@Override
	public Session lookup(final Request request) {
		return (Session)getAttribute(request, Session.SESSION_ATTRIBUTE_NAME);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#bind(org.apache.wicket.request.Request, org.apache.wicket.Session)
	 */
	@Override
	public void bind(final Request request, final Session newSession) {
		if (getAttribute(request, Session.SESSION_ATTRIBUTE_NAME) != newSession) {
			for (final BindListener listener : getBindListeners()) {
				listener.bindingSession(request, newSession);
			}
			// TODO Wicket's HttpStore only stores the session if the session row already exists...!?
			// (alternative would be to call getSessionId(request, true) first to create the row)
			if (getSessionId(request, false) != null) {
				setAttribute(request, Session.SESSION_ATTRIBUTE_NAME, newSession);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#flushSession(org.apache.wicket.request.Request, org.apache.wicket.Session)
	 */
	@Override
	public void flushSession(final Request request, final Session session) {
		bind(request, session);
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#destroy()
	 */
	@Override
	public void destroy() {
	}

}
