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
 * This class represents rows from table 'message_translation'.
 */
public class MessageTranslation implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public MessageTranslation() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the languageKey
     */
    private String languageKey;

    /**
     * the messageFamilyId
     */
    private Long messageFamilyId;

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
     * Getter method for the languageKey.
     * @return the languageKey
     */
    public String getLanguageKey() {
        return languageKey;
    }

    /**
     * Setter method for the languageKey.
     * @param languageKey the languageKey to set
     */
    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }

    /**
     * Getter method for the messageFamilyId.
     * @return the messageFamilyId
     */
    public Long getMessageFamilyId() {
        return messageFamilyId;
    }

    /**
     * Setter method for the messageFamilyId.
     * @param messageFamilyId the messageFamilyId to set
     */
    public void setMessageFamilyId(Long messageFamilyId) {
        this.messageFamilyId = messageFamilyId;
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
        return "{MessageTranslation. id = " + id + ", languageKey = " + languageKey + ", messageFamilyId = " + messageFamilyId + ", text = " + text + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static MessageTranslation findById(long id) {
        final QMessageTranslation q = QMessageTranslation.messageTranslation;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<MessageTranslation> getModelForId(long id) {
        return new EntityModel<MessageTranslation>(QMessageTranslation.messageTranslation, QMessageTranslation.messageTranslation.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QMessageTranslation q = QMessageTranslation.messageTranslation;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.languageKey, languageKey);
        insert.set(q.messageFamilyId, messageFamilyId);
        insert.set(q.text, text);
        id = insert.executeWithKey(Long.class);
    }

}

