package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumUserPermissions is a Querydsl query type for PhorumUserPermissions
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumUserPermissions extends com.mysema.query.sql.RelationalPathBase<PhorumUserPermissions> {

    private static final long serialVersionUID = 1775966782;

    public static final QPhorumUserPermissions phorumUserPermissions = new QPhorumUserPermissions("phorum_user_permissions");

    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    public final NumberPath<Integer> permission = createNumber("permission", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumUserPermissions> primary = createPrimaryKey(forumId, userId);

    public QPhorumUserPermissions(String variable) {
        super(PhorumUserPermissions.class, forVariable(variable), "null", "phorum_user_permissions");
    }

    public QPhorumUserPermissions(Path<? extends PhorumUserPermissions> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_user_permissions");
    }

    public QPhorumUserPermissions(PathMetadata<?> metadata) {
        super(PhorumUserPermissions.class, metadata, "null", "phorum_user_permissions");
    }

}

