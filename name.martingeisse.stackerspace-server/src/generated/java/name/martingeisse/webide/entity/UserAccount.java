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
 * This class represents rows from table 'user_account'.
 */
public class UserAccount implements Serializable, IEntityWithId<Long>, IEntityWithDeletedFlag {

    /**
     * Constructor.
     */
    public UserAccount() {
    }

    /**
     * the deleted
     */
    private Boolean deleted;

    /**
     * the id
     */
    private Long id;

    /**
     * the passwordHash
     */
    private String passwordHash;

    /**
     * the username
     */
    private String username;

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
     * Getter method for the passwordHash.
     * @return the passwordHash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Setter method for the passwordHash.
     * @param passwordHash the passwordHash to set
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Getter method for the username.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for the username.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{UserAccount. deleted = " + deleted + ", id = " + id + ", passwordHash = " + passwordHash + ", username = " + username + "}";
    }

    /**
     * Loads a record by id.
     * @param id the id of the record to load
     * @return the loaded record
     */
    public static UserAccount findById(long id) {
        final QUserAccount q = QUserAccount.userAccount;
        final SQLQuery query = EntityConnectionManager.getConnection().createQuery();
        return query.from(q).where(q.id.eq(id)).singleResult(q);
    }

    /**
     * Inserts a record into the database using all fields from this object except the ID, then updates the ID.
     */
    public void insert() {
        final QUserAccount q = QUserAccount.userAccount;
        final SQLInsertClause insert = EntityConnectionManager.getConnection().createInsert(q);
        insert.set(q.deleted, deleted);
        insert.set(q.passwordHash, passwordHash);
        insert.set(q.username, username);
        id = insert.executeWithKey(Long.class);
    }

}

