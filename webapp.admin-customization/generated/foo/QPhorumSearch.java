package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumSearch is a Querydsl query type for PhorumSearch
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumSearch extends com.mysema.query.sql.RelationalPathBase<PhorumSearch> {

    private static final long serialVersionUID = 184870275;

    public static final QPhorumSearch phorumSearch = new QPhorumSearch("phorum_search");

    public final DatePath<java.sql.Date> dateTest = createDate("date_test", java.sql.Date.class);

    public final DateTimePath<java.sql.Timestamp> datetimeTest = createDateTime("datetime_test", java.sql.Timestamp.class);

    public final NumberPath<Integer> forumId = createNumber("forum_id", Integer.class);

    public final NumberPath<Integer> messageId = createNumber("message_id", Integer.class);

    public final StringPath searchText = createString("search_text");

    public final com.mysema.query.sql.PrimaryKey<PhorumSearch> primary = createPrimaryKey(messageId);

    public QPhorumSearch(String variable) {
        super(PhorumSearch.class, forVariable(variable), "null", "phorum_search");
    }

    public QPhorumSearch(Path<? extends PhorumSearch> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_search");
    }

    public QPhorumSearch(PathMetadata<?> metadata) {
        super(PhorumSearch.class, metadata, "null", "phorum_search");
    }

}

