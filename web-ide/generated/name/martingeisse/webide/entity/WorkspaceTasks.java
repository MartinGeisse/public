/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_tasks'.
 */
public class WorkspaceTasks implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceTasks() {
    }

    /**
     * the command
     */
    private String command;

    /**
     * the id
     */
    private Long id;

    /**
     * the workspaceId
     */
    private Long workspaceId;

    /**
     * Getter method for the command.
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Setter method for the command.
     * @param command the command to set
     */
    public void setCommand(String command) {
        this.command = command;
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
        return "{WorkspaceTasks. command = " + command + ", id = " + id + ", workspaceId = " + workspaceId + "}";
    }

}

