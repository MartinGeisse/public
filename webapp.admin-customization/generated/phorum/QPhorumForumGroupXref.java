package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumForumGroupXref is a Querydsl query type for PhorumForumGroupXref
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumForumGroupXref extends com.mysema.query.sql.RelationalPathBase<PhorumForumGroupXref> {

    private static final long serialVersionUID = 179168807;

    public static final QPhorumForumGroupXref phorumForumGroupXref = new QPhorumForumGroupXref("phorum_forum_group_xref");

    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    public final NumberPath<Integer> groupId = createNumber("group_id", Integer.class);

    public final NumberPath<Integer> permission = createNumber("permission", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumForumGroupXref> primary = createPrimaryKey(forumId, groupId);

    public QPhorumForumGroupXref(String variable) {
        super(PhorumForumGroupXref.class, forVariable(variable), "null", "phorum_forum_group_xref");
    }

    public QPhorumForumGroupXref(Path<? extends PhorumForumGroupXref> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_forum_group_xref");
    }

    public QPhorumForumGroupXref(PathMetadata<?> metadata) {
        super(PhorumForumGroupXref.class, metadata, "null", "phorum_forum_group_xref");
    }

}

