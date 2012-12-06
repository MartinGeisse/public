/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumUserGroupXref is a Querydsl query type for PhorumUserGroupXref
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumUserGroupXref extends com.mysema.query.sql.RelationalPathBase<PhorumUserGroupXref> {

    private static final long serialVersionUID = 1592516833;

    /**
     * The default instance of this class.
     */
    public static final QPhorumUserGroupXref phorumUserGroupXref = new QPhorumUserGroupXref("phorum_user_group_xref");

    /**
     * Metamodel property for property 'group_id'
     */
    public final NumberPath<Integer> groupId = createNumber("group_id", Integer.class);

    /**
     * Metamodel property for property 'status'
     */
    public final NumberPath<Byte> status = createNumber("status", Byte.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumUserGroupXref> pk_primary = createPrimaryKey(groupId, userId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumUserGroupXref(String variable) {
        super(PhorumUserGroupXref.class, forVariable(variable), "null", "phorum_user_group_xref");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumUserGroupXref(Path<? extends PhorumUserGroupXref> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_user_group_xref");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumUserGroupXref(PathMetadata<?> metadata) {
        super(PhorumUserGroupXref.class, metadata, "null", "phorum_user_group_xref");
    }

}

