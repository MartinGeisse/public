/**
 * This file was generated from the database schema.
 */
package name.martingeisse.slave_services.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QMessageTranslation is a Querydsl query type for MessageTranslation
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QMessageTranslation extends com.mysema.query.sql.RelationalPathBase<MessageTranslation> {

    private static final long serialVersionUID = -755512413;

    /**
     * The default instance of this class.
     */
    public static final QMessageTranslation messageTranslation = new QMessageTranslation("message_translation");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'language_key'
     */
    public final StringPath languageKey = createString("language_key");

    /**
     * Metamodel property for property 'message_family_id'
     */
    public final NumberPath<Long> messageFamilyId = createNumber("message_family_id", Long.class);

    /**
     * Metamodel property for property 'text'
     */
    public final StringPath text = createString("text");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<MessageTranslation> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'message_translation_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<MessageFamily> fk_messageTranslationFk1 = createForeignKey(messageFamilyId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QMessageTranslation(String variable) {
        super(MessageTranslation.class, forVariable(variable), "null", "message_translation");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QMessageTranslation(Path<? extends MessageTranslation> path) {
        super(path.getType(), path.getMetadata(), "null", "message_translation");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QMessageTranslation(PathMetadata<?> metadata) {
        super(MessageTranslation.class, metadata, "null", "message_translation");
    }

}

