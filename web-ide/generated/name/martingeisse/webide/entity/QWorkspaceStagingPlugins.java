/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceStagingPlugins is a Querydsl query type for WorkspaceStagingPlugins
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceStagingPlugins extends com.mysema.query.sql.RelationalPathBase<WorkspaceStagingPlugins> {

    private static final long serialVersionUID = 1077691889;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceStagingPlugins workspaceStagingPlugins = new QWorkspaceStagingPlugins("workspace_staging_plugins");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'plugin_id'
     */
    public final NumberPath<Long> pluginId = createNumber("plugin_id", Long.class);

    /**
     * Metamodel property for property 'workspace_id'
     */
    public final NumberPath<Long> workspaceId = createNumber("workspace_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<WorkspaceStagingPlugins> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_staging_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_workspaceStagingPluginsIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Metamodel property for foreign key 'workspace_staging_plugins_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<Plugins> fk_workspaceStagingPluginsIbfk2 = createForeignKey(pluginId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceStagingPlugins(String variable) {
        super(WorkspaceStagingPlugins.class, forVariable(variable), "null", "workspace_staging_plugins");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceStagingPlugins(Path<? extends WorkspaceStagingPlugins> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_staging_plugins");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceStagingPlugins(PathMetadata<?> metadata) {
        super(WorkspaceStagingPlugins.class, metadata, "null", "workspace_staging_plugins");
    }

}

