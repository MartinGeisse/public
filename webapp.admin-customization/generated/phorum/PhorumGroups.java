/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_groups'.
 */
@GeneratedFromTable("phorum_groups")
public class PhorumGroups extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumGroups.class);

    /**
     * Constructor.
     */
    public PhorumGroups() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the groupId
     */
    private Integer groupId;

    /**
     * the name
     */
    private String name;

    /**
     * the open
     */
    private Boolean open;

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
     * Getter method for the name.
     * @return the name
     */
    @GeneratedFromColumn("name")
    public String getName() {
        return name;
    }

    /**
     * Setter method for the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for the open.
     * @return the open
     */
    @GeneratedFromColumn("open")
    public Boolean getOpen() {
        return open;
    }

    /**
     * Setter method for the open.
     * @param open the open to set
     */
    public void setOpen(Boolean open) {
        this.open = open;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PhorumGroups. groupId = " + groupId + ", name = " + name + ", open = " + open + "}";
    }

}

