/**
 * This file was generated from the database schema.
 */
package name.martingeisse.forum.entity;

import name.martingeisse.wicket.model.database.EntityModel;
import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import name.martingeisse.sql.terms.IEntityWithOrderIndex;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.IModel;
import java.io.Serializable;

/**
 * This class represents rows from table 'post_base'.
 */
public class PostBase implements Serializable, IEntityWithId<Long>, IEntityWithOrderIndex {

    /**
     * Constructor.
     */
    public PostBase() {
    }

    /**
     * the authorIdenticonCode
     */
    private Long authorIdenticonCode;

    /**
     * the authorIpAddress
     */
    private String authorIpAddress;

    /**
     * the authorName
     */
    private String authorName;

    /**
     * the conversationId
     */
    private Long conversationId;

    /**
     * the id
     */
    private Long id;

    /**
     * the orderIndex
     */
    private Integer orderIndex;

    /**
     * Getter method for the authorIdenticonCode.
     * @return the authorIdenticonCode
     */
    public Long getAuthorIdenticonCode() {
        return authorIdenticonCode;
    }

    /**
     * Setter method for the authorIdenticonCode.
     * @param authorIdenticonCode the authorIdenticonCode to set
     */
    public void setAuthorIdenticonCode(Long authorIdenticonCode) {
        this.authorIdenticonCode = authorIdenticonCode;
    }

    /**
     * Getter method for the authorIpAddress.
     * @return the authorIpAddress
     */
    public String getAuthorIpAddress() {
        return authorIpAddress;
    }

    /**
     * Setter method for the authorIpAddress.
     * @param authorIpAddress the authorIpAddress to set
     */
    public void setAuthorIpAddress(String authorIpAddress) {
        this.authorIpAddress = authorIpAddress;
    }

    /**
     * Getter method for the authorName.
     * @return the authorName
     */
    public String getAuthorName() {
        return authorName;
    }

    /**
     * Setter method for the authorName.
     * @param authorName the authorName to set
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Getter method for the conversationId.
     * @return the conversationId
     */
    public Long getConversationId() {
        return conversationId;
    }

    /**
     * Setter method for the conversationId.
     * @param conversationId the conversationId to set
     */
    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * Getter method for the id.
     * @return the id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Setter method for the id.
     * @param id the id to set
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter method for the orderIndex.
     * @return the orderIndex
     */
    @Override
    public Integer getOrderIndex() {
        return orderIndex;
    }

    /**
     * Setter method for the orderIndex.
     * @param orderIndex the orderIndex to set
     */
    @Override
    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PostBase. authorIdenticonCode = " + authorIdenticonCode + ", authorIpAddress = " + authorIpAddress + ", authorName = " + authorName + ", conversationId = " + conversationId + ", id = " + id + ", orderIndex = " + orderIndex + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static PostBase findById(long id) {
        final QPostBase q = QPostBase.postBase;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<PostBase> getModelForId(long id) {
        return new EntityModel<PostBase>(QPostBase.postBase, QPostBase.postBase.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QPostBase q = QPostBase.postBase;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.authorIdenticonCode, authorIdenticonCode);
        insert.set(q.authorIpAddress, authorIpAddress);
        insert.set(q.authorName, authorName);
        insert.set(q.conversationId, conversationId);
        insert.set(q.orderIndex, orderIndex);
        id = insert.executeWithKey(Long.class);
    }

}

