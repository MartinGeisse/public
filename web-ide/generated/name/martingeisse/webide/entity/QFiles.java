/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QFiles is a Querydsl query type for Files
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QFiles extends com.mysema.query.sql.RelationalPathBase<Files> {

    private static final long serialVersionUID = 2087749966;

    /**
     * The default instance of this class.
     */
    public static final QFiles files = new QFiles("files");

    /**
     * Metamodel property for property 'contents'
     */
    public final SimplePath<byte[]> contents = createSimple("contents", byte[].class);

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
    public final com.mysema.query.sql.PrimaryKey<Files> pk_primary = createPrimaryKey(id);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QFiles(String variable) {
        super(Files.class, forVariable(variable), "null", "files");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QFiles(Path<? extends Files> path) {
        super(path.getType(), path.getMetadata(), "null", "files");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QFiles(PathMetadata<?> metadata) {
        super(Files.class, metadata, "null", "files");
    }

}

