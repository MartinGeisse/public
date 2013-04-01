/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QDeclaredExtensionPoints is a Querydsl query type for DeclaredExtensionPoints
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QDeclaredExtensionPoints extends com.mysema.query.sql.RelationalPathBase<DeclaredExtensionPoints> {

    private static final long serialVersionUID = 1635513727;

    /**
     * The default instance of this class.
     */
    public static final QDeclaredExtensionPoints declaredExtensionPoints = new QDeclaredExtensionPoints("declared_extension_points");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for property 'on_change_cleared_section'
     */
    public final NumberPath<Integer> onChangeClearedSection = createNumber("on_change_cleared_section", Integer.class);

    /**
     * Metamodel property for property 'plugin_bundle_id'
     */
    public final NumberPath<Long> pluginBundleId = createNumber("plugin_bundle_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<DeclaredExtensionPoints> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'declared_extension_points_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PluginBundles> fk_declaredExtensionPointsIbfk1 = createForeignKey(pluginBundleId, "id");

    /**
     * Metamodel property for reverse foreign key 'user_extension_bindings_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<UserExtensionBindings> fk__userExtensionBindingsIbfk2 = createInvForeignKey(id, "declared_extension_point_id");

    /**
     * Metamodel property for reverse foreign key 'workspace_extension_bindings_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceExtensionBindings> fk__workspaceExtensionBindingsIbfk2 = createInvForeignKey(id, "declared_extension_point_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QDeclaredExtensionPoints(String variable) {
        super(DeclaredExtensionPoints.class, forVariable(variable), "null", "declared_extension_points");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QDeclaredExtensionPoints(Path<? extends DeclaredExtensionPoints> path) {
        super(path.getType(), path.getMetadata(), "null", "declared_extension_points");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QDeclaredExtensionPoints(PathMetadata<?> metadata) {
        super(DeclaredExtensionPoints.class, metadata, "null", "declared_extension_points");
    }

}

