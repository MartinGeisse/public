/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumGroups is a Querydsl query type for PhorumGroups
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumGroups extends com.mysema.query.sql.RelationalPathBase<PhorumGroups> {

    private static final long serialVersionUID = -1325560350;

    /**
     * The default instance of this class.
     */
    public static final QPhorumGroups phorumGroups = new QPhorumGroups("phorum_groups");

    /**
     * Metamodel property for property 'group_id'
     */
    public final NumberPath<Integer> groupId = createNumber("group_id", Integer.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for property 'open'
     */
    public final BooleanPath open = createBoolean("open");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumGroups> pk_primary = createPrimaryKey(groupId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumGroups(String variable) {
        super(PhorumGroups.class, forVariable(variable), "null", "phorum_groups");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumGroups(Path<? extends PhorumGroups> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_groups");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumGroups(PathMetadata<?> metadata) {
        super(PhorumGroups.class, metadata, "null", "phorum_groups");
    }

}

