/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import name.martingeisse.sql.terms.IEntityWithDeletedFlag;
import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import java.io.Serializable;

/**
 * This class represents rows from table 'player'.
 */
public class Player implements Serializable, IEntityWithId<Long>, IEntityWithDeletedFlag {

    /**
     * Constructor.
     */
    public Player() {
    }

    /**
     * the coins
     */
    private Long coins;

    /**
     * the deleted
     */
    private Boolean deleted;

    /**
     * the faction
     */
    private Integer faction;

    /**
     * the id
     */
    private Long id;

    /**
     * the leftAngle
     */
    private java.math.BigDecimal leftAngle;

    /**
     * the name
     */
    private String name;

    /**
     * the upAngle
     */
    private java.math.BigDecimal upAngle;

    /**
     * the userAccountId
     */
    private Long userAccountId;

    /**
     * the x
     */
    private java.math.BigDecimal x;

    /**
     * the y
     */
    private java.math.BigDecimal y;

    /**
     * the z
     */
    private java.math.BigDecimal z;

    /**
     * Getter method for the coins.
     * @return the coins
     */
    public Long getCoins() {
        return coins;
    }

    /**
     * Setter method for the coins.
     * @param coins the coins to set
     */
    public void setCoins(Long coins) {
        this.coins = coins;
    }

    /**
     * Getter method for the deleted.
     * @return the deleted
     */
    @Override
    public Boolean getDeleted() {
        return deleted;
    }

    /**
     * Setter method for the deleted.
     * @param deleted the deleted to set
     */
    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Getter method for the faction.
     * @return the faction
     */
    public Integer getFaction() {
        return faction;
    }

    /**
     * Setter method for the faction.
     * @param faction the faction to set
     */
    public void setFaction(Integer faction) {
        this.faction = faction;
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
     * Getter method for the leftAngle.
     * @return the leftAngle
     */
    public java.math.BigDecimal getLeftAngle() {
        return leftAngle;
    }

    /**
     * Setter method for the leftAngle.
     * @param leftAngle the leftAngle to set
     */
    public void setLeftAngle(java.math.BigDecimal leftAngle) {
        this.leftAngle = leftAngle;
    }

    /**
     * Getter method for the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for the upAngle.
     * @return the upAngle
     */
    public java.math.BigDecimal getUpAngle() {
        return upAngle;
    }

    /**
     * Setter method for the upAngle.
     * @param upAngle the upAngle to set
     */
    public void setUpAngle(java.math.BigDecimal upAngle) {
        this.upAngle = upAngle;
    }

    /**
     * Getter method for the userAccountId.
     * @return the userAccountId
     */
    public Long getUserAccountId() {
        return userAccountId;
    }

    /**
     * Setter method for the userAccountId.
     * @param userAccountId the userAccountId to set
     */
    public void setUserAccountId(Long userAccountId) {
        this.userAccountId = userAccountId;
    }

    /**
     * Getter method for the x.
     * @return the x
     */
    public java.math.BigDecimal getX() {
        return x;
    }

    /**
     * Setter method for the x.
     * @param x the x to set
     */
    public void setX(java.math.BigDecimal x) {
        this.x = x;
    }

    /**
     * Getter method for the y.
     * @return the y
     */
    public java.math.BigDecimal getY() {
        return y;
    }

    /**
     * Setter method for the y.
     * @param y the y to set
     */
    public void setY(java.math.BigDecimal y) {
        this.y = y;
    }

    /**
     * Getter method for the z.
     * @return the z
     */
    public java.math.BigDecimal getZ() {
        return z;
    }

    /**
     * Setter method for the z.
     * @param z the z to set
     */
    public void setZ(java.math.BigDecimal z) {
        this.z = z;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Player. coins = " + coins + ", deleted = " + deleted + ", faction = " + faction + ", id = " + id + ", leftAngle = " + leftAngle + ", name = " + name + ", upAngle = " + upAngle + ", userAccountId = " + userAccountId + ", x = " + x + ", y = " + y + ", z = " + z + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static Player findById(long id) {
        final QPlayer q = QPlayer.player;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QPlayer q = QPlayer.player;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.coins, coins);
        insert.set(q.deleted, deleted);
        insert.set(q.faction, faction);
        insert.set(q.leftAngle, leftAngle);
        insert.set(q.name, name);
        insert.set(q.upAngle, upAngle);
        insert.set(q.userAccountId, userAccountId);
        insert.set(q.x, x);
        insert.set(q.y, y);
        insert.set(q.z, z);
        insert.set(q.leftAngle, leftAngle);
        insert.set(q.upAngle, upAngle);
        id = insert.executeWithKey(Long.class);
    }

}

