/**
 * This file was generated from the database schema.
 */
package name.martingeisse.hackathon.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QDummy is a Querydsl query type for Dummy
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QDummy extends com.mysema.query.sql.RelationalPathBase<Dummy> {

    private static final long serialVersionUID = -1003319506;

    /**
     * The default instance of this class.
     */
    public static final QDummy dummy = new QDummy("dummy");

    /**
     * Metamodel property for property 'foo'
     */
    public final StringPath foo = createString("foo");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Dummy> pk_primary = createPrimaryKey(id);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QDummy(String variable) {
        super(Dummy.class, forVariable(variable), "null", "dummy");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QDummy(Path<? extends Dummy> path) {
        super(path.getType(), path.getMetadata(), "null", "dummy");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QDummy(PathMetadata<?> metadata) {
        super(Dummy.class, metadata, "null", "dummy");
    }

}

