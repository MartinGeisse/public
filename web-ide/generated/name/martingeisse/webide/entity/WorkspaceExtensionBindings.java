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
     * the anchorPath
     */
    private String anchorPath;

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
     * the workspaceId
     */
    private Long workspaceId;

    /**
     * Getter method for the anchorPath.
     * @return the anchorPath
     */
    public String getAnchorPath() {
        return anchorPath;
    }

    /**
     * Setter method for the anchorPath.
     * @param anchorPath the anchorPath to set
     */
    public void setAnchorPath(String anchorPath) {
        this.anchorPath = anchorPath;
    }

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
     * Getter method for the workspaceId.
     * @return the workspaceId
     */
    public Long getWorkspaceId() {
        return workspaceId;
    }

    /**
     * Setter method for the workspaceId.
     * @param workspaceId the workspaceId to set
     */
    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{WorkspaceExtensionBindings. anchorPath = " + anchorPath + ", declaredExtensionId = " + declaredExtensionId + ", declaredExtensionPointId = " + declaredExtensionPointId + ", id = " + id + ", workspaceId = " + workspaceId + "}";
    }

}

