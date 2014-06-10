/**
 * This file was generated from the database schema.
 */
package name.martingeisse.forum.entity;

import name.martingeisse.wicket.model.database.EntityModel;
import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.IModel;
import java.io.Serializable;

/**
 * This class represents rows from table 'post_image'.
 */
public class PostImage implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public PostImage() {
    }

    /**
     * the contentType
     */
    private String contentType;

    /**
     * the data
     */
    private byte[] data;

    /**
     * the id
     */
    private Long id;

    /**
     * the postBaseId
     */
    private Long postBaseId;

    /**
     * Getter method for the contentType.
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Setter method for the contentType.
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Getter method for the data.
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Setter method for the data.
     * @param data the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
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
     * Getter method for the postBaseId.
     * @return the postBaseId
     */
    public Long getPostBaseId() {
        return postBaseId;
    }

    /**
     * Setter method for the postBaseId.
     * @param postBaseId the postBaseId to set
     */
    public void setPostBaseId(Long postBaseId) {
        this.postBaseId = postBaseId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PostImage. contentType = " + contentType + ", data = " + data + ", id = " + id + ", postBaseId = " + postBaseId + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static PostImage findById(long id) {
        final QPostImage q = QPostImage.postImage;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<PostImage> getModelForId(long id) {
        return new EntityModel<PostImage>(QPostImage.postImage, QPostImage.postImage.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QPostImage q = QPostImage.postImage;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.contentType, contentType);
        insert.set(q.data, data);
        insert.set(q.postBaseId, postBaseId);
        id = insert.executeWithKey(Long.class);
    }

}

