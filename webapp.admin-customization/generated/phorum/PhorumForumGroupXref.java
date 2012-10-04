/**
 * This file was generated from the database schema.
 */
package phorum;


/**
 * This class represents rows from table 'phorum_forum_group_xref'.
 */
public class PhorumForumGroupXref {

    /**
     * Constructor.
     */
    public PhorumForumGroupXref() {
    }

    /**
     * the forum_id
     */
    private Integer forumId;

    /**
     * the group_id
     */
    private Integer groupId;

    /**
     * the permission
     */
    private Integer permission;

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
     * Getter method for the groupId.
     * @return the groupId
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "forumId = " + forumId + ", groupId = " + groupId + ", permission = " + permission;
    }

}

