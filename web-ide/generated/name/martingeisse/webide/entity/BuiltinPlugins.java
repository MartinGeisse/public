/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'builtin_plugins'.
 */
public class BuiltinPlugins implements Serializable {

    /**
     * Constructor.
     */
    public BuiltinPlugins() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the pluginId
     */
    private Long pluginId;

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
     * Getter method for the pluginId.
     * @return the pluginId
     */
    public Long getPluginId() {
        return pluginId;
    }

    /**
     * Setter method for the pluginId.
     * @param pluginId the pluginId to set
     */
    public void setPluginId(Long pluginId) {
        this.pluginId = pluginId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{BuiltinPlugins. id = " + id + ", pluginId = " + pluginId + "}";
    }

}

