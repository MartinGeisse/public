/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import javax.annotation.Generated;

import java.util.*;
import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumCrossrefTest is a Querydsl query type for PhorumCrossrefTest
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumCrossrefTest extends com.mysema.query.sql.RelationalPathBase<PhorumCrossrefTest> {

    private static final long serialVersionUID = 807186572;

    /**
     * The default instance of this class.
     */
    public static final QPhorumCrossrefTest phorumCrossrefTest = new QPhorumCrossrefTest("phorum_crossref_test");

    /**
     * Metamodel property for property 'alias'
     */
    public final StringPath alias = createString("alias");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumCrossrefTest> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'phorum_crossref_test_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PhorumSettingsGroups> fk_phorumCrossrefTestIbfk1 = createForeignKey(Arrays.asList(name, name), Arrays.asList("name", "name"));

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumCrossrefTest(String variable) {
        super(PhorumCrossrefTest.class, forVariable(variable), "null", "phorum_crossref_test");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumCrossrefTest(Path<? extends PhorumCrossrefTest> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_crossref_test");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumCrossrefTest(PathMetadata<?> metadata) {
        super(PhorumCrossrefTest.class, metadata, "null", "phorum_crossref_test");
    }

}

