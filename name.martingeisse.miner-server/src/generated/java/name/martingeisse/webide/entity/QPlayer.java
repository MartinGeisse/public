/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPlayer is a Querydsl query type for Player
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPlayer extends com.mysema.query.sql.RelationalPathBase<Player> {

    private static final long serialVersionUID = 584492778;

    /**
     * The default instance of this class.
     */
    public static final QPlayer player = new QPlayer("player");

    /**
     * Metamodel property for property 'coins'
     */
    public final NumberPath<Long> coins = createNumber("coins", Long.class);

    /**
     * Metamodel property for property 'faction'
     */
    public final NumberPath<Integer> faction = createNumber("faction", Integer.class);

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'name'
     */
    public final StringPath name = createString("name");

    /**
     * Metamodel property for property 'user_account_id'
     */
    public final NumberPath<Long> userAccountId = createNumber("user_account_id", Long.class);

    /**
     * Metamodel property for property 'x'
     */
    public final NumberPath<java.math.BigDecimal> x = createNumber("x", java.math.BigDecimal.class);

    /**
     * Metamodel property for property 'y'
     */
    public final NumberPath<java.math.BigDecimal> y = createNumber("y", java.math.BigDecimal.class);

    /**
     * Metamodel property for property 'z'
     */
    public final NumberPath<java.math.BigDecimal> z = createNumber("z", java.math.BigDecimal.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<Player> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'player_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<UserAccount> fk_playerIbfk1 = createForeignKey(userAccountId, "id");

    /**
     * Metamodel property for reverse foreign key 'player_awarded_achievement_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PlayerAwardedAchievement> fk__playerAwardedAchievementIbfk1 = createInvForeignKey(id, "player_id");

    /**
     * Metamodel property for reverse foreign key 'player_inventory_slot_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<PlayerInventorySlot> fk__playerInventorySlotIbfk1 = createInvForeignKey(id, "player_id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPlayer(String variable) {
        super(Player.class, forVariable(variable), "null", "player");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPlayer(Path<? extends Player> path) {
        super(path.getType(), path.getMetadata(), "null", "player");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPlayer(PathMetadata<?> metadata) {
        super(Player.class, metadata, "null", "player");
    }

}

