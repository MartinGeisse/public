/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceResourceDeltas is a Querydsl query type for WorkspaceResourceDeltas
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceResourceDeltas extends com.mysema.query.sql.RelationalPathBase<WorkspaceResourceDeltas> {

    private static final long serialVersionUID = -854286219;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceResourceDeltas workspaceResourceDeltas = new QWorkspaceResourceDeltas("workspace_resource_deltas");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'is_deep'
     */
    public final BooleanPath isDeep = createBoolean("is_deep");

    /**
     * Metamodel property for property 'path'
     */
    public final StringPath path = createString("path");

    /**
     * Metamodel property for property 'workspace_id'
     */
    public final NumberPath<Long> workspaceId = createNumber("workspace_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<WorkspaceResourceDeltas> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_resource_deltas_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_workspaceResourceDeltasIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceResourceDeltas(String variable) {
        super(WorkspaceResourceDeltas.class, forVariable(variable), "null", "workspace_resource_deltas");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceResourceDeltas(Path<? extends WorkspaceResourceDeltas> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_resource_deltas");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceResourceDeltas(PathMetadata<?> metadata) {
        super(WorkspaceResourceDeltas.class, metadata, "null", "workspace_resource_deltas");
    }

}

