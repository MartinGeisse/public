/**
 * This file was generated from the database schema.
 */
package name.martingeisse.forum.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPostText is a Querydsl query type for PostText
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QPostText extends com.mysema.query.sql.RelationalPathBase<PostText> {

    private static final long serialVersionUID = 1553167125;

    /**
     * The default instance of this class.
     */
    public static final QPostText postText = new QPostText("post_text");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'post_base_id'
     */
    public final NumberPath<Long> postBaseId = createNumber("post_base_id", Long.class);

    /**
     * Metamodel property for property 'text'
     */
    public final StringPath text = createString("text");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PostText> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'post_text_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PostBase> fk_postTextFk1 = createForeignKey(postBaseId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPostText(String variable) {
        super(PostText.class, forVariable(variable), "null", "post_text");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPostText(Path<? extends PostText> path) {
        super(path.getType(), path.getMetadata(), "null", "post_text");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPostText(PathMetadata<?> metadata) {
        super(PostText.class, metadata, "null", "post_text");
    }

}

