/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumBanlists is a Querydsl query type for PhorumBanlists
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumBanlists extends com.mysema.query.sql.RelationalPathBase<PhorumBanlists> {

    private static final long serialVersionUID = -1085811347;

    /**
     * The default instance of this class.
     */
    public static final QPhorumBanlists phorumBanlists = new QPhorumBanlists("phorum_banlists");

    /**
     * Metamodel property for property 'comments'
     */
    public final StringPath comments = createString("comments");

    /**
     * Metamodel property for property 'forum_id'
     */
    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    /**
     * Metamodel property for property 'pcre'
     */
    public final BooleanPath pcre = createBoolean("pcre");

    /**
     * Metamodel property for property 'string'
     */
    public final StringPath string = createString("string");

    /**
     * Metamodel property for property 'type'
     */
    public final NumberPath<Byte> type = createNumber("type", Byte.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumBanlists> primary = createPrimaryKey(id);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumBanlists(String variable) {
        super(PhorumBanlists.class, forVariable(variable), "null", "phorum_banlists");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumBanlists(Path<? extends PhorumBanlists> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_banlists");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumBanlists(PathMetadata<?> metadata) {
        super(PhorumBanlists.class, metadata, "null", "phorum_banlists");
    }

}

