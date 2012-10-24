/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumForums is a Querydsl query type for PhorumForums
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumForums extends com.mysema.query.sql.RelationalPathBase<PhorumForums> {

    private static final long serialVersionUID = 1449347513;

    /**
     * The default instance of this class.
     */
    public static final QPhorumForums phorumForums = new QPhorumForums("phorum_forums");

    /**
     * Metamodel property for property 'active'
     */
    public final BooleanPath active = createBoolean("active");

    /**
     * Metamodel property for property 'allow_attachment_types'
     */
    public final StringPath allowAttachmentTypes = createString("allow_attachment_types");

    /**
     * Metamodel property for property 'allow_email_notify'
     */
    public final BooleanPath allowEmailNotify = createBoolean("allow_email_notify");

    /**
     * Metamodel property for property 'cache_version'
     */
    public final NumberPath<Integer> cacheVersion = createNumber("cache_version", Integer.class);

    /**
     * Metamodel property for property 'check_duplicate'
     */
    public final BooleanPath checkDuplicate = createBoolean("check_duplicate");

    /**
     * Metamodel property for property 'count_views'
     */
    public final BooleanPath countViews = createBoolean("count_views");

    /**
     * Metamodel property for property 'count_views_per_thread'
     */
    public final BooleanPath countViewsPerThread = createBoolean("count_views_per_thread");

    /**
     * Metamodel property for property 'description'
     */
    public final StringPath description = createString("description");

    /**
     * Metamodel property for property 'display_fixed'
     */
    public final BooleanPath displayFixed = createBoolean("display_fixed");

    /**
     * Metamodel property for property 'display_ip_address'
     */
    public final BooleanPath displayIpAddress = createBoolean("display_ip_address");

    /**
     * Metamodel property for property 'display_order'
     */
    public final NumberPath<Integer> displayOrder = createNumber("display_order", Integer.class);

    /**
     * Metamodel property for property 'edit_post'
     */
    public final BooleanPath editPost = createBoolean("edit_post");

    /**
     * Metamodel property for property 'email_moderators'
     */
    public final BooleanPath emailModerators = createBoolean("email_moderators");

    /**
     * Metamodel property for property 'float_to_top'
     */
    public final BooleanPath floatToTop = createBoolean("float_to_top");

    /**
     * Metamodel property for property 'folder_flag'
     */
    public final BooleanPath folderFlag = createBoolean("folder_flag");

    /**
     * Metamodel property for property 'forum_id'
     */
    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    /**
     * Metamodel property for property 'forum_path'
     */
    public final StringPath forumPath = createString("forum_path");

    /**
     * Metamodel property for property 'inherit_id'
     */
    public final NumberPath<Integer> inheritId = createNumber("inherit_id", Integer.class);

    /**
     * Metamodel property for property 'language'
     */
    public final StringPath language = createString("language");

    /**
     * Metamodel property for property 'last_post_time'
     */
    public final NumberPath<Integer> lastPostTime = createNumber("last_post_time", Integer.class);

    /**
     * Metamodel property for property 'list_length_flat'
     */
    public final NumberPath<Integer> listLengthFlat = createNumber("list_length_flat", Integer.class);

    /**
     * Metamodel property for property 'list_length_threaded'
     */
    public final NumberPath<Integer> listLengthThreaded = createNumber("list_length_threaded", Integer.class);

    /**
     * Metamodel property for property 'max_attachment_size'
     */
    public final NumberPath<Integer> maxAttachmentSize = createNumber("max_attachment_size", Integer.class);

    /**
     * Metamodel property for property 'max_attachments'
     */
    public final NumberPath<Integer> maxAttachments = createNumber("max_attachments", Integer.class);

    /**
     * Metamodel property for property 'max_totalattachment_size'
     */
    public final NumberPath<Integer> maxTotalattachmentSize = createNumber("max_totalattachment_size", Integer.class);

    /**
     * Metamodel property for property 'message_count'
     */
    public final NumberPath<Integer> messageCount = createNumber("message_count", Integer.class);

    /**
     * Metamodel property for property 'moderation'
     */
    public final NumberPath<Integer> moderation = createNumber("moderation", Integer.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for property 'parent_id'
     */
    public final NumberPath<Integer> parentId = createNumber("parent_id", Integer.class);

    /**
     * Metamodel property for property 'pub_perms'
     */
    public final NumberPath<Integer> pubPerms = createNumber("pub_perms", Integer.class);

    /**
     * Metamodel property for property 'read_length'
     */
    public final NumberPath<Integer> readLength = createNumber("read_length", Integer.class);

    /**
     * Metamodel property for property 'reg_perms'
     */
    public final NumberPath<Integer> regPerms = createNumber("reg_perms", Integer.class);

    /**
     * Metamodel property for property 'reverse_threading'
     */
    public final BooleanPath reverseThreading = createBoolean("reverse_threading");

    /**
     * Metamodel property for property 'sticky_count'
     */
    public final NumberPath<Integer> stickyCount = createNumber("sticky_count", Integer.class);

    /**
     * Metamodel property for property 'template'
     */
    public final StringPath template = createString("template");

    /**
     * Metamodel property for property 'template_settings'
     */
    public final StringPath templateSettings = createString("template_settings");

    /**
     * Metamodel property for property 'thread_count'
     */
    public final NumberPath<Integer> threadCount = createNumber("thread_count", Integer.class);

    /**
     * Metamodel property for property 'threaded_list'
     */
    public final BooleanPath threadedList = createBoolean("threaded_list");

    /**
     * Metamodel property for property 'threaded_read'
     */
    public final BooleanPath threadedRead = createBoolean("threaded_read");

    /**
     * Metamodel property for property 'vroot'
     */
    public final NumberPath<Integer> vroot = createNumber("vroot", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumForums> pk_primary = createPrimaryKey(forumId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumForums(String variable) {
        super(PhorumForums.class, forVariable(variable), "null", "phorum_forums");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumForums(Path<? extends PhorumForums> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_forums");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumForums(PathMetadata<?> metadata) {
        super(PhorumForums.class, metadata, "null", "phorum_forums");
    }

}

