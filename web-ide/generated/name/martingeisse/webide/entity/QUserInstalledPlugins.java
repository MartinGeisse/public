/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QUserInstalledPlugins is a Querydsl query type for UserInstalledPlugins
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QUserInstalledPlugins extends com.mysema.query.sql.RelationalPathBase<UserInstalledPlugins> {

    private static final long serialVersionUID = 2071551226;

    /**
     * The default instance of this class.
     */
    public static final QUserInstalledPlugins userInstalledPlugins = new QUserInstalledPlugins("user_installed_plugins");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'plugin_id'
     */
    public final NumberPath<Long> pluginId = createNumber("plugin_id", Long.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Long> userId = createNumber("user_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<UserInstalledPlugins> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'user_plugins_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<Plugins> fk_userPluginsIbfk2 = createForeignKey(pluginId, "id");

    /**
     * Metamodel property for foreign key 'user_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Users> fk_userPluginsIbfk1 = createForeignKey(userId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QUserInstalledPlugins(String variable) {
        super(UserInstalledPlugins.class, forVariable(variable), "null", "user_installed_plugins");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QUserInstalledPlugins(Path<? extends UserInstalledPlugins> path) {
        super(path.getType(), path.getMetadata(), "null", "user_installed_plugins");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QUserInstalledPlugins(PathMetadata<?> metadata) {
        super(UserInstalledPlugins.class, metadata, "null", "user_installed_plugins");
    }

}

