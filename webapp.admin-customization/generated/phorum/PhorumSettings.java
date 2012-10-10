/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;

/**
 * This class represents rows from table 'phorum_settings'.
 */
public class PhorumSettings extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumSettings.class);

    /**
     * Constructor.
     */
    public PhorumSettings() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the data
     */
    private String data;

    /**
     * the groupId
     */
    private Integer groupId;

    /**
     * the name
     */
    private String name;

    /**
     * the type
     */
    private String type;

    /**
     * Getter method for the data.
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Setter method for the data.
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
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
     * Getter method for the name.
     * @return the name
     */
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
     * Getter method for the type.
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for the type.
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "data = " + data + ", groupId = " + groupId + ", name = " + name + ", type = " + type;
    }

}

