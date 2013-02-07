/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceTasks is a Querydsl query type for WorkspaceTasks
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceTasks extends com.mysema.query.sql.RelationalPathBase<WorkspaceTasks> {

    private static final long serialVersionUID = -532732158;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceTasks workspaceTasks = new QWorkspaceTasks("workspace_tasks");

    /**
     * Metamodel property for property 'command'
     */
    public final StringPath command = createString("command");

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
    public final com.mysema.query.sql.PrimaryKey<WorkspaceTasks> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_tasks_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_workspaceTasksIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceTasks(String variable) {
        super(WorkspaceTasks.class, forVariable(variable), "null", "workspace_tasks");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceTasks(Path<? extends WorkspaceTasks> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_tasks");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceTasks(PathMetadata<?> metadata) {
        super(WorkspaceTasks.class, metadata, "null", "workspace_tasks");
    }

}

