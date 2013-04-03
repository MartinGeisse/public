/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QUsers is a Querydsl query type for Users
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QUsers extends com.mysema.query.sql.RelationalPathBase<Users> {

    private static final long serialVersionUID = 2101894367;

    /**
     * The default instance of this class.
     */
    public static final QUsers users = new QUsers("users");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'login_name'
     */
    public final StringPath loginName = createString("login_name");

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Users> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'user_installed_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<UserInstalledPlugins> fk__userInstalledPluginsIbfk1 = createInvForeignKey(id, "user_id");

    /**
     * Metamodel property for reverse foreign key 'plugin_bundle_states_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PluginBundleStates> fk__pluginBundleStatesIbfk1 = createInvForeignKey(id, "user_id");

    /**
     * Metamodel property for reverse foreign key 'extension_networks_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<ExtensionNetworks> fk__extensionNetworksIbfk2 = createInvForeignKey(id, "user_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QUsers(String variable) {
        super(Users.class, forVariable(variable), "null", "users");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QUsers(Path<? extends Users> path) {
        super(path.getType(), path.getMetadata(), "null", "users");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QUsers(PathMetadata<?> metadata) {
        super(Users.class, metadata, "null", "users");
    }

}

