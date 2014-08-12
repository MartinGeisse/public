/**
 * This file was generated from the database schema.
 */
package name.martingeisse.papyros.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QTemplateFamily is a Querydsl query type for TemplateFamily
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QTemplateFamily extends com.mysema.query.sql.RelationalPathBase<TemplateFamily> {

    private static final long serialVersionUID = -1337513567;

    /**
     * The default instance of this class.
     */
    public static final QTemplateFamily templateFamily = new QTemplateFamily("template_family");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'key'
     */
    public final StringPath key = createString("key");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<TemplateFamily> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'template_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Template> fk__templateFk1 = createInvForeignKey(id, "template_family_id");

    /**
     * Metamodel property for reverse foreign key 'preview_data_set_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PreviewDataSet> fk__previewDataSetFk1 = createInvForeignKey(id, "template_family_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QTemplateFamily(String variable) {
        super(TemplateFamily.class, forVariable(variable), "null", "template_family");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QTemplateFamily(Path<? extends TemplateFamily> path) {
        super(path.getType(), path.getMetadata(), "null", "template_family");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QTemplateFamily(PathMetadata<?> metadata) {
        super(TemplateFamily.class, metadata, "null", "template_family");
    }

}

