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
 * This class represents rows from table 'phorum_subscribers'.
 */
public class PhorumSubscribers implements Serializable {

    /**
     * Constructor.
     */
    public PhorumSubscribers() {
    }

    /**
     * the forumId
     */
    private Integer forumId;

    /**
     * the subType
     */
    private Byte subType;

    /**
     * the thread
     */
    private Integer thread;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the forumId.
     * @return the forumId
     */
    public Integer getForumId() {
        return forumId;
    }

    /**
     * Setter method for the forumId.
     * @param forumId the forumId to set
     */
    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    /**
     * Getter method for the subType.
     * @return the subType
     */
    public Byte getSubType() {
        return subType;
    }

    /**
     * Setter method for the subType.
     * @param subType the subType to set
     */
    public void setSubType(Byte subType) {
        this.subType = subType;
    }

    /**
     * Getter method for the thread.
     * @return the thread
     */
    public Integer getThread() {
        return thread;
    }

    /**
     * Setter method for the thread.
     * @param thread the thread to set
     */
    public void setThread(Integer thread) {
        this.thread = thread;
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
        return "{PhorumSubscribers. forumId = " + forumId + ", subType = " + subType + ", thread = " + thread + ", userId = " + userId + "}";
    }

}

