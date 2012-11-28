/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.util.ArrayList;
import com.mysema.query.support.Expressions;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Predicate;
import name.martingeisse.common.database.EntityConnectionManager;
import name.martingeisse.apidemo.Databases;
import com.mysema.commons.lang.CloseableIterator;
import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents rows from table 'phorum_pm_buddies'.
 */
public class PhorumPmBuddies implements Serializable {

    /**
     * Constructor.
     */
    public PhorumPmBuddies() {
    }

    /**
     * the buddyUserId
     */
    private Integer buddyUserId;

    /**
     * the pmBuddyId
     */
    private Integer pmBuddyId;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the buddyUserId.
     * @return the buddyUserId
     */
    public Integer getBuddyUserId() {
        return buddyUserId;
    }

    /**
     * Setter method for the buddyUserId.
     * @param buddyUserId the buddyUserId to set
     */
    public void setBuddyUserId(Integer buddyUserId) {
        this.buddyUserId = buddyUserId;
    }

    /**
     * Getter method for the pmBuddyId.
     * @return the pmBuddyId
     */
    public Integer getPmBuddyId() {
        return pmBuddyId;
    }

    /**
     * Setter method for the pmBuddyId.
     * @param pmBuddyId the pmBuddyId to set
     */
    public void setPmBuddyId(Integer pmBuddyId) {
        this.pmBuddyId = pmBuddyId;
    }

    /**
     * Getter method for the userId.
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Setter method for the userId.
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PhorumPmBuddies. buddyUserId = " + buddyUserId + ", pmBuddyId = " + pmBuddyId + ", userId = " + userId + "}";
    }

}

