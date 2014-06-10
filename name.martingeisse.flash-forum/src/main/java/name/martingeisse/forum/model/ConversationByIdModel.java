/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.forum.model;

import name.martingeisse.forum.entity.Conversation;
import name.martingeisse.forum.entity.QConversation;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.AbstractReadOnlyModel;
import com.mysema.query.sql.SQLQuery;

/**
 * This model returns a conversation, using its ID.
 */
public final class ConversationByIdModel extends AbstractReadOnlyModel<Conversation> {

	/**
	 * the id
	 */
	private final long id;

	/**
	 * Constructor.
	 * @param id the id
	 */
	public ConversationByIdModel(final long id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public Conversation getObject() {
		final QConversation qc = QConversation.conversation;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		return query.from(qc).where(qc.id.eq(id)).singleResult(qc);
	}

}
