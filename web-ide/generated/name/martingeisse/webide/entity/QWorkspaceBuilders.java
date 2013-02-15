/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceBuilders is a Querydsl query type for WorkspaceBuilders
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceBuilders extends com.mysema.query.sql.RelationalPathBase<WorkspaceBuilders> {

    private static final long serialVersionUID = -1740884924;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceBuilders workspaceBuilders = new QWorkspaceBuilders("workspace_builders");

    /**
     * Metamodel property for property 'builder_class'
     */
    public final StringPath builderClass = createString("builder_class");

    /**
     * Metamodel property for property 'builder_name'
     */
    public final StringPath builderName = createString("builder_name");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'plugin_bundle_id'
     */
    public final NumberPath<Long> pluginBundleId = createNumber("plugin_bundle_id", Long.class);

    /**
     * Metamodel property for property 'staging_path'
     */
    public final StringPath stagingPath = createString("staging_path");

    /**
     * Metamodel property for property 'workspace_id'
     */
    public final NumberPath<Long> workspaceId = createNumber("workspace_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<WorkspaceBuilders> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_builders_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_workspaceBuildersIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Metamodel property for foreign key 'workspace_builders_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<PluginBundles> fk_workspaceBuildersIbfk2 = createForeignKey(pluginBundleId, "id");

    /**
     * Metamodel property for reverse foreign key 'workspace_build_triggers_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceBuildTriggers> fk__workspaceBuildTriggersIbfk2 = createInvForeignKey(id, "workspace_builder_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceBuilders(String variable) {
        super(WorkspaceBuilders.class, forVariable(variable), "null", "workspace_builders");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceBuilders(Path<? extends WorkspaceBuilders> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_builders");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceBuilders(PathMetadata<?> metadata) {
        super(WorkspaceBuilders.class, metadata, "null", "workspace_builders");
    }

}

