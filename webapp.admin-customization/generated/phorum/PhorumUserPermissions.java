/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;

/**
 * This class represents rows from table 'phorum_user_permissions'.
 */
public class PhorumUserPermissions extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumUserPermissions.class);

    /**
     * Constructor.
     */
    public PhorumUserPermissions() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the forumId
     */
    private Integer forumId;

    /**
     * the permission
     */
    private Integer permission;

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
     * Getter method for the permission.
     * @return the permission
     */
    public Integer getPermission() {
        return permission;
    }

    /**
     * Setter method for the permission.
     * @param permission the permission to set
     */
    public void setPermission(Integer permission) {
        this.permission = permission;
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
        return "forumId = " + forumId + ", permission = " + permission + ", userId = " + userId;
    }

}

