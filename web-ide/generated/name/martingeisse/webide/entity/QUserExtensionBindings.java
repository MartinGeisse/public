/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QUserExtensionBindings is a Querydsl query type for UserExtensionBindings
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QUserExtensionBindings extends com.mysema.query.sql.RelationalPathBase<UserExtensionBindings> {

    private static final long serialVersionUID = 786053177;

    /**
     * The default instance of this class.
     */
    public static final QUserExtensionBindings userExtensionBindings = new QUserExtensionBindings("user_extension_bindings");

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
    public final com.mysema.query.sql.PrimaryKey<UserExtensionBindings> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'user_extension_bindings_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<DeclaredExtensionPoints> fk_userExtensionBindingsIbfk2 = createForeignKey(declaredExtensionPointId, "id");

    /**
     * Metamodel property for foreign key 'user_extension_bindings_ibfk_3'
     */
    public final com.mysema.query.sql.ForeignKey<DeclaredExtensions> fk_userExtensionBindingsIbfk3 = createForeignKey(declaredExtensionId, "id");

    /**
     * Metamodel property for foreign key 'user_extension_bindings_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Users> fk_userExtensionBindingsIbfk1 = createForeignKey(userId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QUserExtensionBindings(String variable) {
        super(UserExtensionBindings.class, forVariable(variable), "null", "user_extension_bindings");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QUserExtensionBindings(Path<? extends UserExtensionBindings> path) {
        super(path.getType(), path.getMetadata(), "null", "user_extension_bindings");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QUserExtensionBindings(PathMetadata<?> metadata) {
        super(UserExtensionBindings.class, metadata, "null", "user_extension_bindings");
    }

}

