/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QExtensionBindings is a Querydsl query type for ExtensionBindings
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QExtensionBindings extends com.mysema.query.sql.RelationalPathBase<ExtensionBindings> {

    private static final long serialVersionUID = -1883031484;

    /**
     * The default instance of this class.
     */
    public static final QExtensionBindings extensionBindings = new QExtensionBindings("extension_bindings");

    /**
     * Metamodel property for property 'declared_extension_id'
     */
    public final NumberPath<Long> declaredExtensionId = createNumber("declared_extension_id", Long.class);

    /**
     * Metamodel property for property 'declared_extension_point_id'
     */
    public final NumberPath<Long> declaredExtensionPointId = createNumber("declared_extension_point_id", Long.class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'user_id'
     */
    public final NumberPath<Long> userId = createNumber("user_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<ExtensionBindings> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'extension_bindings_ibfk_3'
     */
    public final com.mysema.query.sql.ForeignKey<DeclaredExtensions> fk_extensionBindingsIbfk3 = createForeignKey(declaredExtensionId, "id");

    /**
     * Metamodel property for foreign key 'extension_bindings_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<DeclaredExtensionPoints> fk_extensionBindingsIbfk2 = createForeignKey(declaredExtensionPointId, "id");

    /**
     * Metamodel property for foreign key 'extension_bindings_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Users> fk_extensionBindingsIbfk1 = createForeignKey(userId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QExtensionBindings(String variable) {
        super(ExtensionBindings.class, forVariable(variable), "null", "extension_bindings");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QExtensionBindings(Path<? extends ExtensionBindings> path) {
        super(path.getType(), path.getMetadata(), "null", "extension_bindings");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QExtensionBindings(PathMetadata<?> metadata) {
        super(ExtensionBindings.class, metadata, "null", "extension_bindings");
    }

}

