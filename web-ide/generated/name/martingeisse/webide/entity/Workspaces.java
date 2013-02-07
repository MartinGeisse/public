/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspaces'.
 */
public class Workspaces implements Serializable {

    /**
     * Constructor.
     */
    public Workspaces() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the isBuilding
     */
    private Boolean isBuilding;

    /**
     * the name
     */
    private String name;

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
     * Getter method for the isBuilding.
     * @return the isBuilding
     */
    public Boolean getIsBuilding() {
        return isBuilding;
    }

    /**
     * Setter method for the isBuilding.
     * @param isBuilding the isBuilding to set
     */
    public void setIsBuilding(Boolean isBuilding) {
        this.isBuilding = isBuilding;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Workspaces. id = " + id + ", isBuilding = " + isBuilding + ", name = " + name + "}";
    }

}

