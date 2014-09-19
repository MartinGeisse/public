/**
 * This file was generated from the database schema.
 */
package name.martingeisse.slave_services.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QMessageFamily is a Querydsl query type for MessageFamily
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QMessageFamily extends com.mysema.query.sql.RelationalPathBase<MessageFamily> {

    private static final long serialVersionUID = -1742785742;

    /**
     * The default instance of this class.
     */
    public static final QMessageFamily messageFamily = new QMessageFamily("message_family");

    /**
     * Metamodel property for property 'developer_text'
     */
    public final StringPath developerText = createString("developer_text");

    /**
     * Metamodel property for property 'domain'
     */
    public final StringPath domain = createString("domain");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'message_key'
     */
    public final StringPath messageKey = createString("message_key");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<MessageFamily> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'message_translation_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<MessageTranslation> fk__messageTranslationFk1 = createInvForeignKey(id, "message_family_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QMessageFamily(String variable) {
        super(MessageFamily.class, forVariable(variable), "null", "message_family");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QMessageFamily(Path<? extends MessageFamily> path) {
        super(path.getType(), path.getMetadata(), "null", "message_family");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QMessageFamily(PathMetadata<?> metadata) {
        super(MessageFamily.class, metadata, "null", "message_family");
    }

}

