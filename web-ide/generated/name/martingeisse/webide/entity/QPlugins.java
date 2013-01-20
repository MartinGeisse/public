/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPlugins is a Querydsl query type for Plugins
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPlugins extends com.mysema.query.sql.RelationalPathBase<Plugins> {

    private static final long serialVersionUID = 957344951;

    /**
     * The default instance of this class.
     */
    public static final QPlugins plugins = new QPlugins("plugins");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'is_unpacked'
     */
    public final BooleanPath isUnpacked = createBoolean("is_unpacked");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Plugins> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'workspace_staging_plugins_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceStagingPlugins> fk__workspaceStagingPluginsIbfk2 = createInvForeignKey(id, "plugin_id");

    /**
     * Metamodel property for reverse foreign key 'plugin_bundles_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PluginBundles> fk__pluginBundlesIbfk1 = createInvForeignKey(id, "plugin_id");

    /**
     * Metamodel property for reverse foreign key 'user_plugins_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<UserInstalledPlugins> fk__userPluginsIbfk2 = createInvForeignKey(id, "plugin_id");

    /**
     * Metamodel property for reverse foreign key 'builtin_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<BuiltinPlugins> fk__builtinPluginsIbfk1 = createInvForeignKey(id, "plugin_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPlugins(String variable) {
        super(Plugins.class, forVariable(variable), "null", "plugins");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPlugins(Path<? extends Plugins> path) {
        super(path.getType(), path.getMetadata(), "null", "plugins");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPlugins(PathMetadata<?> metadata) {
        super(Plugins.class, metadata, "null", "plugins");
    }

}

