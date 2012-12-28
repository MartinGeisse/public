/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QUserPlugins is a Querydsl query type for UserPlugins
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QUserPlugins extends com.mysema.query.sql.RelationalPathBase<UserPlugins> {

    private static final long serialVersionUID = -1131189524;

    /**
     * The default instance of this class.
     */
    public static final QUserPlugins userPlugins = new QUserPlugins("user_plugins");

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
    public final com.mysema.query.sql.PrimaryKey<UserPlugins> pk_primary = createPrimaryKey(id);

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
    public QUserPlugins(String variable) {
        super(UserPlugins.class, forVariable(variable), "null", "user_plugins");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QUserPlugins(Path<? extends UserPlugins> path) {
        super(path.getType(), path.getMetadata(), "null", "user_plugins");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QUserPlugins(PathMetadata<?> metadata) {
        super(UserPlugins.class, metadata, "null", "user_plugins");
    }

}

