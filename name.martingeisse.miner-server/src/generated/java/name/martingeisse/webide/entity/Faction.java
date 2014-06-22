/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import name.martingeisse.sql.terms.IEntityWithId;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import name.martingeisse.sql.EntityConnectionManager;
import java.io.Serializable;

/**
 * This class represents rows from table 'faction'.
 */
public class Faction implements Serializable, IEntityWithId<Long> {

    /**
     * Constructor.
     */
    public Faction() {
    }

    /**
     * the divinePower
     */
    private Long divinePower;

    /**
     * the id
     */
    private Long id;

    /**
     * the score
     */
    private Long score;

    /**
     * Getter method for the divinePower.
     * @return the divinePower
     */
    public Long getDivinePower() {
        return divinePower;
    }

    /**
     * Setter method for the divinePower.
     * @param divinePower the divinePower to set
     */
    public void setDivinePower(Long divinePower) {
        this.divinePower = divinePower;
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
     * Getter method for the score.
     * @return the score
     */
    public Long getScore() {
        return score;
    }

    /**
     * Setter method for the score.
     * @param score the score to set
     */
    public void setScore(Long score) {
        this.score = score;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Faction. divinePower = " + divinePower + ", id = " + id + ", score = " + score + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static Faction findById(long id) {
        final QFaction q = QFaction.faction;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QFaction q = QFaction.faction;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.divinePower, divinePower);
        insert.set(q.score, score);
        id = insert.executeWithKey(Long.class);
    }

}

