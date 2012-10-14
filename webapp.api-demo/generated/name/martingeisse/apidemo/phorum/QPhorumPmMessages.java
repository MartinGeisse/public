/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumPmMessages is a Querydsl query type for PhorumPmMessages
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumPmMessages extends com.mysema.query.sql.RelationalPathBase<PhorumPmMessages> {

    private static final long serialVersionUID = -1768497872;

    /**
     * The default instance of this class.
     */
    public static final QPhorumPmMessages phorumPmMessages = new QPhorumPmMessages("phorum_pm_messages");

    /**
     * Metamodel property for property 'author'
     */
    public final StringPath author = createString("author");

    /**
     * Metamodel property for property 'datestamp'
     */
    public final NumberPath<Integer> datestamp = createNumber("datestamp", Integer.class);

    /**
     * Metamodel property for property 'message'
     */
    public final StringPath message = createString("message");

    /**
     * Metamodel property for property 'meta'
     */
    public final StringPath meta = createString("meta");

    /**
     * Metamodel property for property 'pm_message_id'
     */
    public final NumberPath<Integer> pmMessageId = createNumber("pm_message_id", Integer.class);

    /**
     * Metamodel property for property 'subject'
     */
    public final StringPath subject = createString("subject");

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumPmMessages> primary = createPrimaryKey(pmMessageId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumPmMessages(String variable) {
        super(PhorumPmMessages.class, forVariable(variable), "null", "phorum_pm_messages");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumPmMessages(Path<? extends PhorumPmMessages> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_pm_messages");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumPmMessages(PathMetadata<?> metadata) {
        super(PhorumPmMessages.class, metadata, "null", "phorum_pm_messages");
    }

}

