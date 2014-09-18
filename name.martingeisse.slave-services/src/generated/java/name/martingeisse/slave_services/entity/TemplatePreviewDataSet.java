/**
 * This file was generated from the database schema.
 */
package name.martingeisse.slave_services.entity;

import name.martingeisse.wicket.model.database.EntityModel;
import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import name.martingeisse.sql.terms.IEntityWithOrderIndex;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.IModel;
import java.io.Serializable;

/**
 * This class represents rows from table 'template_preview_data_set'.
 */
public class TemplatePreviewDataSet implements Serializable, IEntityWithId<Long>, IEntityWithOrderIndex {

    /**
     * Constructor.
     */
    public TemplatePreviewDataSet() {
    }

    /**
     * the data
     */
    private String data;

    /**
     * the id
     */
    private Long id;

    /**
     * the name
     */
    private String name;

    /**
     * the orderIndex
     */
    private Integer orderIndex;

    /**
     * the previewDataKey
     */
    private String previewDataKey;

    /**
     * the templateFamilyId
     */
    private Long templateFamilyId;

    /**
     * Getter method for the data.
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Setter method for the data.
     * @param data the data to set
     */
    public void setData(String data) {
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

    /**
     * Getter method for the previewDataKey.
     * @return the previewDataKey
     */
    public String getPreviewDataKey() {
        return previewDataKey;
    }

    /**
     * Setter method for the previewDataKey.
     * @param previewDataKey the previewDataKey to set
     */
    public void setPreviewDataKey(String previewDataKey) {
        this.previewDataKey = previewDataKey;
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
        return "{TemplatePreviewDataSet. data = " + data + ", id = " + id + ", name = " + name + ", orderIndex = " + orderIndex + ", previewDataKey = " + previewDataKey + ", templateFamilyId = " + templateFamilyId + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static TemplatePreviewDataSet findById(long id) {
        final QTemplatePreviewDataSet q = QTemplatePreviewDataSet.templatePreviewDataSet;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<TemplatePreviewDataSet> getModelForId(long id) {
        return new EntityModel<TemplatePreviewDataSet>(QTemplatePreviewDataSet.templatePreviewDataSet, QTemplatePreviewDataSet.templatePreviewDataSet.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QTemplatePreviewDataSet q = QTemplatePreviewDataSet.templatePreviewDataSet;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.data, data);
        insert.set(q.name, name);
        insert.set(q.orderIndex, orderIndex);
        insert.set(q.previewDataKey, previewDataKey);
        insert.set(q.templateFamilyId, templateFamilyId);
        id = insert.executeWithKey(Long.class);
    }

}

