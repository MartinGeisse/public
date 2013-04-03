/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'extension_bindings'.
 */
public class ExtensionBindings implements Serializable {

    /**
     * Constructor.
     */
    public ExtensionBindings() {
    }

    /**
     * the declaredExtensionId
     */
    private Long declaredExtensionId;

    /**
     * the declaredExtensionPointId
     */
    private Long declaredExtensionPointId;

    /**
     * the extensionNetworkId
     */
    private Long extensionNetworkId;

    /**
     * the id
     */
    private Long id;

    /**
     * Getter method for the declaredExtensionId.
     * @return the declaredExtensionId
     */
    public Long getDeclaredExtensionId() {
        return declaredExtensionId;
    }

    /**
     * Setter method for the declaredExtensionId.
     * @param declaredExtensionId the declaredExtensionId to set
     */
    public void setDeclaredExtensionId(Long declaredExtensionId) {
        this.declaredExtensionId = declaredExtensionId;
    }

    /**
     * Getter method for the declaredExtensionPointId.
     * @return the declaredExtensionPointId
     */
    public Long getDeclaredExtensionPointId() {
        return declaredExtensionPointId;
    }

    /**
     * Setter method for the declaredExtensionPointId.
     * @param declaredExtensionPointId the declaredExtensionPointId to set
     */
    public void setDeclaredExtensionPointId(Long declaredExtensionPointId) {
        this.declaredExtensionPointId = declaredExtensionPointId;
    }

    /**
     * Getter method for the extensionNetworkId.
     * @return the extensionNetworkId
     */
    public Long getExtensionNetworkId() {
        return extensionNetworkId;
    }

    /**
     * Setter method for the extensionNetworkId.
     * @param extensionNetworkId the extensionNetworkId to set
     */
    public void setExtensionNetworkId(Long extensionNetworkId) {
        this.extensionNetworkId = extensionNetworkId;
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{ExtensionBindings. declaredExtensionId = " + declaredExtensionId + ", declaredExtensionPointId = " + declaredExtensionPointId + ", extensionNetworkId = " + extensionNetworkId + ", id = " + id + "}";
    }

}

