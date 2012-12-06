/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumForumGroupXref is a Querydsl query type for PhorumForumGroupXref
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumForumGroupXref extends com.mysema.query.sql.RelationalPathBase<PhorumForumGroupXref> {

    private static final long serialVersionUID = 179168807;

    /**
     * The default instance of this class.
     */
    public static final QPhorumForumGroupXref phorumForumGroupXref = new QPhorumForumGroupXref("phorum_forum_group_xref");

    /**
     * Metamodel property for property 'forum_id'
     */
    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    /**
     * Metamodel property for property 'group_id'
     */
    public final NumberPath<Integer> groupId = createNumber("group_id", Integer.class);

    /**
     * Metamodel property for property 'permission'
     */
    public final NumberPath<Integer> permission = createNumber("permission", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumForumGroupXref> pk_primary = createPrimaryKey(forumId, groupId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumForumGroupXref(String variable) {
        super(PhorumForumGroupXref.class, forVariable(variable), "null", "phorum_forum_group_xref");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumForumGroupXref(Path<? extends PhorumForumGroupXref> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_forum_group_xref");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumForumGroupXref(PathMetadata<?> metadata) {
        super(PhorumForumGroupXref.class, metadata, "null", "phorum_forum_group_xref");
    }

}

