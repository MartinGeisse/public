package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumUserCustomFields is a Querydsl query type for PhorumUserCustomFields
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumUserCustomFields extends com.mysema.query.sql.RelationalPathBase<PhorumUserCustomFields> {

    private static final long serialVersionUID = -1559662832;

    public static final QPhorumUserCustomFields phorumUserCustomFields = new QPhorumUserCustomFields("phorum_user_custom_fields");

    public final StringPath data = createString("data");

    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    public final com.mysema.query.sql.PrimaryKey<PhorumUserCustomFields> primary = createPrimaryKey(type, userId);

    public QPhorumUserCustomFields(String variable) {
        super(PhorumUserCustomFields.class, forVariable(variable), "null", "phorum_user_custom_fields");
    }

    public QPhorumUserCustomFields(Path<? extends PhorumUserCustomFields> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_user_custom_fields");
    }

    public QPhorumUserCustomFields(PathMetadata<?> metadata) {
        super(PhorumUserCustomFields.class, metadata, "null", "phorum_user_custom_fields");
    }

}

