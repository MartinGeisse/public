/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPluginBundleStates is a Querydsl query type for PluginBundleStates
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPluginBundleStates extends com.mysema.query.sql.RelationalPathBase<PluginBundleStates> {

    private static final long serialVersionUID = -673720704;

    /**
     * The default instance of this class.
     */
    public static final QPluginBundleStates pluginBundleStates = new QPluginBundleStates("plugin_bundle_states");

    /**
     * Metamodel property for property 'data'
     */
    public final SimplePath<byte[]> data = createSimple("data", byte[].class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'plugin_bundle_id'
     */
    public final NumberPath<Long> pluginBundleId = createNumber("plugin_bundle_id", Long.class);

    /**
     * Metamodel property for property 'section'
     */
    public final NumberPath<Integer> section = createNumber("section", Integer.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Long> userId = createNumber("user_id", Long.class);

    /**
     * Metamodel property for property 'workspace_id'
     */
    public final NumberPath<Long> workspaceId = createNumber("workspace_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PluginBundleStates> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'plugin_bundle_states_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<PluginBundles> fk_pluginBundleStatesIbfk2 = createForeignKey(pluginBundleId, "id");

    /**
     * Metamodel property for foreign key 'plugin_bundle_states_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Users> fk_pluginBundleStatesIbfk1 = createForeignKey(userId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPluginBundleStates(String variable) {
        super(PluginBundleStates.class, forVariable(variable), "null", "plugin_bundle_states");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPluginBundleStates(Path<? extends PluginBundleStates> path) {
        super(path.getType(), path.getMetadata(), "null", "plugin_bundle_states");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPluginBundleStates(PathMetadata<?> metadata) {
        super(PluginBundleStates.class, metadata, "null", "plugin_bundle_states");
    }

}

