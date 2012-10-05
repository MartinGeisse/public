/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumUserNewflags is a Querydsl query type for PhorumUserNewflags
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumUserNewflags extends com.mysema.query.sql.RelationalPathBase<PhorumUserNewflags> {

    private static final long serialVersionUID = -1703833760;

    /**
     * The default instance of this class.
     */
    public static final QPhorumUserNewflags phorumUserNewflags = new QPhorumUserNewflags("phorum_user_newflags");

    /**
     * Metamodel property for property 'forum_id'
     */
    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

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
    public final com.mysema.query.sql.PrimaryKey<PhorumUserNewflags> primary = createPrimaryKey(forumId, messageId, userId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumUserNewflags(String variable) {
        super(PhorumUserNewflags.class, forVariable(variable), "null", "phorum_user_newflags");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumUserNewflags(Path<? extends PhorumUserNewflags> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_user_newflags");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumUserNewflags(PathMetadata<?> metadata) {
        super(PhorumUserNewflags.class, metadata, "null", "phorum_user_newflags");
    }

}

