package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumPmMessages is a Querydsl query type for PhorumPmMessages
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumPmMessages extends com.mysema.query.sql.RelationalPathBase<PhorumPmMessages> {

    private static final long serialVersionUID = 1444881271;

    public static final QPhorumPmMessages phorumPmMessages = new QPhorumPmMessages("phorum_pm_messages");

    public final StringPath author = createString("author");

    public final NumberPath<Integer> datestamp = createNumber("datestamp", Integer.class);

    public final StringPath message = createString("message");

    public final StringPath meta = createString("meta");

    public final NumberPath<Integer> pmMessageId = createNumber("pm_message_id", Integer.class);

    public final StringPath subject = createString("subject");

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumPmMessages> primary = createPrimaryKey(pmMessageId);

    public QPhorumPmMessages(String variable) {
        super(PhorumPmMessages.class, forVariable(variable), "null", "phorum_pm_messages");
    }

    public QPhorumPmMessages(Path<? extends PhorumPmMessages> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_pm_messages");
    }

    public QPhorumPmMessages(PathMetadata<?> metadata) {
        super(PhorumPmMessages.class, metadata, "null", "phorum_pm_messages");
    }

}

