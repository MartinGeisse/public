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
     * the jarfile
     */
    private byte[] jarfile;

    /**
     * the pluginVersionId
     */
    private Long pluginVersionId;

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
     * Getter method for the jarfile.
     * @return the jarfile
     */
    public byte[] getJarfile() {
        return jarfile;
    }

    /**
     * Setter method for the jarfile.
     * @param jarfile the jarfile to set
     */
    public void setJarfile(byte[] jarfile) {
        this.jarfile = jarfile;
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
        return "{PluginBundles. descriptor = " + descriptor + ", id = " + id + ", jarfile = " + jarfile + ", pluginVersionId = " + pluginVersionId + "}";
    }

}

