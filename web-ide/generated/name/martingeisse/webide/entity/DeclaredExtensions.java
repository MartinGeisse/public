/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'declared_extensions'.
 */
public class DeclaredExtensions implements Serializable {

    /**
     * Constructor.
     */
    public DeclaredExtensions() {
    }

    /**
     * the descriptor
     */
    private String descriptor;

    /**
     * the extensionPointName
     */
    private String extensionPointName;

    /**
     * the id
     */
    private Long id;

    /**
     * the pluginBundleId
     */
    private Long pluginBundleId;

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
     * Getter method for the extensionPointName.
     * @return the extensionPointName
     */
    public String getExtensionPointName() {
        return extensionPointName;
    }

    /**
     * Setter method for the extensionPointName.
     * @param extensionPointName the extensionPointName to set
     */
    public void setExtensionPointName(String extensionPointName) {
        this.extensionPointName = extensionPointName;
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
     * Getter method for the pluginBundleId.
     * @return the pluginBundleId
     */
    public Long getPluginBundleId() {
        return pluginBundleId;
    }

    /**
     * Setter method for the pluginBundleId.
     * @param pluginBundleId the pluginBundleId to set
     */
    public void setPluginBundleId(Long pluginBundleId) {
        this.pluginBundleId = pluginBundleId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{DeclaredExtensions. descriptor = " + descriptor + ", extensionPointName = " + extensionPointName + ", id = " + id + ", pluginBundleId = " + pluginBundleId + "}";
    }

}

