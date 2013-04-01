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
     * the pluginVersionId
     */
    private Long pluginVersionId;

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
     * Getter method for the pluginVersionId.
     * @return the pluginVersionId
     */
    public Long getPluginVersionId() {
        return pluginVersionId;
    }

    /**
     * Setter method for the pluginVersionId.
     * @param pluginVersionId the pluginVersionId to set
     */
    public void setPluginVersionId(Long pluginVersionId) {
        this.pluginVersionId = pluginVersionId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{BuiltinPlugins. id = " + id + ", pluginVersionId = " + pluginVersionId + "}";
    }

}

