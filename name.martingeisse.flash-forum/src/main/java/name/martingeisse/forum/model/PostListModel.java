/**
 * Copyright (c) 2010 Martin Geisse
 *
 * This file is distributed under the terms of the MIT license.
 */

package name.martingeisse.forum.model;

import java.util.List;
import name.martingeisse.forum.entity.PostBase;
import name.martingeisse.forum.entity.PostFile;
import name.martingeisse.forum.entity.PostText;
import name.martingeisse.forum.entity.QPostBase;
import name.martingeisse.forum.entity.QPostFile;
import name.martingeisse.forum.entity.QPostText;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.AbstractReadOnlyModel;
import com.mysema.query.sql.SQLQuery;

/**
 * This model returns a list of posts. Each post is represented by an object
 * array that contains ({@link PostBase}, {@link PostText}, {@link PostFile})
 * in this order. Objects other than those appropriate for the post type are null.
 */
public final class PostListModel extends AbstractReadOnlyModel<List<Object[]>> {

	/**
	 * the conversationId
	 */
	private final long conversationId;

	/**
	 * Constructor.
	 * @param conversationId the conversationId
	 */
	public PostListModel(final long conversationId) {
		this.conversationId = conversationId;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
	 */
	@Override
	public List<Object[]> getObject() {
		final QPostBase qpb = QPostBase.postBase;
		final QPostText qpt = QPostText.postText;
		final QPostFile qpf = QPostFile.postFile;
		final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
		query.from(qpb);
		query.leftJoin(qpt).on(qpt.postBaseId.eq(qpb.id));
		query.leftJoin(qpf).on(qpf.postBaseId.eq(qpb.id));
		query.where(qpb.conversationId.eq(conversationId));
		query.orderBy(qpb.creationInstant.asc());
		List<Object[]> result = query.list(qpb, qpt, qpf);
		for (Object[] entry : result) {
			if (((PostText)entry[1]).getId() == null) {
				entry[1] = null;
			}
			if (((PostFile)entry[2]).getId() == null) {
				entry[2] = null;
			}
		}
		return result;
	}

}
