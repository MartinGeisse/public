package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumMessages is a Querydsl query type for PhorumMessages
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumMessages extends com.mysema.query.sql.RelationalPathBase<PhorumMessages> {

    private static final long serialVersionUID = 773319130;

    public static final QPhorumMessages phorumMessages = new QPhorumMessages("phorum_messages");

    public final StringPath author = createString("author");

    public final StringPath body = createString("body");

    public final BooleanPath closed = createBoolean("closed");

    public final NumberPath<Integer> datestamp = createNumber("datestamp", Integer.class);

    public final StringPath email = createString("email");

    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    public final StringPath ip = createString("ip");

    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    public final StringPath meta = createString("meta");

    public final BooleanPath moderatorPost = createBoolean("moderator_post");

    public final NumberPath<Integer> modifystamp = createNumber("modifystamp", Integer.class);

    public final BooleanPath moved = createBoolean("moved");

    public final StringPath msgid = createString("msgid");

    public final NumberPath<Integer> parentId = createNumber("parent_id", Integer.class);

    public final StringPath recentAuthor = createString("recent_author");

    public final NumberPath<Integer> recentMessageId = createNumber("recent_message_id", Integer.class);

    public final NumberPath<Integer> recentUserId = createNumber("recent_user_id", Integer.class);

    public final NumberPath<Byte> sort = createNumber("sort", Byte.class);

    public final NumberPath<Byte> status = createNumber("status", Byte.class);

    public final StringPath subject = createString("subject");

    public final NumberPath<Integer> thread = createNumber("thread", Integer.class);

    public final NumberPath<Integer> threadCount = createNumber("thread_count", Integer.class);

    public final NumberPath<Integer> threadviewcount = createNumber("threadviewcount", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final NumberPath<Integer> viewcount = createNumber("viewcount", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumMessages> primary = createPrimaryKey(messageId);

    public QPhorumMessages(String variable) {
        super(PhorumMessages.class, forVariable(variable), "null", "phorum_messages");
    }

    public QPhorumMessages(Path<? extends PhorumMessages> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_messages");
    }

    public QPhorumMessages(PathMetadata<?> metadata) {
        super(PhorumMessages.class, metadata, "null", "phorum_messages");
    }

}

