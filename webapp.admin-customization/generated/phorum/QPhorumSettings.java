package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumSettings is a Querydsl query type for PhorumSettings
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumSettings extends com.mysema.query.sql.RelationalPathBase<PhorumSettings> {

    private static final long serialVersionUID = -1624922959;

    public static final QPhorumSettings phorumSettings = new QPhorumSettings("phorum_settings");

    public final StringPath data = createString("data");

    public final NumberPath<Integer> groupId = createNumber("group_id", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath type = createString("type");

    public final com.mysema.query.sql.PrimaryKey<PhorumSettings> primary = createPrimaryKey(name);

    public final com.mysema.query.sql.ForeignKey<PhorumSettingsGroups> phorumSettingsIbfk1 = createForeignKey(groupId, "id");

    public final com.mysema.query.sql.ForeignKey<PhorumSettingsNotes> _phorumSettingsNotesIbfk1 = createInvForeignKey(name, "setting_name");

    public QPhorumSettings(String variable) {
        super(PhorumSettings.class, forVariable(variable), "null", "phorum_settings");
    }

    public QPhorumSettings(Path<? extends PhorumSettings> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_settings");
    }

    public QPhorumSettings(PathMetadata<?> metadata) {
        super(PhorumSettings.class, metadata, "null", "phorum_settings");
    }

}

