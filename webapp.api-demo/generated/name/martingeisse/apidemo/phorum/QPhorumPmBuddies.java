/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumPmBuddies is a Querydsl query type for PhorumPmBuddies
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumPmBuddies extends com.mysema.query.sql.RelationalPathBase<PhorumPmBuddies> {

    private static final long serialVersionUID = 461046144;

    /**
     * The default instance of this class.
     */
    public static final QPhorumPmBuddies phorumPmBuddies = new QPhorumPmBuddies("phorum_pm_buddies");

    /**
     * Metamodel property for property 'buddy_user_id'
     */
    public final NumberPath<Integer> buddyUserId = createNumber("buddy_user_id", Integer.class);

    /**
     * Metamodel property for property 'pm_buddy_id'
     */
    public final NumberPath<Integer> pmBuddyId = createNumber("pm_buddy_id", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumPmBuddies> pk_primary = createPrimaryKey(pmBuddyId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumPmBuddies(String variable) {
        super(PhorumPmBuddies.class, forVariable(variable), "null", "phorum_pm_buddies");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumPmBuddies(Path<? extends PhorumPmBuddies> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_pm_buddies");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumPmBuddies(PathMetadata<?> metadata) {
        super(PhorumPmBuddies.class, metadata, "null", "phorum_pm_buddies");
    }

}

