/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_pm_buddies'.
 */
@GeneratedFromTable("phorum_pm_buddies")
public class PhorumPmBuddies extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumPmBuddies.class);

    /**
     * Constructor.
     */
    public PhorumPmBuddies() {
        super(GENERATED_CLASS_META_DATA);
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
    @GeneratedFromColumn("buddy_user_id")
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
    @GeneratedFromColumn("pm_buddy_id")
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
    @GeneratedFromColumn("user_id")
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

