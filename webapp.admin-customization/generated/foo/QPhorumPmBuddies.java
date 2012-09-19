package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumPmBuddies is a Querydsl query type for PhorumPmBuddies
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumPmBuddies extends com.mysema.query.sql.RelationalPathBase<PhorumPmBuddies> {

    private static final long serialVersionUID = 1851251564;

    public static final QPhorumPmBuddies phorumPmBuddies = new QPhorumPmBuddies("phorum_pm_buddies");

    public final NumberPath<Integer> buddyUserId = createNumber("buddy_user_id", Integer.class);

    public final NumberPath<Integer> pmBuddyId = createNumber("pm_buddy_id", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumPmBuddies> primary = createPrimaryKey(pmBuddyId);

    public QPhorumPmBuddies(String variable) {
        super(PhorumPmBuddies.class, forVariable(variable), "null", "phorum_pm_buddies");
    }

    public QPhorumPmBuddies(Path<? extends PhorumPmBuddies> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_pm_buddies");
    }

    public QPhorumPmBuddies(PathMetadata<?> metadata) {
        super(PhorumPmBuddies.class, metadata, "null", "phorum_pm_buddies");
    }

}

