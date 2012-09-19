package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import java.util.*;


/**
 * QPhorumCrossrefTest is a Querydsl query type for PhorumCrossrefTest
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumCrossrefTest extends com.mysema.query.sql.RelationalPathBase<PhorumCrossrefTest> {

    private static final long serialVersionUID = 47218464;

    public static final QPhorumCrossrefTest phorumCrossrefTest = new QPhorumCrossrefTest("phorum_crossref_test");

    public final StringPath alias = createString("alias");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<PhorumCrossrefTest> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<PhorumSettingsGroups> phorumCrossrefTestIbfk1 = createForeignKey(Arrays.asList(name, name), Arrays.asList("name", "name"));

    public QPhorumCrossrefTest(String variable) {
        super(PhorumCrossrefTest.class, forVariable(variable), "null", "phorum_crossref_test");
    }

    public QPhorumCrossrefTest(Path<? extends PhorumCrossrefTest> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_crossref_test");
    }

    public QPhorumCrossrefTest(PathMetadata<?> metadata) {
        super(PhorumCrossrefTest.class, metadata, "null", "phorum_crossref_test");
    }

}

