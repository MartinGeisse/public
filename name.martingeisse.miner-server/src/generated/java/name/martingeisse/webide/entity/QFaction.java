/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QFaction is a Querydsl query type for Faction
 */
@Generated("name.martingeisse.sql.codegen.MetaDataSerializer")
public class QFaction extends com.mysema.query.sql.RelationalPathBase<Faction> {

    private static final long serialVersionUID = 341086003;

    /**
     * The default instance of this class.
     */
    public static final QFaction faction = new QFaction("faction");

    /**
     * Metamodel property for property 'divine_power'
     */
    public final NumberPath<Long> divinePower = createNumber("divine_power", Long.class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'score'
     */
    public final NumberPath<Long> score = createNumber("score", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Faction> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for reverse foreign key 'player_ibfk_2'
     */
    public final com.mysema.query.sql.ForeignKey<Player> fk__playerIbfk2 = createInvForeignKey(id, "faction_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QFaction(String variable) {
        super(Faction.class, forVariable(variable), "null", "faction");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QFaction(Path<? extends Faction> path) {
        super(path.getType(), path.getMetadata(), "null", "faction");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QFaction(PathMetadata<?> metadata) {
        super(Faction.class, metadata, "null", "faction");
    }

}

