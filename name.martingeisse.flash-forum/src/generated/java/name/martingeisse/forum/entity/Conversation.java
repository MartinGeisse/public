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
 * This class represents rows from table 'conversation'.
 */
public class Conversation implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public Conversation() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the name
     */
    private String name;

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
     * Getter method for the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Conversation. id = " + id + ", name = " + name + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static Conversation findById(long id) {
        final QConversation q = QConversation.conversation;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<Conversation> getModelForId(long id) {
        return new EntityModel<Conversation>(QConversation.conversation, QConversation.conversation.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QConversation q = QConversation.conversation;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.name, name);
        id = insert.executeWithKey(Long.class);
    }

}

