package phorum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import javax.annotation.Generated;


/**
 * QPhorumSettingsNotes is a Querydsl query type for PhorumSettingsNotes
 */
@Generated("com.mysema.query.sql.codegen.MetaDataSerializer")
public class QPhorumSettingsNotes extends com.mysema.query.sql.RelationalPathBase<PhorumSettingsNotes> {

    private static final long serialVersionUID = 491389072;

    public static final QPhorumSettingsNotes phorumSettingsNotes = new QPhorumSettingsNotes("phorum_settings_notes");

    public final StringPath note = createString("note");

    public final StringPath settingName = createString("setting_name");

    public final com.mysema.query.sql.PrimaryKey<PhorumSettingsNotes> primary = createPrimaryKey(settingName);

    public final com.mysema.query.sql.ForeignKey<PhorumSettings> phorumSettingsNotesIbfk1 = createForeignKey(settingName, "name");

    public QPhorumSettingsNotes(String variable) {
        super(PhorumSettingsNotes.class, forVariable(variable), "null", "phorum_settings_notes");
    }

    public QPhorumSettingsNotes(Path<? extends PhorumSettingsNotes> entity) {
        super(entity.getType(), entity.getMetadata(), "null", "phorum_settings_notes");
    }

    public QPhorumSettingsNotes(PathMetadata<?> metadata) {
        super(PhorumSettingsNotes.class, metadata, "null", "phorum_settings_notes");
    }

}

