/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_forum_group_xref'.
 */
@GeneratedFromTable("phorum_forum_group_xref")
public class PhorumForumGroupXref extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumForumGroupXref.class);

    /**
     * Constructor.
     */
    public PhorumForumGroupXref() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the forumId
     */
    private Integer forumId;

    /**
     * the groupId
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
    @GeneratedFromColumn("forum_id")
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
     * Getter method for the permission.
     * @return the permission
     */
    @GeneratedFromColumn("permission")
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
        return "{PhorumForumGroupXref. forumId = " + forumId + ", groupId = " + groupId + ", permission = " + permission + "}";
    }

}

