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
 * This class represents rows from table 'post_text'.
 */
public class PostText implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public PostText() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the postBaseId
     */
    private Long postBaseId;

    /**
     * the text
     */
    private String text;

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

    /**
     * Getter method for the text.
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Setter method for the text.
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PostText. id = " + id + ", postBaseId = " + postBaseId + ", text = " + text + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static PostText findById(long id) {
        final QPostText q = QPostText.postText;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<PostText> getModelForId(long id) {
        return new EntityModel<PostText>(QPostText.postText, QPostText.postText.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QPostText q = QPostText.postText;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.postBaseId, postBaseId);
        insert.set(q.text, text);
        id = insert.executeWithKey(Long.class);
    }

}

