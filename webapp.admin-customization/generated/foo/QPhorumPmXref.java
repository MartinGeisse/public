package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumPmXref is a Querydsl query type for PhorumPmXref
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumPmXref extends com.mysema.query.sql.RelationalPathBase<PhorumPmXref> {

    private static final long serialVersionUID = 106102931;

    public static final QPhorumPmXref phorumPmXref = new QPhorumPmXref("phorum_pm_xref");

    public final NumberPath<Integer> pmFolderId = createNumber("pm_folder_id", Integer.class);

    public final NumberPath<Integer> pmMessageId = createNumber("pm_message_id", Integer.class);

    public final NumberPath<Integer> pmXrefId = createNumber("pm_xref_id", Integer.class);

    public final BooleanPath readFlag = createBoolean("read_flag");

    public final BooleanPath replyFlag = createBoolean("reply_flag");

    public final StringPath specialFolder = createString("special_folder");

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumPmXref> primary = createPrimaryKey(pmXrefId);

    public QPhorumPmXref(String variable) {
        super(PhorumPmXref.class, forVariable(variable), "null", "phorum_pm_xref");
    }

    public QPhorumPmXref(Path<? extends PhorumPmXref> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_pm_xref");
    }

    public QPhorumPmXref(PathMetadata<?> metadata) {
        super(PhorumPmXref.class, metadata, "null", "phorum_pm_xref");
    }

}

