/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumFiles is a Querydsl query type for PhorumFiles
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumFiles extends com.mysema.query.sql.RelationalPathBase<PhorumFiles> {

    private static final long serialVersionUID = 185115472;

    /**
     * The default instance of this class.
     */
    public static final QPhorumFiles phorumFiles = new QPhorumFiles("phorum_files");

    /**
     * Metamodel property for property 'add_datetime'
     */
    public final NumberPath<Integer> addDatetime = createNumber("add_datetime", Integer.class);

    /**
     * Metamodel property for property 'file_data'
     */
    public final StringPath fileData = createString("file_data");

    /**
     * Metamodel property for property 'file_id'
     */
    public final NumberPath<Integer> fileId = createNumber("file_id", Integer.class);

    /**
     * Metamodel property for property 'filename'
     */
    public final StringPath filename = createString("filename");

    /**
     * Metamodel property for property 'filesize'
     */
    public final NumberPath<Integer> filesize = createNumber("filesize", Integer.class);

    /**
     * Metamodel property for property 'link'
     */
    public final StringPath link = createString("link");

    /**
     * Metamodel property for property 'message_id'
     */
    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumFiles> primary = createPrimaryKey(fileId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumFiles(String variable) {
        super(PhorumFiles.class, forVariable(variable), "null", "phorum_files");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumFiles(Path<? extends PhorumFiles> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_files");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumFiles(PathMetadata<?> metadata) {
        super(PhorumFiles.class, metadata, "null", "phorum_files");
    }

}

