/**
 * This file was generated from the database schema.
 */
package phorum;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPhorumSettings is a Querydsl query type for PhorumSettings
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPhorumSettings extends com.mysema.query.sql.RelationalPathBase<PhorumSettings> {

    private static final long serialVersionUID = -1624922959;

    /**
     * The default instance of this class.
     */
    public static final QPhorumSettings phorumSettings = new QPhorumSettings("phorum_settings");

    /**
     * Metamodel property for property 'data'
     */
    public final StringPath data = createString("data");

    /**
     * Metamodel property for property 'group_id'
     */
    public final NumberPath<Integer> groupId = createNumber("group_id", Integer.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for property 'type'
     */
    public final StringPath type = createString("type");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PhorumSettings> pk_primary = createPrimaryKey(name);

    /**
     * Metamodel property for foreign key 'phorum_settings_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PhorumSettingsGroups> fk_phorumSettingsIbfk1 = createForeignKey(groupId, "id");

    /**
     * Metamodel property for reverse foreign key 'phorum_settings_notes_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PhorumSettingsNotes> fk__phorumSettingsNotesIbfk1 = createInvForeignKey(name, "setting_name");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPhorumSettings(String variable) {
        super(PhorumSettings.class, forVariable(variable), "null", "phorum_settings");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPhorumSettings(Path<? extends PhorumSettings> path) {
        super(path.getType(), path.getMetadata(), "null", "phorum_settings");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPhorumSettings(PathMetadata<?> metadata) {
        super(PhorumSettings.class, metadata, "null", "phorum_settings");
    }

}

