/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import java.util.*;
import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumSettingsGroups is a Querydsl query type for PhorumSettingsGroups
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumSettingsGroups extends com.mysema.query.sql.RelationalPathBase<PhorumSettingsGroups> {

    private static final long serialVersionUID = -2144575003;

    /**
     * The default instance of this class.
     */
    public static final QPhorumSettingsGroups phorumSettingsGroups = new QPhorumSettingsGroups("phorum_settings_groups");

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
    public final com.mysema.query.sql.PrimaryKey<PhorumSettingsGroups> primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'phorum_crossref_test_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PhorumCrossrefTest> _phorumCrossrefTestIbfk1 = createInvForeignKey(Arrays.asList(name, name), Arrays.asList("name", "name"));

    /**
     * Metamodel property for reverse foreign key 'phorum_settings_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PhorumSettings> _phorumSettingsIbfk1 = createInvForeignKey(id, "group_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumSettingsGroups(String variable) {
        super(PhorumSettingsGroups.class, forVariable(variable), "null", "phorum_settings_groups");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumSettingsGroups(Path<? extends PhorumSettingsGroups> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_settings_groups");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumSettingsGroups(PathMetadata<?> metadata) {
        super(PhorumSettingsGroups.class, metadata, "null", "phorum_settings_groups");
    }

}

