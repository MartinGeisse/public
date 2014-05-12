/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPlayerInventorySlot is a Querydsl query type for PlayerInventorySlot
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPlayerInventorySlot extends com.mysema.query.sql.RelationalPathBase<PlayerInventorySlot> {

    private static final long serialVersionUID = -1022099792;

    /**
     * The default instance of this class.
     */
    public static final QPlayerInventorySlot playerInventorySlot = new QPlayerInventorySlot("player_inventory_slot");

    /**
     * Metamodel property for property 'equipped'
     */
    public final BooleanPath equipped = createBoolean("equipped");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'index'
     */
    public final NumberPath<Integer> index = createNumber("index", Integer.class);

    /**
     * Metamodel property for property 'player_id'
     */
    public final NumberPath<Long> playerId = createNumber("player_id", Long.class);

    /**
     * Metamodel property for property 'quantity'
     */
    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    /**
     * Metamodel property for property 'type'
     */
    public final NumberPath<Integer> type = createNumber("type", Integer.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PlayerInventorySlot> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'player_inventory_slot_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Player> fk_playerInventorySlotIbfk1 = createForeignKey(playerId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPlayerInventorySlot(String variable) {
        super(PlayerInventorySlot.class, forVariable(variable), "null", "player_inventory_slot");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPlayerInventorySlot(Path<? extends PlayerInventorySlot> path) {
        super(path.getType(), path.getMetadata(), "null", "player_inventory_slot");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPlayerInventorySlot(PathMetadata<?> metadata) {
        super(PlayerInventorySlot.class, metadata, "null", "player_inventory_slot");
    }

}

