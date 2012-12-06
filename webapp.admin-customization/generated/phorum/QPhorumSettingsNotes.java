/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumSettingsNotes is a Querydsl query type for PhorumSettingsNotes
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumSettingsNotes extends com.mysema.query.sql.RelationalPathBase<PhorumSettingsNotes> {

    private static final long serialVersionUID = 491389072;

    /**
     * The default instance of this class.
     */
    public static final QPhorumSettingsNotes phorumSettingsNotes = new QPhorumSettingsNotes("phorum_settings_notes");

    /**
     * Metamodel property for property 'note'
     */
    public final StringPath note = createString("note");

    /**
     * Metamodel property for property 'setting_name'
     */
    public final StringPath settingName = createString("setting_name");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumSettingsNotes> pk_primary = createPrimaryKey(settingName);

    /**
     * Metamodel property for foreign key 'phorum_settings_notes_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PhorumSettings> fk_phorumSettingsNotesIbfk1 = createForeignKey(settingName, "name");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumSettingsNotes(String variable) {
        super(PhorumSettingsNotes.class, forVariable(variable), "null", "phorum_settings_notes");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumSettingsNotes(Path<? extends PhorumSettingsNotes> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_settings_notes");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumSettingsNotes(PathMetadata<?> metadata) {
        super(PhorumSettingsNotes.class, metadata, "null", "phorum_settings_notes");
    }

}

