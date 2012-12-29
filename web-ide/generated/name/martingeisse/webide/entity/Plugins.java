/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'plugins'.
 */
public class Plugins implements Serializable {

    /**
     * Constructor.
     */
    public Plugins() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the isUnpacked
     */
    private Boolean isUnpacked;

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
     * Getter method for the isUnpacked.
     * @return the isUnpacked
     */
    public Boolean getIsUnpacked() {
        return isUnpacked;
    }

    /**
     * Setter method for the isUnpacked.
     * @param isUnpacked the isUnpacked to set
     */
    public void setIsUnpacked(Boolean isUnpacked) {
        this.isUnpacked = isUnpacked;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Plugins. id = " + id + ", isUnpacked = " + isUnpacked + "}";
    }

}

