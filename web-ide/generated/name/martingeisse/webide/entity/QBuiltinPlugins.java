/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QBuiltinPlugins is a Querydsl query type for BuiltinPlugins
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QBuiltinPlugins extends com.mysema.query.sql.RelationalPathBase<BuiltinPlugins> {

    private static final long serialVersionUID = -1924237050;

    /**
     * The default instance of this class.
     */
    public static final QBuiltinPlugins builtinPlugins = new QBuiltinPlugins("builtin_plugins");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'plugin_id'
     */
    public final NumberPath<Long> pluginId = createNumber("plugin_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<BuiltinPlugins> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'builtin_plugins_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Plugins> fk_builtinPluginsIbfk1 = createForeignKey(pluginId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QBuiltinPlugins(String variable) {
        super(BuiltinPlugins.class, forVariable(variable), "null", "builtin_plugins");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QBuiltinPlugins(Path<? extends BuiltinPlugins> path) {
        super(path.getType(), path.getMetadata(), "null", "builtin_plugins");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QBuiltinPlugins(PathMetadata<?> metadata) {
        super(BuiltinPlugins.class, metadata, "null", "builtin_plugins");
    }

}

