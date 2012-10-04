package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumBanlists is a Querydsl query type for PhorumBanlists
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumBanlists extends com.mysema.query.sql.RelationalPathBase<PhorumBanlists> {

    private static final long serialVersionUID = -622132876;

    public static final QPhorumBanlists phorumBanlists = new QPhorumBanlists("phorum_banlists");

    public final StringPath comments = createString("comments");

    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath pcre = createBoolean("pcre");

    public final StringPath string = createString("string");

    public final NumberPath<Byte> type = createNumber("type", Byte.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumBanlists> primary = createPrimaryKey(id);

    public QPhorumBanlists(String variable) {
        super(PhorumBanlists.class, forVariable(variable), "null", "phorum_banlists");
    }

    public QPhorumBanlists(Path<? extends PhorumBanlists> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_banlists");
    }

    public QPhorumBanlists(PathMetadata<?> metadata) {
        super(PhorumBanlists.class, metadata, "null", "phorum_banlists");
    }

}

