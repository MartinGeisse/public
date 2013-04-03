/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QExtensionNetworks is a Querydsl query type for ExtensionNetworks
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QExtensionNetworks extends com.mysema.query.sql.RelationalPathBase<ExtensionNetworks> {

    private static final long serialVersionUID = -1509686853;

    /**
     * The default instance of this class.
     */
    public static final QExtensionNetworks extensionNetworks = new QExtensionNetworks("extension_networks");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

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
    public final com.mysema.query.sql.PrimaryKey<ExtensionNetworks> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'extension_networks_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<Users> fk_extensionNetworksIbfk2 = createForeignKey(userId, "id");

    /**
     * Metamodel property for foreign key 'extension_networks_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Workspaces> fk_extensionNetworksIbfk1 = createForeignKey(workspaceId, "id");

    /**
     * Metamodel property for reverse foreign key 'extension_bindings_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<ExtensionBindings> fk__extensionBindingsIbfk1 = createInvForeignKey(id, "extension_network_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QExtensionNetworks(String variable) {
        super(ExtensionNetworks.class, forVariable(variable), "null", "extension_networks");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QExtensionNetworks(Path<? extends ExtensionNetworks> path) {
        super(path.getType(), path.getMetadata(), "null", "extension_networks");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QExtensionNetworks(PathMetadata<?> metadata) {
        super(ExtensionNetworks.class, metadata, "null", "extension_networks");
    }

}

