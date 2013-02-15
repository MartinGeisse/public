/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaces is a Querydsl query type for Workspaces
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaces extends com.mysema.query.sql.RelationalPathBase<Workspaces> {

    private static final long serialVersionUID = 2081850887;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaces workspaces = new QWorkspaces("workspaces");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'is_building'
     */
    public final BooleanPath isBuilding = createBoolean("is_building");

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Workspaces> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'workspace_tasks_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceTasks> fk__workspaceTasksIbfk1 = createInvForeignKey(id, "workspace_id");

    /**
     * Metamodel property for reverse foreign key 'workspace_staging_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceStagingPlugins> fk__workspaceStagingPluginsIbfk1 = createInvForeignKey(id, "workspace_id");

    /**
     * Metamodel property for reverse foreign key 'workspace_resource_deltas_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceResourceDeltas> fk__workspaceResourceDeltasIbfk1 = createInvForeignKey(id, "workspace_id");

    /**
     * Metamodel property for reverse foreign key 'workspace_resources_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceResources> fk__workspaceResourcesIbfk1 = createInvForeignKey(id, "workspace_id");

    /**
     * Metamodel property for reverse foreign key 'markers_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Markers> fk__markersIbfk1 = createInvForeignKey(id, "workspace_id");

    /**
     * Metamodel property for reverse foreign key 'workspace_builders_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceBuilders> fk__workspaceBuildersIbfk1 = createInvForeignKey(id, "workspace_id");

    /**
     * Metamodel property for reverse foreign key 'workspace_build_triggers_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceBuildTriggers> fk__workspaceBuildTriggersIbfk1 = createInvForeignKey(id, "workspace_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaces(String variable) {
        super(Workspaces.class, forVariable(variable), "null", "workspaces");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaces(Path<? extends Workspaces> path) {
        super(path.getType(), path.getMetadata(), "null", "workspaces");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaces(PathMetadata<?> metadata) {
        super(Workspaces.class, metadata, "null", "workspaces");
    }

}

