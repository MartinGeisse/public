/**
 * This file was generated from the database schema.
 */
package name.martingeisse.forum.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPostImage is a Querydsl query type for PostImage
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QPostImage extends com.mysema.query.sql.RelationalPathBase<PostImage> {

    private static final long serialVersionUID = 893597811;

    /**
     * The default instance of this class.
     */
    public static final QPostImage postImage = new QPostImage("post_image");

    /**
     * Metamodel property for property 'content_type'
     */
    public final StringPath contentType = createString("content_type");

    /**
     * Metamodel property for property 'data'
     */
    public final SimplePath<byte[]> data = createSimple("data", byte[].class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'post_base_id'
     */
    public final NumberPath<Long> postBaseId = createNumber("post_base_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PostImage> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'post_image_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PostBase> fk_postImageFk1 = createForeignKey(postBaseId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPostImage(String variable) {
        super(PostImage.class, forVariable(variable), "null", "post_image");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPostImage(Path<? extends PostImage> path) {
        super(path.getType(), path.getMetadata(), "null", "post_image");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPostImage(PathMetadata<?> metadata) {
        super(PostImage.class, metadata, "null", "post_image");
    }

}

