/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QMarkers is a Querydsl query type for Markers
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QMarkers extends com.mysema.query.sql.RelationalPathBase<Markers> {

    private static final long serialVersionUID = -2022741872;

    /**
     * The default instance of this class.
     */
    public static final QMarkers markers = new QMarkers("markers");

    /**
     * Metamodel property for property 'column'
     */
    public final NumberPath<Long> column = createNumber("column", Long.class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'line'
     */
    public final NumberPath<Long> line = createNumber("line", Long.class);

    /**
     * Metamodel property for property 'meaning'
     */
    public final StringPath meaning = createString("meaning");

    /**
     * Metamodel property for property 'message'
     */
    public final StringPath message = createString("message");

    /**
     * Metamodel property for property 'origin'
     */
    public final StringPath origin = createString("origin");

    /**
     * Metamodel property for property 'workspace_resource_id'
     */
    public final NumberPath<Long> workspaceResourceId = createNumber("workspace_resource_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Markers> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'markers_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<WorkspaceResources> fk_markersIbfk1 = createForeignKey(workspaceResourceId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QMarkers(String variable) {
        super(Markers.class, forVariable(variable), "null", "markers");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QMarkers(Path<? extends Markers> path) {
        super(path.getType(), path.getMetadata(), "null", "markers");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QMarkers(PathMetadata<?> metadata) {
        super(Markers.class, metadata, "null", "markers");
    }

}

