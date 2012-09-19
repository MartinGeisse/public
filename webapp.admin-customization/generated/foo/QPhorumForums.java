package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumForums is a Querydsl query type for PhorumForums
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumForums extends com.mysema.query.sql.RelationalPathBase<PhorumForums> {

    private static final long serialVersionUID = -177563827;

    public static final QPhorumForums phorumForums = new QPhorumForums("phorum_forums");

    public final BooleanPath active = createBoolean("active");

    public final StringPath allowAttachmentTypes = createString("allow_attachment_types");

    public final BooleanPath allowEmailNotify = createBoolean("allow_email_notify");

    public final NumberPath<Integer> cacheVersion = createNumber("cache_version", Integer.class);

    public final BooleanPath checkDuplicate = createBoolean("check_duplicate");

    public final BooleanPath countViews = createBoolean("count_views");

    public final BooleanPath countViewsPerThread = createBoolean("count_views_per_thread");

    public final StringPath description = createString("description");

    public final BooleanPath displayFixed = createBoolean("display_fixed");

    public final BooleanPath displayIpAddress = createBoolean("display_ip_address");

    public final NumberPath<Integer> displayOrder = createNumber("display_order", Integer.class);

    public final BooleanPath editPost = createBoolean("edit_post");

    public final BooleanPath emailModerators = createBoolean("email_moderators");

    public final BooleanPath floatToTop = createBoolean("float_to_top");

    public final BooleanPath folderFlag = createBoolean("folder_flag");

    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    public final StringPath forumPath = createString("forum_path");

    public final NumberPath<Integer> inheritId = createNumber("inherit_id", Integer.class);

    public final StringPath language = createString("language");

    public final NumberPath<Integer> lastPostTime = createNumber("last_post_time", Integer.class);

    public final NumberPath<Integer> listLengthFlat = createNumber("list_length_flat", Integer.class);

    public final NumberPath<Integer> listLengthThreaded = createNumber("list_length_threaded", Integer.class);

    public final NumberPath<Integer> maxAttachmentSize = createNumber("max_attachment_size", Integer.class);

    public final NumberPath<Integer> maxAttachments = createNumber("max_attachments", Integer.class);

    public final NumberPath<Integer> maxTotalattachmentSize = createNumber("max_totalattachment_size", Integer.class);

    public final NumberPath<Integer> messageCount = createNumber("message_count", Integer.class);

    public final NumberPath<Integer> moderation = createNumber("moderation", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> parentId = createNumber("parent_id", Integer.class);

    public final NumberPath<Integer> pubPerms = createNumber("pub_perms", Integer.class);

    public final NumberPath<Integer> readLength = createNumber("read_length", Integer.class);

    public final NumberPath<Integer> regPerms = createNumber("reg_perms", Integer.class);

    public final BooleanPath reverseThreading = createBoolean("reverse_threading");

    public final NumberPath<Integer> stickyCount = createNumber("sticky_count", Integer.class);

    public final StringPath template = createString("template");

    public final StringPath templateSettings = createString("template_settings");

    public final NumberPath<Integer> threadCount = createNumber("thread_count", Integer.class);

    public final BooleanPath threadedList = createBoolean("threaded_list");

    public final BooleanPath threadedRead = createBoolean("threaded_read");

    public final NumberPath<Integer> vroot = createNumber("vroot", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumForums> primary = createPrimaryKey(forumId);

    public QPhorumForums(String variable) {
        super(PhorumForums.class, forVariable(variable), "null", "phorum_forums");
    }

    public QPhorumForums(Path<? extends PhorumForums> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_forums");
    }

    public QPhorumForums(PathMetadata<?> metadata) {
        super(PhorumForums.class, metadata, "null", "phorum_forums");
    }

}

