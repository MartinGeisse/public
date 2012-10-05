/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumPmXref is a Querydsl query type for PhorumPmXref
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumPmXref extends com.mysema.query.sql.RelationalPathBase<PhorumPmXref> {

    private static final long serialVersionUID = -1073204026;

    /**
     * The default instance of this class.
     */
    public static final QPhorumPmXref phorumPmXref = new QPhorumPmXref("phorum_pm_xref");

    /**
     * Metamodel property for property 'pm_folder_id'
     */
    public final NumberPath<Integer> pmFolderId = createNumber("pm_folder_id", Integer.class);

    /**
     * Metamodel property for property 'pm_message_id'
     */
    public final NumberPath<Integer> pmMessageId = createNumber("pm_message_id", Integer.class);

    /**
     * Metamodel property for property 'pm_xref_id'
     */
    public final NumberPath<Integer> pmXrefId = createNumber("pm_xref_id", Integer.class);

    /**
     * Metamodel property for property 'read_flag'
     */
    public final BooleanPath readFlag = createBoolean("read_flag");

    /**
     * Metamodel property for property 'reply_flag'
     */
    public final BooleanPath replyFlag = createBoolean("reply_flag");

    /**
     * Metamodel property for property 'special_folder'
     */
    public final StringPath specialFolder = createString("special_folder");

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumPmXref> primary = createPrimaryKey(pmXrefId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumPmXref(String variable) {
        super(PhorumPmXref.class, forVariable(variable), "null", "phorum_pm_xref");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumPmXref(Path<? extends PhorumPmXref> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_pm_xref");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumPmXref(PathMetadata<?> metadata) {
        super(PhorumPmXref.class, metadata, "null", "phorum_pm_xref");
    }

}

