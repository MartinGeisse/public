/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumUserCustomFields is a Querydsl query type for PhorumUserCustomFields
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumUserCustomFields extends com.mysema.query.sql.RelationalPathBase<PhorumUserCustomFields> {

    private static final long serialVersionUID = 46598780;

    /**
     * The default instance of this class.
     */
    public static final QPhorumUserCustomFields phorumUserCustomFields = new QPhorumUserCustomFields("phorum_user_custom_fields");

    /**
     * Metamodel property for property 'data'
     */
    public final StringPath data = createString("data");

    /**
     * Metamodel property for property 'type'
     */
    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Integer> userId = createNumber("user_id", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumUserCustomFields> pk_primary = createPrimaryKey(type, userId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumUserCustomFields(String variable) {
        super(PhorumUserCustomFields.class, forVariable(variable), "null", "phorum_user_custom_fields");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumUserCustomFields(Path<? extends PhorumUserCustomFields> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_user_custom_fields");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumUserCustomFields(PathMetadata<?> metadata) {
        super(PhorumUserCustomFields.class, metadata, "null", "phorum_user_custom_fields");
    }

}

