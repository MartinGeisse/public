/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_extension_bindings'.
 */
public class WorkspaceExtensionBindings implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceExtensionBindings() {
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
     * the id
     */
    private Long id;

    /**
     * the workspaceExtensionNetworkId
     */
    private Long workspaceExtensionNetworkId;

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
     * Getter method for the workspaceExtensionNetworkId.
     * @return the workspaceExtensionNetworkId
     */
    public Long getWorkspaceExtensionNetworkId() {
        return workspaceExtensionNetworkId;
    }

    /**
     * Setter method for the workspaceExtensionNetworkId.
     * @param workspaceExtensionNetworkId the workspaceExtensionNetworkId to set
     */
    public void setWorkspaceExtensionNetworkId(Long workspaceExtensionNetworkId) {
        this.workspaceExtensionNetworkId = workspaceExtensionNetworkId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{WorkspaceExtensionBindings. declaredExtensionId = " + declaredExtensionId + ", declaredExtensionPointId = " + declaredExtensionPointId + ", id = " + id + ", workspaceExtensionNetworkId = " + workspaceExtensionNetworkId + "}";
    }

}

