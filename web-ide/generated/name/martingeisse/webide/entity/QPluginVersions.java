/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPluginVersions is a Querydsl query type for PluginVersions
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPluginVersions extends com.mysema.query.sql.RelationalPathBase<PluginVersions> {

    private static final long serialVersionUID = 46621175;

    /**
     * The default instance of this class.
     */
    public static final QPluginVersions pluginVersions = new QPluginVersions("plugin_versions");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'is_active'
     */
    public final BooleanPath isActive = createBoolean("is_active");

    /**
     * Metamodel property for property 'is_unpacked'
     */
    public final BooleanPath isUnpacked = createBoolean("is_unpacked");

    /**
     * Metamodel property for property 'plugin_public_id'
     */
    public final StringPath pluginPublicId = createString("plugin_public_id");

    /**
     * Metamodel property for property 'staging_workspace_id'
     */
    public final NumberPath<Long> stagingWorkspaceId = createNumber("staging_workspace_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PluginVersions> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'plugin_versions_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_pluginVersionsIbfk1 = createForeignKey(stagingWorkspaceId, "id");

    /**
     * Metamodel property for reverse foreign key 'plugin_bundles_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PluginBundles> fk__pluginBundlesIbfk1 = createInvForeignKey(id, "plugin_version_id");

    /**
     * Metamodel property for reverse foreign key 'builtin_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<BuiltinPlugins> fk__builtinPluginsIbfk1 = createInvForeignKey(id, "plugin_version_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPluginVersions(String variable) {
        super(PluginVersions.class, forVariable(variable), "null", "plugin_versions");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPluginVersions(Path<? extends PluginVersions> path) {
        super(path.getType(), path.getMetadata(), "null", "plugin_versions");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPluginVersions(PathMetadata<?> metadata) {
        super(PluginVersions.class, metadata, "null", "plugin_versions");
    }

}

