package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumUserNewflags is a Querydsl query type for PhorumUserNewflags
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumUserNewflags extends com.mysema.query.sql.RelationalPathBase<PhorumUserNewflags> {

    private static final long serialVersionUID = 1855294829;

    public static final QPhorumUserNewflags phorumUserNewflags = new QPhorumUserNewflags("phorum_user_newflags");

    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumUserNewflags> primary = createPrimaryKey(forumId, messageId, userId);

    public QPhorumUserNewflags(String variable) {
        super(PhorumUserNewflags.class, forVariable(variable), "null", "phorum_user_newflags");
    }

    public QPhorumUserNewflags(Path<? extends PhorumUserNewflags> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_user_newflags");
    }

    public QPhorumUserNewflags(PathMetadata<?> metadata) {
        super(PhorumUserNewflags.class, metadata, "null", "phorum_user_newflags");
    }

}

