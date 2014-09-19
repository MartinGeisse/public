/**
 * This file was generated from the database schema.
 */
package name.martingeisse.slave_services.entity;

import name.martingeisse.wicket.model.database.EntityModel;
import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.IModel;
import java.io.Serializable;

/**
 * This class represents rows from table 'message_family'.
 */
public class MessageFamily implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public MessageFamily() {
    }

    /**
     * the developerText
     */
    private String developerText;

    /**
     * the domain
     */
    private String domain;

    /**
     * the id
     */
    private Long id;

    /**
     * the messageKey
     */
    private String messageKey;

    /**
     * Getter method for the developerText.
     * @return the developerText
     */
    public String getDeveloperText() {
        return developerText;
    }

    /**
     * Setter method for the developerText.
     * @param developerText the developerText to set
     */
    public void setDeveloperText(String developerText) {
        this.developerText = developerText;
    }

    /**
     * Getter method for the domain.
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Setter method for the domain.
     * @param domain the domain to set
     */
    public void setDomain(String domain) {
        this.domain = domain;
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
     * Getter method for the messageKey.
     * @return the messageKey
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * Setter method for the messageKey.
     * @param messageKey the messageKey to set
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{MessageFamily. developerText = " + developerText + ", domain = " + domain + ", id = " + id + ", messageKey = " + messageKey + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static MessageFamily findById(long id) {
        final QMessageFamily q = QMessageFamily.messageFamily;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<MessageFamily> getModelForId(long id) {
        return new EntityModel<MessageFamily>(QMessageFamily.messageFamily, QMessageFamily.messageFamily.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QMessageFamily q = QMessageFamily.messageFamily;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.developerText, developerText);
        insert.set(q.domain, domain);
        insert.set(q.messageKey, messageKey);
        id = insert.executeWithKey(Long.class);
    }

}

