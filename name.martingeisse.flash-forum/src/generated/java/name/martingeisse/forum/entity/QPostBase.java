/**
 * This file was generated from the database schema.
 */
package name.martingeisse.forum.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPostBase is a Querydsl query type for PostBase
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QPostBase extends com.mysema.query.sql.RelationalPathBase<PostBase> {

    private static final long serialVersionUID = 1552626873;

    /**
     * The default instance of this class.
     */
    public static final QPostBase postBase = new QPostBase("post_base");

    /**
     * Metamodel property for property 'author_identicon_code'
     */
    public final NumberPath<Long> authorIdenticonCode = createNumber("author_identicon_code", Long.class);

    /**
     * Metamodel property for property 'author_ip_address'
     */
    public final StringPath authorIpAddress = createString("author_ip_address");

    /**
     * Metamodel property for property 'author_name'
     */
    public final StringPath authorName = createString("author_name");

    /**
     * Metamodel property for property 'conversation_id'
     */
    public final NumberPath<Long> conversationId = createNumber("conversation_id", Long.class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'order_index'
     */
    public final NumberPath<Integer> orderIndex = createNumber("order_index", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PostBase> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'post_base_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Conversation> fk_postBaseFk1 = createForeignKey(conversationId, "id");

    /**
     * Metamodel property for reverse foreign key 'post_image_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PostImage> fk__postImageFk1 = createInvForeignKey(id, "post_base_id");

    /**
     * Metamodel property for reverse foreign key 'post_text_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PostText> fk__postTextFk1 = createInvForeignKey(id, "post_base_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPostBase(String variable) {
        super(PostBase.class, forVariable(variable), "null", "post_base");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPostBase(Path<? extends PostBase> path) {
        super(path.getType(), path.getMetadata(), "null", "post_base");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPostBase(PathMetadata<?> metadata) {
        super(PostBase.class, metadata, "null", "post_base");
    }

}

