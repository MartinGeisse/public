/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceResources is a Querydsl query type for WorkspaceResources
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceResources extends com.mysema.query.sql.RelationalPathBase<WorkspaceResources> {

    private static final long serialVersionUID = 1284542361;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceResources workspaceResources = new QWorkspaceResources("workspace_resources");

    /**
     * Metamodel property for property 'contents'
     */
    public final SimplePath<byte[]> contents = createSimple("contents", byte[].class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for property 'parent_id'
     */
    public final NumberPath<Long> parentId = createNumber("parent_id", Long.class);

    /**
     * Metamodel property for property 'type'
     */
    public final StringPath type = createString("type");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<WorkspaceResources> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_resources_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceResources> fk_workspaceResourcesIbfk1 = createForeignKey(parentId, "id");

    /**
     * Metamodel property for reverse foreign key 'workspace_staging_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceStagingPlugins> fk__workspaceStagingPluginsIbfk1 = createInvForeignKey(id, "workspace_resource_id");

    /**
     * Metamodel property for reverse foreign key 'workspace_resources_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceResources> fk__workspaceResourcesIbfk1 = createInvForeignKey(id, "parent_id");

    /**
     * Metamodel property for reverse foreign key 'markers_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Markers> fk__markersIbfk1 = createInvForeignKey(id, "workspace_resource_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceResources(String variable) {
        super(WorkspaceResources.class, forVariable(variable), "null", "workspace_resources");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceResources(Path<? extends WorkspaceResources> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_resources");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceResources(PathMetadata<?> metadata) {
        super(WorkspaceResources.class, metadata, "null", "workspace_resources");
    }

}

