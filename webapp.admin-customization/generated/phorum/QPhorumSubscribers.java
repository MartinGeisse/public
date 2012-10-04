package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumSubscribers is a Querydsl query type for PhorumSubscribers
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumSubscribers extends com.mysema.query.sql.RelationalPathBase<PhorumSubscribers> {

    private static final long serialVersionUID = 1459774909;

    public static final QPhorumSubscribers phorumSubscribers = new QPhorumSubscribers("phorum_subscribers");

    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    public final NumberPath<Byte> subType = createNumber("sub_type", Byte.class);

    public final NumberPath<Integer> thread = createNumber("thread", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumSubscribers> primary = createPrimaryKey(forumId, thread, userId);

    public QPhorumSubscribers(String variable) {
        super(PhorumSubscribers.class, forVariable(variable), "null", "phorum_subscribers");
    }

    public QPhorumSubscribers(Path<? extends PhorumSubscribers> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_subscribers");
    }

    public QPhorumSubscribers(PathMetadata<?> metadata) {
        super(PhorumSubscribers.class, metadata, "null", "phorum_subscribers");
    }

}

