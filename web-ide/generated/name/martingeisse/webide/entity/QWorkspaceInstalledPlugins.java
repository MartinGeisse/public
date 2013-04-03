/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QWorkspaceInstalledPlugins is a Querydsl query type for WorkspaceInstalledPlugins
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QWorkspaceInstalledPlugins extends com.mysema.query.sql.RelationalPathBase<WorkspaceInstalledPlugins> {

    private static final long serialVersionUID = -499204398;

    /**
     * The default instance of this class.
     */
    public static final QWorkspaceInstalledPlugins workspaceInstalledPlugins = new QWorkspaceInstalledPlugins("workspace_installed_plugins");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'plugin_public_id'
     */
    public final StringPath pluginPublicId = createString("plugin_public_id");

    /**
     * Metamodel property for property 'workspace_id'
     */
    public final NumberPath<Long> workspaceId = createNumber("workspace_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<WorkspaceInstalledPlugins> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'workspace_installed_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_workspaceInstalledPluginsIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QWorkspaceInstalledPlugins(String variable) {
        super(WorkspaceInstalledPlugins.class, forVariable(variable), "null", "workspace_installed_plugins");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QWorkspaceInstalledPlugins(Path<? extends WorkspaceInstalledPlugins> path) {
        super(path.getType(), path.getMetadata(), "null", "workspace_installed_plugins");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QWorkspaceInstalledPlugins(PathMetadata<?> metadata) {
        super(WorkspaceInstalledPlugins.class, metadata, "null", "workspace_installed_plugins");
    }

}

