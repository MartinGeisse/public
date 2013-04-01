/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceExtensionNetworks is a Querydsl query type for WorkspaceExtensionNetworks
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceExtensionNetworks extends com.mysema.query.sql.RelationalPathBase<WorkspaceExtensionNetworks> {

    private static final long serialVersionUID = -1224615208;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceExtensionNetworks workspaceExtensionNetworks = new QWorkspaceExtensionNetworks("workspace_extension_networks");

    /**
     * Metamodel property for property 'anchor_path'
     */
    public final StringPath anchorPath = createString("anchor_path");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'workspace_id'
     */
    public final NumberPath<Long> workspaceId = createNumber("workspace_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<WorkspaceExtensionNetworks> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_extension_networks_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_workspaceExtensionNetworksIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Metamodel property for reverse foreign key 'workspace_extension_bindings_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceExtensionBindings> fk__workspaceExtensionBindingsIbfk1 = createInvForeignKey(id, "workspace_extension_network_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceExtensionNetworks(String variable) {
        super(WorkspaceExtensionNetworks.class, forVariable(variable), "null", "workspace_extension_networks");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceExtensionNetworks(Path<? extends WorkspaceExtensionNetworks> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_extension_networks");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceExtensionNetworks(PathMetadata<?> metadata) {
        super(WorkspaceExtensionNetworks.class, metadata, "null", "workspace_extension_networks");
    }

}

