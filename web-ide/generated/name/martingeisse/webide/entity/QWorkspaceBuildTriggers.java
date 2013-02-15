/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceBuildTriggers is a Querydsl query type for WorkspaceBuildTriggers
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceBuildTriggers extends com.mysema.query.sql.RelationalPathBase<WorkspaceBuildTriggers> {

    private static final long serialVersionUID = 1814377917;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceBuildTriggers workspaceBuildTriggers = new QWorkspaceBuildTriggers("workspace_build_triggers");

    /**
     * Metamodel property for property 'buildscript_path'
     */
    public final StringPath buildscriptPath = createString("buildscript_path");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'path_pattern'
     */
    public final StringPath pathPattern = createString("path_pattern");

    /**
     * Metamodel property for property 'trigger_base_path'
     */
    public final StringPath triggerBasePath = createString("trigger_base_path");

    /**
     * Metamodel property for property 'workspace_builder_id'
     */
    public final NumberPath<Long> workspaceBuilderId = createNumber("workspace_builder_id", Long.class);

    /**
     * Metamodel property for property 'workspace_id'
     */
    public final NumberPath<Long> workspaceId = createNumber("workspace_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<WorkspaceBuildTriggers> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_build_triggers_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_workspaceBuildTriggersIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Metamodel property for foreign key 'workspace_build_triggers_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceBuilders> fk_workspaceBuildTriggersIbfk2 = createForeignKey(workspaceBuilderId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceBuildTriggers(String variable) {
        super(WorkspaceBuildTriggers.class, forVariable(variable), "null", "workspace_build_triggers");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceBuildTriggers(Path<? extends WorkspaceBuildTriggers> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_build_triggers");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceBuildTriggers(PathMetadata<?> metadata) {
        super(WorkspaceBuildTriggers.class, metadata, "null", "workspace_build_triggers");
    }

}

