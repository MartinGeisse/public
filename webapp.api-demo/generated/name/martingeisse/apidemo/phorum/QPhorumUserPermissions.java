/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumUserPermissions is a Querydsl query type for PhorumUserPermissions
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumUserPermissions extends com.mysema.query.sql.RelationalPathBase<PhorumUserPermissions> {

    private static final long serialVersionUID = -1081712302;

    /**
     * The default instance of this class.
     */
    public static final QPhorumUserPermissions phorumUserPermissions = new QPhorumUserPermissions("phorum_user_permissions");

    /**
     * Metamodel property for property 'forum_id'
     */
    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    /**
     * Metamodel property for property 'permission'
     */
    public final NumberPath<Integer> permission = createNumber("permission", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumUserPermissions> pk_primary = createPrimaryKey(forumId, userId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumUserPermissions(String variable) {
        super(PhorumUserPermissions.class, forVariable(variable), "null", "phorum_user_permissions");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumUserPermissions(Path<? extends PhorumUserPermissions> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_user_permissions");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumUserPermissions(PathMetadata<?> metadata) {
        super(PhorumUserPermissions.class, metadata, "null", "phorum_user_permissions");
    }

}

