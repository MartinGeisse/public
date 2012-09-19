package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumGroups is a Querydsl query type for PhorumGroups
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumGroups extends com.mysema.query.sql.RelationalPathBase<PhorumGroups> {

    private static final long serialVersionUID = -146253393;

    public static final QPhorumGroups phorumGroups = new QPhorumGroups("phorum_groups");

    public final NumberPath<Integer> groupId = createNumber("group_id", Integer.class);

    public final StringPath name = createString("name");

    public final BooleanPath open = createBoolean("open");

    public final com.mysema.query.sql.PrimaryKey<PhorumGroups> primary = createPrimaryKey(groupId);

    public QPhorumGroups(String variable) {
        super(PhorumGroups.class, forVariable(variable), "null", "phorum_groups");
    }

    public QPhorumGroups(Path<? extends PhorumGroups> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_groups");
    }

    public QPhorumGroups(PathMetadata<?> metadata) {
        super(PhorumGroups.class, metadata, "null", "phorum_groups");
    }

}

