/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_resource_deltas'.
 */
public class WorkspaceResourceDeltas implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceResourceDeltas() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the isDeep
     */
    private Boolean isDeep;

    /**
     * the path
     */
    private String path;

    /**
     * the workspaceId
     */
    private Long workspaceId;

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
     * Getter method for the isDeep.
     * @return the isDeep
     */
    public Boolean getIsDeep() {
        return isDeep;
    }

    /**
     * Setter method for the isDeep.
     * @param isDeep the isDeep to set
     */
    public void setIsDeep(Boolean isDeep) {
        this.isDeep = isDeep;
    }

    /**
     * Getter method for the path.
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter method for the path.
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
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
        return "{WorkspaceResourceDeltas. id = " + id + ", isDeep = " + isDeep + ", path = " + path + ", workspaceId = " + workspaceId + "}";
    }

}

