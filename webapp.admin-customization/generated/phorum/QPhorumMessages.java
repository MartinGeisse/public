/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumMessages is a Querydsl query type for PhorumMessages
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumMessages extends com.mysema.query.sql.RelationalPathBase<PhorumMessages> {

    private static final long serialVersionUID = 773319130;

    /**
     * The default instance of this class.
     */
    public static final QPhorumMessages phorumMessages = new QPhorumMessages("phorum_messages");

    /**
     * Metamodel property for property 'author'
     */
    public final StringPath author = createString("author");

    /**
     * Metamodel property for property 'body'
     */
    public final StringPath body = createString("body");

    /**
     * Metamodel property for property 'closed'
     */
    public final BooleanPath closed = createBoolean("closed");

    /**
     * Metamodel property for property 'datestamp'
     */
    public final NumberPath<Integer> datestamp = createNumber("datestamp", Integer.class);

    /**
     * Metamodel property for property 'email'
     */
    public final StringPath email = createString("email");

    /**
     * Metamodel property for property 'forum_id'
     */
    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    /**
     * Metamodel property for property 'ip'
     */
    public final StringPath ip = createString("ip");

    /**
     * Metamodel property for property 'message_id'
     */
    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    /**
     * Metamodel property for property 'meta'
     */
    public final StringPath meta = createString("meta");

    /**
     * Metamodel property for property 'moderator_post'
     */
    public final BooleanPath moderatorPost = createBoolean("moderator_post");

    /**
     * Metamodel property for property 'modifystamp'
     */
    public final NumberPath<Integer> modifystamp = createNumber("modifystamp", Integer.class);

    /**
     * Metamodel property for property 'moved'
     */
    public final BooleanPath moved = createBoolean("moved");

    /**
     * Metamodel property for property 'msgid'
     */
    public final StringPath msgid = createString("msgid");

    /**
     * Metamodel property for property 'parent_id'
     */
    public final NumberPath<Integer> parentId = createNumber("parent_id", Integer.class);

    /**
     * Metamodel property for property 'recent_author'
     */
    public final StringPath recentAuthor = createString("recent_author");

    /**
     * Metamodel property for property 'recent_message_id'
     */
    public final NumberPath<Integer> recentMessageId = createNumber("recent_message_id", Integer.class);

    /**
     * Metamodel property for property 'recent_user_id'
     */
    public final NumberPath<Integer> recentUserId = createNumber("recent_user_id", Integer.class);

    /**
     * Metamodel property for property 'sort'
     */
    public final NumberPath<Byte> sort = createNumber("sort", Byte.class);

    /**
     * Metamodel property for property 'status'
     */
    public final NumberPath<Byte> status = createNumber("status", Byte.class);

    /**
     * Metamodel property for property 'subject'
     */
    public final StringPath subject = createString("subject");

    /**
     * Metamodel property for property 'thread'
     */
    public final NumberPath<Integer> thread = createNumber("thread", Integer.class);

    /**
     * Metamodel property for property 'thread_count'
     */
    public final NumberPath<Integer> threadCount = createNumber("thread_count", Integer.class);

    /**
     * Metamodel property for property 'threadviewcount'
     */
    public final NumberPath<Integer> threadviewcount = createNumber("threadviewcount", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for property 'viewcount'
     */
    public final NumberPath<Integer> viewcount = createNumber("viewcount", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumMessages> pk_primary = createPrimaryKey(messageId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumMessages(String variable) {
        super(PhorumMessages.class, forVariable(variable), "null", "phorum_messages");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumMessages(Path<? extends PhorumMessages> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_messages");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumMessages(PathMetadata<?> metadata) {
        super(PhorumMessages.class, metadata, "null", "phorum_messages");
    }

}

