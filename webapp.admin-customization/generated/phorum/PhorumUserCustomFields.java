/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_user_custom_fields'.
 */
@GeneratedFromTable("phorum_user_custom_fields")
public class PhorumUserCustomFields extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumUserCustomFields.class);

    /**
     * Constructor.
     */
    public PhorumUserCustomFields() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the data
     */
    private String data;

    /**
     * the type
     */
    private Integer type;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * Getter method for the data.
     * @return the data
     */
    @GeneratedFromColumn("data")
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
     * Getter method for the type.
     * @return the type
     */
    @GeneratedFromColumn("type")
    public Integer getType() {
        return type;
    }

    /**
     * Setter method for the type.
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
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
        return "data = " + data + ", type = " + type + ", userId = " + userId;
    }

}

