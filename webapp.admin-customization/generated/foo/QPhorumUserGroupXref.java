package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumUserGroupXref is a Querydsl query type for PhorumUserGroupXref
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumUserGroupXref extends com.mysema.query.sql.RelationalPathBase<PhorumUserGroupXref> {

    private static final long serialVersionUID = 256353396;

    public static final QPhorumUserGroupXref phorumUserGroupXref = new QPhorumUserGroupXref("phorum_user_group_xref");

    public final NumberPath<Integer> groupId = createNumber("group_id", Integer.class);

    public final NumberPath<Byte> status = createNumber("status", Byte.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumUserGroupXref> primary = createPrimaryKey(groupId, userId);

    public QPhorumUserGroupXref(String variable) {
        super(PhorumUserGroupXref.class, forVariable(variable), "null", "phorum_user_group_xref");
    }

    public QPhorumUserGroupXref(Path<? extends PhorumUserGroupXref> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_user_group_xref");
    }

    public QPhorumUserGroupXref(PathMetadata<?> metadata) {
        super(PhorumUserGroupXref.class, metadata, "null", "phorum_user_group_xref");
    }

}

