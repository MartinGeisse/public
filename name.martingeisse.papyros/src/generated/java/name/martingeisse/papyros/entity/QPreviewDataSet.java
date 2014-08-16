/**
 * This file was generated from the database schema.
 */
package name.martingeisse.papyros.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPreviewDataSet is a Querydsl query type for PreviewDataSet
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QPreviewDataSet extends com.mysema.query.sql.RelationalPathBase<PreviewDataSet> {

    private static final long serialVersionUID = -1903527789;

    /**
     * The default instance of this class.
     */
    public static final QPreviewDataSet previewDataSet = new QPreviewDataSet("preview_data_set");

    /**
     * Metamodel property for property 'data'
     */
    public final StringPath data = createString("data");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for property 'order_index'
     */
    public final NumberPath<Integer> orderIndex = createNumber("order_index", Integer.class);

    /**
     * Metamodel property for property 'preview_data_key'
     */
    public final StringPath previewDataKey = createString("preview_data_key");

    /**
     * Metamodel property for property 'template_family_id'
     */
    public final NumberPath<Long> templateFamilyId = createNumber("template_family_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PreviewDataSet> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'preview_data_set_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<TemplateFamily> fk_previewDataSetFk1 = createForeignKey(templateFamilyId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPreviewDataSet(String variable) {
        super(PreviewDataSet.class, forVariable(variable), "null", "preview_data_set");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPreviewDataSet(Path<? extends PreviewDataSet> path) {
        super(path.getType(), path.getMetadata(), "null", "preview_data_set");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPreviewDataSet(PathMetadata<?> metadata) {
        super(PreviewDataSet.class, metadata, "null", "preview_data_set");
    }

}

