/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_extension_networks'.
 */
public class WorkspaceExtensionNetworks implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceExtensionNetworks() {
    }

    /**
     * the anchorPath
     */
    private String anchorPath;

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
        return "{WorkspaceExtensionNetworks. anchorPath = " + anchorPath + ", id = " + id + ", workspaceId = " + workspaceId + "}";
    }

}

