/**
 * This file was generated from the database schema.
 */
package name.martingeisse.papyros.entity;

import name.martingeisse.wicket.model.database.EntityModel;
import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.IModel;
import java.io.Serializable;

/**
 * This class represents rows from table 'template'.
 */
public class Template implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public Template() {
    }

    /**
     * the content
     */
    private String content;

    /**
     * the id
     */
    private Long id;

    /**
     * the languageKey
     */
    private String languageKey;

    /**
     * the templateFamilyId
     */
    private Long templateFamilyId;

    /**
     * Getter method for the content.
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter method for the content.
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
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
     * Getter method for the templateFamilyId.
     * @return the templateFamilyId
     */
    public Long getTemplateFamilyId() {
        return templateFamilyId;
    }

    /**
     * Setter method for the templateFamilyId.
     * @param templateFamilyId the templateFamilyId to set
     */
    public void setTemplateFamilyId(Long templateFamilyId) {
        this.templateFamilyId = templateFamilyId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Template. content = " + content + ", id = " + id + ", languageKey = " + languageKey + ", templateFamilyId = " + templateFamilyId + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static Template findById(long id) {
        final QTemplate q = QTemplate.template;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<Template> getModelForId(long id) {
        return new EntityModel<Template>(QTemplate.template, QTemplate.template.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QTemplate q = QTemplate.template;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.content, content);
        insert.set(q.languageKey, languageKey);
        insert.set(q.templateFamilyId, templateFamilyId);
        id = insert.executeWithKey(Long.class);
    }

}

