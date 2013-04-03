/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'extension_networks'.
 */
public class ExtensionNetworks implements Serializable {

    /**
     * Constructor.
     */
    public ExtensionNetworks() {
    }

    /**
     * the id
     */
    private Long id;

    /**
     * the userId
     */
    private Long userId;

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
     * Getter method for the userId.
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Setter method for the userId.
     * @param userId the userId to set
     */
    public void setUserId(Long userId) {
        this.userId = userId;
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
        return "{ExtensionNetworks. id = " + id + ", userId = " + userId + ", workspaceId = " + workspaceId + "}";
    }

}

