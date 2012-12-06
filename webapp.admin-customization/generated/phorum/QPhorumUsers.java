/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumUsers is a Querydsl query type for PhorumUsers
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumUsers extends com.mysema.query.sql.RelationalPathBase<PhorumUsers> {

    private static final long serialVersionUID = -29810630;

    /**
     * The default instance of this class.
     */
    public static final QPhorumUsers phorumUsers = new QPhorumUsers("phorum_users");

    /**
     * Metamodel property for property 'active'
     */
    public final BooleanPath active = createBoolean("active");

    /**
     * Metamodel property for property 'admin'
     */
    public final BooleanPath admin = createBoolean("admin");

    /**
     * Metamodel property for property 'date_added'
     */
    public final NumberPath<Integer> dateAdded = createNumber("date_added", Integer.class);

    /**
     * Metamodel property for property 'date_last_active'
     */
    public final NumberPath<Integer> dateLastActive = createNumber("date_last_active", Integer.class);

    /**
     * Metamodel property for property 'display_name'
     */
    public final StringPath displayName = createString("display_name");

    /**
     * Metamodel property for property 'email'
     */
    public final StringPath email = createString("email");

    /**
     * Metamodel property for property 'email_notify'
     */
    public final BooleanPath emailNotify = createBoolean("email_notify");

    /**
     * Metamodel property for property 'email_temp'
     */
    public final StringPath emailTemp = createString("email_temp");

    /**
     * Metamodel property for property 'hide_activity'
     */
    public final BooleanPath hideActivity = createBoolean("hide_activity");

    /**
     * Metamodel property for property 'hide_email'
     */
    public final BooleanPath hideEmail = createBoolean("hide_email");

    /**
     * Metamodel property for property 'is_dst'
     */
    public final BooleanPath isDst = createBoolean("is_dst");

    /**
     * Metamodel property for property 'last_active_forum'
     */
    public final NumberPath<Integer> lastActiveForum = createNumber("last_active_forum", Integer.class);

    /**
     * Metamodel property for property 'moderation_email'
     */
    public final BooleanPath moderationEmail = createBoolean("moderation_email");

    /**
     * Metamodel property for property 'moderator_data'
     */
    public final StringPath moderatorData = createString("moderator_data");

    /**
     * Metamodel property for property 'password'
     */
    public final StringPath password = createString("password");

    /**
     * Metamodel property for property 'password_temp'
     */
    public final StringPath passwordTemp = createString("password_temp");

    /**
     * Metamodel property for property 'pm_email_notify'
     */
    public final BooleanPath pmEmailNotify = createBoolean("pm_email_notify");

    /**
     * Metamodel property for property 'posts'
     */
    public final NumberPath<Integer> posts = createNumber("posts", Integer.class);

    /**
     * Metamodel property for property 'real_name'
     */
    public final StringPath realName = createString("real_name");

    /**
     * Metamodel property for property 'sessid_lt'
     */
    public final StringPath sessidLt = createString("sessid_lt");

    /**
     * Metamodel property for property 'sessid_st'
     */
    public final StringPath sessidSt = createString("sessid_st");

    /**
     * Metamodel property for property 'sessid_st_timeout'
     */
    public final NumberPath<Integer> sessidStTimeout = createNumber("sessid_st_timeout", Integer.class);

    /**
     * Metamodel property for property 'settings_data'
     */
    public final StringPath settingsData = createString("settings_data");

    /**
     * Metamodel property for property 'show_signature'
     */
    public final BooleanPath showSignature = createBoolean("show_signature");

    /**
     * Metamodel property for property 'signature'
     */
    public final StringPath signature = createString("signature");

    /**
     * Metamodel property for property 'threaded_list'
     */
    public final BooleanPath threadedList = createBoolean("threaded_list");

    /**
     * Metamodel property for property 'threaded_read'
     */
    public final BooleanPath threadedRead = createBoolean("threaded_read");

    /**
     * Metamodel property for property 'tz_offset'
     */
    public final NumberPath<Float> tzOffset = createNumber("tz_offset", Float.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for property 'user_language'
     */
    public final StringPath userLanguage = createString("user_language");

    /**
     * Metamodel property for property 'user_template'
     */
    public final StringPath userTemplate = createString("user_template");

    /**
     * Metamodel property for property 'username'
     */
    public final StringPath username = createString("username");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumUsers> pk_primary = createPrimaryKey(userId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumUsers(String variable) {
        super(PhorumUsers.class, forVariable(variable), "null", "phorum_users");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumUsers(Path<? extends PhorumUsers> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_users");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumUsers(PathMetadata<?> metadata) {
        super(PhorumUsers.class, metadata, "null", "phorum_users");
    }

}

