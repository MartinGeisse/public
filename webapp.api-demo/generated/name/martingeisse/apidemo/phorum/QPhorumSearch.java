/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumSearch is a Querydsl query type for PhorumSearch
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumSearch extends com.mysema.query.sql.RelationalPathBase<PhorumSearch> {

    private static final long serialVersionUID = 1811781615;

    /**
     * The default instance of this class.
     */
    public static final QPhorumSearch phorumSearch = new QPhorumSearch("phorum_search");

    /**
     * Metamodel property for property 'date_test'
     */
    public final DatePath<org.joda.time.LocalDate> dateTest = createDate("date_test", org.joda.time.LocalDate.class);

    /**
     * Metamodel property for property 'datetime_test'
     */
    public final DateTimePath<org.joda.time.DateTime> datetimeTest = createDateTime("datetime_test", org.joda.time.DateTime.class);

    /**
     * Metamodel property for property 'forum_id'
     */
    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    /**
     * Metamodel property for property 'message_id'
     */
    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    /**
     * Metamodel property for property 'search_text'
     */
    public final StringPath searchText = createString("search_text");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumSearch> pk_primary = createPrimaryKey(messageId);

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumSearch(String variable) {
        super(PhorumSearch.class, forVariable(variable), "null", "phorum_search");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumSearch(Path<? extends PhorumSearch> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_search");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumSearch(PathMetadata<?> metadata) {
        super(PhorumSearch.class, metadata, "null", "phorum_search");
    }

}

