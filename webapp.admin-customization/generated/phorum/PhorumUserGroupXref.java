/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_user_group_xref'.
 */
@GeneratedFromTable("phorum_user_group_xref")
public class PhorumUserGroupXref extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumUserGroupXref.class);

    /**
     * Constructor.
     */
    public PhorumUserGroupXref() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the groupId
     */
    private Integer groupId;

    /**
     * the status
     */
    private Byte status;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the groupId.
     * @return the groupId
     */
    @GeneratedFromColumn("group_id")
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Setter method for the groupId.
     * @param groupId the groupId to set
     */
    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * Getter method for the status.
     * @return the status
     */
    @GeneratedFromColumn("status")
    public Byte getStatus() {
        return status;
    }

    /**
     * Setter method for the status.
     * @param status the status to set
     */
    public void setStatus(Byte status) {
        this.status = status;
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
        return "{PhorumUserGroupXref. groupId = " + groupId + ", status = " + status + ", userId = " + userId + "}";
    }

}

