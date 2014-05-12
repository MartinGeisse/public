/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import name.martingeisse.common.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import name.martingeisse.common.database.EntityConnectionManager;
import com.mysema.query.sql.dml.SQLInsertClause;
import java.io.Serializable;

/**
 * This class represents rows from table 'player_awarded_achievement'.
 */
public class PlayerAwardedAchievement implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public PlayerAwardedAchievement() {
    }

    /**
     * the achievementCode
     */
    private String achievementCode;

    /**
     * the id
     */
    private Long id;

    /**
     * the playerId
     */
    private Long playerId;

    /**
     * Getter method for the achievementCode.
     * @return the achievementCode
     */
    public String getAchievementCode() {
        return achievementCode;
    }

    /**
     * Setter method for the achievementCode.
     * @param achievementCode the achievementCode to set
     */
    public void setAchievementCode(String achievementCode) {
        this.achievementCode = achievementCode;
    }

    /**
     * Getter method for the id.
     * @return the id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Setter method for the id.
     * @param id the id to set
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter method for the playerId.
     * @return the playerId
     */
    public Long getPlayerId() {
        return playerId;
    }

    /**
     * Setter method for the playerId.
     * @param playerId the playerId to set
     */
    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PlayerAwardedAchievement. achievementCode = " + achievementCode + ", id = " + id + ", playerId = " + playerId + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static PlayerAwardedAchievement findById(long id) {
        final QPlayerAwardedAchievement q = QPlayerAwardedAchievement.playerAwardedAchievement;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QPlayerAwardedAchievement q = QPlayerAwardedAchievement.playerAwardedAchievement;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.achievementCode, achievementCode);
        insert.set(q.playerId, playerId);
        id = insert.executeWithKey(Long.class);
    }

}

