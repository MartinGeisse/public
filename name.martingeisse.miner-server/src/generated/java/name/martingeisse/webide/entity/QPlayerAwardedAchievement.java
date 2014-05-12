/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import javax.annotation.Generated;

import com.mysema.query.types.*;
import com.mysema.query.types.path.*;

import static com.mysema.query.types.PathMetadataFactory.*;

/**
 * QPlayerAwardedAchievement is a Querydsl query type for PlayerAwardedAchievement
 */
@Generated("name.martingeisse.tools.codegen.MetaDataSerializer")
public class QPlayerAwardedAchievement extends com.mysema.query.sql.RelationalPathBase<PlayerAwardedAchievement> {

    private static final long serialVersionUID = -675372451;

    /**
     * The default instance of this class.
     */
    public static final QPlayerAwardedAchievement playerAwardedAchievement = new QPlayerAwardedAchievement("player_awarded_achievement");

    /**
     * Metamodel property for property 'achievement_code'
     */
    public final StringPath achievementCode = createString("achievement_code");

    /**
     * Metamodel property for property 'id'
     */
    public final NumberPath<Long> id = createNumber("id", Long.class);

    /**
     * Metamodel property for property 'player_id'
     */
    public final NumberPath<Long> playerId = createNumber("player_id", Long.class);

    /**
     * Metamodel property for primary key 'PRIMARY'
     */
    public final com.mysema.query.sql.PrimaryKey<PlayerAwardedAchievement> pk_primary = createPrimaryKey(id);

    /**
     * Metamodel property for foreign key 'player_awarded_achievement_ibfk_1'
     */
    public final com.mysema.query.sql.ForeignKey<Player> fk_playerAwardedAchievementIbfk1 = createForeignKey(playerId, "id");

    /**
     * Path-variable based constructor.
     * @param variable the path variable for this entity
     */
    public QPlayerAwardedAchievement(String variable) {
        super(PlayerAwardedAchievement.class, forVariable(variable), "null", "player_awarded_achievement");
    }

    /**
     * Path based constructor
     * @param path the path for this entity
     */
    public QPlayerAwardedAchievement(Path<? extends PlayerAwardedAchievement> path) {
        super(path.getType(), path.getMetadata(), "null", "player_awarded_achievement");
    }

    /**
     * Path metadata based constructor
     * @param metadata the path metadata for this entity
     */
    public QPlayerAwardedAchievement(PathMetadata<?> metadata) {
        super(PlayerAwardedAchievement.class, metadata, "null", "player_awarded_achievement");
    }

}

