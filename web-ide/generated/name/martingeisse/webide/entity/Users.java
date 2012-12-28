/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'users'.
 */
public class Users implements Serializable {

    /**
     * Constructor.
     */
    public Users() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the loginName
     */
    private String loginName;

    /**
     * Getter method for the id.
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter method for the id.
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter method for the loginName.
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Setter method for the loginName.
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Users. id = " + id + ", loginName = " + loginName + "}";
    }

}

