package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumUsers is a Querydsl query type for PhorumUsers
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumUsers extends com.mysema.query.sql.RelationalPathBase<PhorumUsers> {

    private static final long serialVersionUID = -29810630;

    public static final QPhorumUsers phorumUsers = new QPhorumUsers("phorum_users");

    public final BooleanPath active = createBoolean("active");

    public final BooleanPath admin = createBoolean("admin");

    public final NumberPath<Integer> dateAdded = createNumber("date_added", Integer.class);

    public final NumberPath<Integer> dateLastActive = createNumber("date_last_active", Integer.class);

    public final StringPath displayName = createString("display_name");

    public final StringPath email = createString("email");

    public final BooleanPath emailNotify = createBoolean("email_notify");

    public final StringPath emailTemp = createString("email_temp");

    public final BooleanPath hideActivity = createBoolean("hide_activity");

    public final BooleanPath hideEmail = createBoolean("hide_email");

    public final BooleanPath isDst = createBoolean("is_dst");

    public final NumberPath<Integer> lastActiveForum = createNumber("last_active_forum", Integer.class);

    public final BooleanPath moderationEmail = createBoolean("moderation_email");

    public final StringPath moderatorData = createString("moderator_data");

    public final StringPath password = createString("password");

    public final StringPath passwordTemp = createString("password_temp");

    public final BooleanPath pmEmailNotify = createBoolean("pm_email_notify");

    public final NumberPath<Integer> posts = createNumber("posts", Integer.class);

    public final StringPath realName = createString("real_name");

    public final StringPath sessidLt = createString("sessid_lt");

    public final StringPath sessidSt = createString("sessid_st");

    public final NumberPath<Integer> sessidStTimeout = createNumber("sessid_st_timeout", Integer.class);

    public final StringPath settingsData = createString("settings_data");

    public final BooleanPath showSignature = createBoolean("show_signature");

    public final StringPath signature = createString("signature");

    public final BooleanPath threadedList = createBoolean("threaded_list");

    public final BooleanPath threadedRead = createBoolean("threaded_read");

    public final NumberPath<Float> tzOffset = createNumber("tz_offset", Float.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final StringPath userLanguage = createString("user_language");

    public final StringPath userTemplate = createString("user_template");

    public final StringPath username = createString("username");

    public final com.mysema.query.sql.PrimaryKey<PhorumUsers> primary = createPrimaryKey(userId);

    public QPhorumUsers(String variable) {
        super(PhorumUsers.class, forVariable(variable), "null", "phorum_users");
    }

    public QPhorumUsers(Path<? extends PhorumUsers> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_users");
    }

    public QPhorumUsers(PathMetadata<?> metadata) {
        super(PhorumUsers.class, metadata, "null", "phorum_users");
    }

}

