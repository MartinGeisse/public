/**
 * This file was generated from the database schema.
 */
package name.martingeisse.forum.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPostFile is a Querydsl query type for PostFile
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QPostFile extends com.mysema.query.sql.RelationalPathBase<PostFile> {

    private static final long serialVersionUID = 1552753508;

    /**
     * The default instance of this class.
     */
    public static final QPostFile postFile = new QPostFile("post_file");

    /**
     * Metamodel property for property 'content_type'
     */
    public final StringPath contentType = createString("content_type");

    /**
     * Metamodel property for property 'data'
     */
    public final SimplePath<byte[]> data = createSimple("data", byte[].class);

    /**
     * Metamodel property for property 'filename'
     */
    public final StringPath filename = createString("filename");

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
    public final com.mysema.query.sql.PrimaryKey<PostFile> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'post_file_fk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PostBase> fk_postFileFk1 = createForeignKey(postBaseId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPostFile(String variable) {
        super(PostFile.class, forVariable(variable), "null", "post_file");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPostFile(Path<? extends PostFile> path) {
        super(path.getType(), path.getMetadata(), "null", "post_file");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPostFile(PathMetadata<?> metadata) {
        super(PostFile.class, metadata, "null", "post_file");
    }

}

