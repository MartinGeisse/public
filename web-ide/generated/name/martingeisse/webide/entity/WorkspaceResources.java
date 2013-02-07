/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'workspace_resources'.
 */
public class WorkspaceResources implements Serializable {

    /**
     * Constructor.
     */
    public WorkspaceResources() {
    }

    /**
     * the contents
     */
    private byte[] contents;

    /**
     * the id
     */
    private Long id;

    /**
     * the name
     */
    private String name;

    /**
     * the parentId
     */
    private Long parentId;

    /**
     * the type
     */
    private String type;

    /**
     * the workspaceId
     */
    private Long workspaceId;

    /**
     * Getter method for the contents.
     * @return the contents
     */
    public byte[] getContents() {
        return contents;
    }

    /**
     * Setter method for the contents.
     * @param contents the contents to set
     */
    public void setContents(byte[] contents) {
        this.contents = contents;
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
     * Getter method for the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for the name.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for the parentId.
     * @return the parentId
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * Setter method for the parentId.
     * @param parentId the parentId to set
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * Getter method for the type.
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter method for the type.
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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
        return "{WorkspaceResources. contents = " + contents + ", id = " + id + ", name = " + name + ", parentId = " + parentId + ", type = " + type + ", workspaceId = " + workspaceId + "}";
    }

}

