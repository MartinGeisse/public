/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QDeclaredExtensions is a Querydsl query type for DeclaredExtensions
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QDeclaredExtensions extends com.mysema.query.sql.RelationalPathBase<DeclaredExtensions> {

    private static final long serialVersionUID = 155350647;

    /**
     * The default instance of this class.
     */
    public static final QDeclaredExtensions declaredExtensions = new QDeclaredExtensions("declared_extensions");

    /**
     * Metamodel property for property 'descriptor'
     */
    public final StringPath descriptor = createString("descriptor");

    /**
     * Metamodel property for property 'extension_point_name'
     */
    public final StringPath extensionPointName = createString("extension_point_name");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'plugin_bundle_id'
     */
    public final NumberPath<Long> pluginBundleId = createNumber("plugin_bundle_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<DeclaredExtensions> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'declared_extensions_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PluginBundles> fk_declaredExtensionsIbfk1 = createForeignKey(pluginBundleId, "id");

    /**
     * Metamodel property for reverse foreign key 'extension_bindings_ibfk_3'
     */
    public final com.mysema.query.sql.ForeignKey<ExtensionBindings> fk__extensionBindingsIbfk3 = createInvForeignKey(id, "declared_extension_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QDeclaredExtensions(String variable) {
        super(DeclaredExtensions.class, forVariable(variable), "null", "declared_extensions");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QDeclaredExtensions(Path<? extends DeclaredExtensions> path) {
        super(path.getType(), path.getMetadata(), "null", "declared_extensions");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QDeclaredExtensions(PathMetadata<?> metadata) {
        super(DeclaredExtensions.class, metadata, "null", "declared_extensions");
    }

}

