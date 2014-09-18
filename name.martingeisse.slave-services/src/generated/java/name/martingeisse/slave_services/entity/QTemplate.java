/**
 * This file was generated from the database schema.
 */
package name.martingeisse.slave_services.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QTemplate is a Querydsl query type for Template
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QTemplate extends com.mysema.query.sql.RelationalPathBase<Template> {

    private static final long serialVersionUID = -529466403;

    /**
     * The default instance of this class.
     */
    public static final QTemplate template = new QTemplate("template");

    /**
     * Metamodel property for property 'content'
     */
    public final StringPath content = createString("content");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'language_key'
     */
    public final StringPath languageKey = createString("language_key");

    /**
     * Metamodel property for property 'template_family_id'
     */
    public final NumberPath<Long> templateFamilyId = createNumber("template_family_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Template> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'template_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<TemplateFamily> fk_templateFk1 = createForeignKey(templateFamilyId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QTemplate(String variable) {
        super(Template.class, forVariable(variable), "null", "template");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QTemplate(Path<? extends Template> path) {
        super(path.getType(), path.getMetadata(), "null", "template");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QTemplate(PathMetadata<?> metadata) {
        super(Template.class, metadata, "null", "template");
    }

}

