/**
 * This file was generated from the database schema.
 */
package name.martingeisse.papyros.entity;

import name.martingeisse.wicket.model.database.EntityModel;
import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import name.martingeisse.sql.terms.IEntityWithOrderIndex;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import org.apache.wicket.model.IModel;
import java.io.Serializable;

/**
 * This class represents rows from table 'preview_data_set'.
 */
public class PreviewDataSet implements Serializable, IEntityWithId<Long>, IEntityWithOrderIndex {

    /**
     * Constructor.
     */
    public PreviewDataSet() {
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
     * the previewDataSetNumber
     */
    private Integer previewDataSetNumber;

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
     * Getter method for the previewDataSetNumber.
     * @return the previewDataSetNumber
     */
    public Integer getPreviewDataSetNumber() {
        return previewDataSetNumber;
    }

    /**
     * Setter method for the previewDataSetNumber.
     * @param previewDataSetNumber the previewDataSetNumber to set
     */
    public void setPreviewDataSetNumber(Integer previewDataSetNumber) {
        this.previewDataSetNumber = previewDataSetNumber;
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
        return "{PreviewDataSet. data = " + data + ", id = " + id + ", name = " + name + ", orderIndex = " + orderIndex + ", previewDataSetNumber = " + previewDataSetNumber + ", templateFamilyId = " + templateFamilyId + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static PreviewDataSet findById(long id) {
        final QPreviewDataSet q = QPreviewDataSet.previewDataSet;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Creates a model that loads a record by id.
     * @param id the id of the record to load
     * @return the model loading the record
     */
    public static IModel<PreviewDataSet> getModelForId(long id) {
        return new EntityModel<PreviewDataSet>(QPreviewDataSet.previewDataSet, QPreviewDataSet.previewDataSet.id.eq(id));
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QPreviewDataSet q = QPreviewDataSet.previewDataSet;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.data, data);
        insert.set(q.name, name);
        insert.set(q.orderIndex, orderIndex);
        insert.set(q.previewDataSetNumber, previewDataSetNumber);
        insert.set(q.templateFamilyId, templateFamilyId);
        id = insert.executeWithKey(Long.class);
    }

}

