/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumSubscribers is a Querydsl query type for PhorumSubscribers
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumSubscribers extends com.mysema.query.sql.RelationalPathBase<PhorumSubscribers> {

    private static final long serialVersionUID = 1459774909;

    /**
     * The default instance of this class.
     */
    public static final QPhorumSubscribers phorumSubscribers = new QPhorumSubscribers("phorum_subscribers");

    /**
     * Metamodel property for property 'forum_id'
     */
    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    /**
     * Metamodel property for property 'sub_type'
     */
    public final NumberPath<Byte> subType = createNumber("sub_type", Byte.class);

    /**
     * Metamodel property for property 'thread'
     */
    public final NumberPath<Integer> thread = createNumber("thread", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumSubscribers> pk_primary = createPrimaryKey(forumId, thread, userId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumSubscribers(String variable) {
        super(PhorumSubscribers.class, forVariable(variable), "null", "phorum_subscribers");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumSubscribers(Path<? extends PhorumSubscribers> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_subscribers");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumSubscribers(PathMetadata<?> metadata) {
        super(PhorumSubscribers.class, metadata, "null", "phorum_subscribers");
    }

}

