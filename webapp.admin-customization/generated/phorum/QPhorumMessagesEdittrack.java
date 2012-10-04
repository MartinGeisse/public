package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumMessagesEdittrack is a Querydsl query type for PhorumMessagesEdittrack
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumMessagesEdittrack extends com.mysema.query.sql.RelationalPathBase<PhorumMessagesEdittrack> {

    private static final long serialVersionUID = -1549245241;

    public static final QPhorumMessagesEdittrack phorumMessagesEdittrack = new QPhorumMessagesEdittrack("phorum_messages_edittrack");

    public final StringPath diffBody = createString("diff_body");

    public final StringPath diffSubject = createString("diff_subject");

    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    public final NumberPath<Integer> time = createNumber("time", Integer.class);

    public final NumberPath<Integer> trackId = createNumber("track_id", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumMessagesEdittrack> primary = createPrimaryKey(trackId);

    public QPhorumMessagesEdittrack(String variable) {
        super(PhorumMessagesEdittrack.class, forVariable(variable), "null", "phorum_messages_edittrack");
    }

    public QPhorumMessagesEdittrack(Path<? extends PhorumMessagesEdittrack> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_messages_edittrack");
    }

    public QPhorumMessagesEdittrack(PathMetadata<?> metadata) {
        super(PhorumMessagesEdittrack.class, metadata, "null", "phorum_messages_edittrack");
    }

}

