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
 * This class represents rows from table 'template_family'.
 */
public class TemplateFamily implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public TemplateFamily() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the key
     */
    private String key;

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
     * Getter method for the key.
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Setter method for the key.
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{TemplateFamily. id = " + id + ", key = " + key + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static TemplateFamily findById(long id) {
        final QTemplateFamily q = QTemplateFamily.templateFamily;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<TemplateFamily> getModelForId(long id) {
        return new EntityModel<TemplateFamily>(QTemplateFamily.templateFamily, QTemplateFamily.templateFamily.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QTemplateFamily q = QTemplateFamily.templateFamily;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.key, key);
        id = insert.executeWithKey(Long.class);
    }

}

