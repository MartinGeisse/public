/**
 * This file was generated from the database schema.
 */
package name.martingeisse.forum.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QConversation is a Querydsl query type for Conversation
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QConversation extends com.mysema.query.sql.RelationalPathBase<Conversation> {

    private static final long serialVersionUID = -1138394709;

    /**
     * The default instance of this class.
     */
    public static final QConversation conversation = new QConversation("conversation");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Conversation> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'post_base_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PostBase> fk__postBaseFk1 = createInvForeignKey(id, "conversation_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QConversation(String variable) {
        super(Conversation.class, forVariable(variable), "null", "conversation");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QConversation(Path<? extends Conversation> path) {
        super(path.getType(), path.getMetadata(), "null", "conversation");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QConversation(PathMetadata<?> metadata) {
        super(Conversation.class, metadata, "null", "conversation");
    }

}

