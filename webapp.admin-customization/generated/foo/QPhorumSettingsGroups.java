package foo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;

import java.util.*;


/**
 * QPhorumSettingsGroups is a Querydsl query type for PhorumSettingsGroups
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumSettingsGroups extends com.mysema.query.sql.RelationalPathBase<PhorumSettingsGroups> {

    private static final long serialVersionUID = -615968590;

    public static final QPhorumSettingsGroups phorumSettingsGroups = new QPhorumSettingsGroups("phorum_settings_groups");

    public final StringPath alias = createString("alias");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final com.mysema.query.sql.PrimaryKey<PhorumSettingsGroups> primary = createPrimaryKey(id);

    public final com.mysema.query.sql.ForeignKey<PhorumCrossrefTest> _phorumCrossrefTestIbfk1 = createInvForeignKey(Arrays.asList(name, name), Arrays.asList("name", "name"));

    public final com.mysema.query.sql.ForeignKey<PhorumSettings> _phorumSettingsIbfk1 = createInvForeignKey(id, "group_id");

    public QPhorumSettingsGroups(String variable) {
        super(PhorumSettingsGroups.class, forVariable(variable), "null", "phorum_settings_groups");
    }

    public QPhorumSettingsGroups(Path<? extends PhorumSettingsGroups> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_settings_groups");
    }

    public QPhorumSettingsGroups(PathMetadata<?> metadata) {
        super(PhorumSettingsGroups.class, metadata, "null", "phorum_settings_groups");
    }

}

