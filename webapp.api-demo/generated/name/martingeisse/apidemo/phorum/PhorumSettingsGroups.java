/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.io.Serializable;

/**
 * This class represents rows from table 'phorum_settings_groups'.
 */
public class PhorumSettingsGroups implements Serializable {

    /**
     * Constructor.
     */
    public PhorumSettingsGroups() {
    }

    /**
     * the alias
     */
    private String alias;

    /**
     * the id
     */
    private Integer id;

    /**
     * the name
     */
    private String name;

    /**
     * Getter method for the alias.
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Setter method for the alias.
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Getter method for the id.
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter method for the id.
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
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
        return "{PhorumSettingsGroups. alias = " + alias + ", id = " + id + ", name = " + name + "}";
    }

}

