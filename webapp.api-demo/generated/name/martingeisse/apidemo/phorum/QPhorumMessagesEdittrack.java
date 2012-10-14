/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumMessagesEdittrack is a Querydsl query type for PhorumMessagesEdittrack
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumMessagesEdittrack extends com.mysema.query.sql.RelationalPathBase<PhorumMessagesEdittrack> {

    private static final long serialVersionUID = 175634670;

    /**
     * The default instance of this class.
     */
    public static final QPhorumMessagesEdittrack phorumMessagesEdittrack = new QPhorumMessagesEdittrack("phorum_messages_edittrack");

    /**
     * Metamodel property for property 'diff_body'
     */
    public final StringPath diffBody = createString("diff_body");

    /**
     * Metamodel property for property 'diff_subject'
     */
    public final StringPath diffSubject = createString("diff_subject");

    /**
     * Metamodel property for property 'message_id'
     */
    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    /**
     * Metamodel property for property 'time'
     */
    public final NumberPath<Integer> time = createNumber("time", Integer.class);

    /**
     * Metamodel property for property 'track_id'
     */
    public final NumberPath<Integer> trackId = createNumber("track_id", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumMessagesEdittrack> primary = createPrimaryKey(trackId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumMessagesEdittrack(String variable) {
        super(PhorumMessagesEdittrack.class, forVariable(variable), "null", "phorum_messages_edittrack");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumMessagesEdittrack(Path<? extends PhorumMessagesEdittrack> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_messages_edittrack");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumMessagesEdittrack(PathMetadata<?> metadata) {
        super(PhorumMessagesEdittrack.class, metadata, "null", "phorum_messages_edittrack");
    }

}

