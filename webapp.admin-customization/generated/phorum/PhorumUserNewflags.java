/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;

/**
 * This class represents rows from table 'phorum_user_newflags'.
 */
public class PhorumUserNewflags extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumUserNewflags.class);

    /**
     * Constructor.
     */
    public PhorumUserNewflags() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the forumId
     */
    private Integer forumId;

    /**
     * the messageId
     */
    private Integer messageId;

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
     * Getter method for the messageId.
     * @return the messageId
     */
    public Integer getMessageId() {
        return messageId;
    }

    /**
     * Setter method for the messageId.
     * @param messageId the messageId to set
     */
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
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
        return "forumId = " + forumId + ", messageId = " + messageId + ", userId = " + userId;
    }

}

