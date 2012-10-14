/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.io.Serializable;

/**
 * This class represents rows from table 'phorum_user_custom_fields'.
 */
public class PhorumUserCustomFields implements Serializable {

    /**
     * Constructor.
     */
    public PhorumUserCustomFields() {
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

