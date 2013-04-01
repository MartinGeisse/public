/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceExtensionBindings is a Querydsl query type for WorkspaceExtensionBindings
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceExtensionBindings extends com.mysema.query.sql.RelationalPathBase<WorkspaceExtensionBindings> {

    private static final long serialVersionUID = -1597959839;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceExtensionBindings workspaceExtensionBindings = new QWorkspaceExtensionBindings("workspace_extension_bindings");

    /**
     * Metamodel property for property 'anchor_path'
     */
    public final StringPath anchorPath = createString("anchor_path");

    /**
     * Metamodel property for property 'declared_extension_id'
     */
    public final NumberPath<Long> declaredExtensionId = createNumber("declared_extension_id", Long.class);

    /**
     * Metamodel property for property 'declared_extension_point_id'
     */
    public final NumberPath<Long> declaredExtensionPointId = createNumber("declared_extension_point_id", Long.class);

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
    public final com.mysema.query.sql.PrimaryKey<WorkspaceExtensionBindings> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_extension_bindings_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_workspaceExtensionBindingsIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Metamodel property for foreign key 'workspace_extension_bindings_ibfk_3'
     */
    public final com.mysema.query.sql.ForeignKey<DeclaredExtensions> fk_workspaceExtensionBindingsIbfk3 = createForeignKey(declaredExtensionId, "id");

    /**
     * Metamodel property for foreign key 'workspace_extension_bindings_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<DeclaredExtensionPoints> fk_workspaceExtensionBindingsIbfk2 = createForeignKey(declaredExtensionPointId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceExtensionBindings(String variable) {
        super(WorkspaceExtensionBindings.class, forVariable(variable), "null", "workspace_extension_bindings");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceExtensionBindings(Path<? extends WorkspaceExtensionBindings> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_extension_bindings");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceExtensionBindings(PathMetadata<?> metadata) {
        super(WorkspaceExtensionBindings.class, metadata, "null", "workspace_extension_bindings");
    }

}

