/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'plugin_bundles'.
 */
public class PluginBundles implements Serializable {

    /**
     * Constructor.
     */
    public PluginBundles() {
    }

    /**
     * the descriptor
     */
    private String descriptor;

    /**
     * the id
     */
    private Long id;

    /**
     * the pluginId
     */
    private Long pluginId;

    /**
     * Getter method for the descriptor.
     * @return the descriptor
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Setter method for the descriptor.
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

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
        return "{PluginBundles. descriptor = " + descriptor + ", id = " + id + ", pluginId = " + pluginId + "}";
    }

}

