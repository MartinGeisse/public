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
import com.datastax.driver.core.Row;
import com.datastax.driver.core.querybuilder.Assignment;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select.Where;

/**
 * Stores Wicket sessions to a Cassandra database.
 * 
 * TODO Cassandra 3 will likely support a "SELECT someMap['foo']" (i.e. selecting a
 * single map element instead of the whole map). Since each field is stored as a
 * separate column internally, this seems straightforward. When that happens, the
 * session table layout can be changed to use a map for the attributes, only the
 * id as the primary key and one row per session.
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
	 * Returns a Cassandra Clause object for session ID equality.
	 */
	private final Clause getSessionIdClauseFromSessionId(final String sessionId) {
		return QueryBuilder.eq("id", sessionId);
	}

	/**
	 * Returns a Cassandra Clause object for attribute name equality.
	 */
	private final Clause getAttributeNameClauseFromAttributeName(final String attributeName) {
		return QueryBuilder.eq("attribute_name", attributeName);
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
	 * Returns the database row for the specified ID clause and attribute name, or null if the row wasn't found.
	 */
	private Row fetchRow(Clause sessionIdClause, final String attributeName) {
		Where where = QueryBuilder.select("attribute_value").from(tableName).where(sessionIdClause).and(getAttributeNameClauseFromAttributeName(attributeName));
		return cassandraSession.execute(where).one();
	}

	/**
	 * Returns the database row for the session ID that was sent as part of a request and the
	 * specified attribute name, or null if either the ID or the row or the attribute wasn't found.
	 */
	private Row fetchRow(final Request request, final String attributeName) {
		final Clause sessionIdClause = getSessionIdClauseFromRequest(request);
		return (sessionIdClause == null ? null : fetchRow(sessionIdClause, attributeName));
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
		final Clause sessionIdClause = getSessionIdClauseFromRequest(request);
		if (sessionIdClause == null) {
			return new ArrayList<>();
		}
		final List<String> result = new ArrayList<>();
		for (Row row : cassandraSession.execute(QueryBuilder.select("attribute_name").from(tableName).where(sessionIdClause)).all()) {
			result.add(row.getString(0));
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#setAttribute(org.apache.wicket.request.Request, java.lang.String, java.io.Serializable)
	 */
	@Override
	public void setAttribute(final Request request, final String attributeName, final Serializable value) {
		if (attributeName.equals("id")) {
			throw new IllegalArgumentException("cannot set the 'id' attribute of a session row");
		}
		final Clause sessionIdClause = getSessionIdClauseFromRequest(request);
		if (sessionIdClause != null) {
			final Assignment assignment = QueryBuilder.set("attribute_value", ByteBuffer.wrap(SerializationUtils.serialize(value)));
			final Clause attributeNameClause = getAttributeNameClauseFromAttributeName(attributeName);
			cassandraSession.execute(QueryBuilder.update(tableName).with(assignment).where(sessionIdClause).and(attributeNameClause));
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#removeAttribute(org.apache.wicket.request.Request, java.lang.String)
	 */
	@Override
	public void removeAttribute(final Request request, final String attributeName) {
		if (attributeName.equals("id")) {
			throw new IllegalArgumentException("cannot remove the 'id' attribute of a session row");
		}
		final Clause sessionIdClause = getSessionIdClauseFromRequest(request);
		if (sessionIdClause != null) {
			final Clause attributeNameClause = getAttributeNameClauseFromAttributeName(attributeName);
			cassandraSession.execute(QueryBuilder.delete().from(tableName).where(sessionIdClause).and(attributeNameClause));
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.session.ISessionStore#invalidate(org.apache.wicket.request.Request)
	 */
	@Override
	public void invalidate(final Request request) {
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
		final String existingSessionId = getSessionIdFromRequest(request);
		final Clause existingSessionIdClause = (existingSessionId == null ? null : getSessionIdClauseFromSessionId(existingSessionId));
		final boolean rowExists = (existingSessionIdClause == null ? false : (cassandraSession.execute(QueryBuilder.select("id").from(tableName).where(existingSessionIdClause)).one() != null));
		if (rowExists) {
			return existingSessionId;
		} else if (create) {
			// TODO avoid collision better than just using a random session ID
			final String newSessionId = RandomStringUtils.randomAlphanumeric(64);
			final ByteBuffer emptyBuffer = ByteBuffer.wrap(new byte[0]);
			final Insert insert = QueryBuilder.insertInto(tableName).value("id", newSessionId).value("attribute_name", "id").value("attribute_value", emptyBuffer);
			cassandraSession.execute(insert);
			final IRequestLogger logger = Application.get().getRequestLogger();
			if (logger != null) {
				logger.sessionCreated(newSessionId);
			}
			return newSessionId;
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
			String sessionId = getSessionId(request, true);
			setAttribute(request, Session.SESSION_ATTRIBUTE_NAME, newSession);

			// TODO this is probably necessary
			//			Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE_NAME, sessionId);
			//			sessionCookie.setMaxAge(365 * 24 * 60 * 60); // TODO where should this come from?
			//			sessionCookie.setPath("/");
			//			((HttpServletResponse)RequestCycle.get().getResponse().getContainerResponse()).addCookie(sessionCookie);

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
