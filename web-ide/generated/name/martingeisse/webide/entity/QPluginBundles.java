/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPluginBundles is a Querydsl query type for PluginBundles
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPluginBundles extends com.mysema.query.sql.RelationalPathBase<PluginBundles> {

    private static final long serialVersionUID = -1361698475;

    /**
     * The default instance of this class.
     */
    public static final QPluginBundles pluginBundles = new QPluginBundles("plugin_bundles");

    /**
     * Metamodel property for property 'descriptor'
     */
    public final StringPath descriptor = createString("descriptor");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'jarfile'
     */
    public final SimplePath<byte[]> jarfile = createSimple("jarfile", byte[].class);

    /**
     * Metamodel property for property 'plugin_version_id'
     */
    public final NumberPath<Long> pluginVersionId = createNumber("plugin_version_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PluginBundles> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'plugin_bundles_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PluginVersions> fk_pluginBundlesIbfk1 = createForeignKey(pluginVersionId, "id");

    /**
     * Metamodel property for reverse foreign key 'declared_extension_points_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<DeclaredExtensionPoints> fk__declaredExtensionPointsIbfk1 = createInvForeignKey(id, "plugin_bundle_id");

    /**
     * Metamodel property for reverse foreign key 'plugin_bundle_states_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<PluginBundleStates> fk__pluginBundleStatesIbfk2 = createInvForeignKey(id, "plugin_bundle_id");

    /**
     * Metamodel property for reverse foreign key 'declared_extensions_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<DeclaredExtensions> fk__declaredExtensionsIbfk1 = createInvForeignKey(id, "plugin_bundle_id");

    /**
     * Metamodel property for reverse foreign key 'workspace_builders_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceBuilders> fk__workspaceBuildersIbfk2 = createInvForeignKey(id, "plugin_bundle_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPluginBundles(String variable) {
        super(PluginBundles.class, forVariable(variable), "null", "plugin_bundles");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPluginBundles(Path<? extends PluginBundles> path) {
        super(path.getType(), path.getMetadata(), "null", "plugin_bundles");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPluginBundles(PathMetadata<?> metadata) {
        super(PluginBundles.class, metadata, "null", "plugin_bundles");
    }

}

