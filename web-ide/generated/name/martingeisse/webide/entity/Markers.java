/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'markers'.
 */
public class Markers implements Serializable {

    /**
     * Constructor.
     */
    public Markers() {
    }

    /**
     * the column
     */
    private Long column;

    /**
     * the id
     */
    private Long id;

    /**
     * the line
     */
    private Long line;

    /**
     * the meaning
     */
    private String meaning;

    /**
     * the message
     */
    private String message;

    /**
     * the origin
     */
    private String origin;

    /**
     * the workspaceId
     */
    private Long workspaceId;

    /**
     * the workspaceResourceId
     */
    private Long workspaceResourceId;

    /**
     * Getter method for the column.
     * @return the column
     */
    public Long getColumn() {
        return column;
    }

    /**
     * Setter method for the column.
     * @param column the column to set
     */
    public void setColumn(Long column) {
        this.column = column;
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
     * Getter method for the line.
     * @return the line
     */
    public Long getLine() {
        return line;
    }

    /**
     * Setter method for the line.
     * @param line the line to set
     */
    public void setLine(Long line) {
        this.line = line;
    }

    /**
     * Getter method for the meaning.
     * @return the meaning
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * Setter method for the meaning.
     * @param meaning the meaning to set
     */
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    /**
     * Getter method for the message.
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Setter method for the message.
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter method for the origin.
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Setter method for the origin.
     * @param origin the origin to set
     */
    public void setOrigin(String origin) {
        this.origin = origin;
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

    /**
     * Getter method for the workspaceResourceId.
     * @return the workspaceResourceId
     */
    public Long getWorkspaceResourceId() {
        return workspaceResourceId;
    }

    /**
     * Setter method for the workspaceResourceId.
     * @param workspaceResourceId the workspaceResourceId to set
     */
    public void setWorkspaceResourceId(Long workspaceResourceId) {
        this.workspaceResourceId = workspaceResourceId;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Markers. column = " + column + ", id = " + id + ", line = " + line + ", meaning = " + meaning + ", message = " + message + ", origin = " + origin + ", workspaceId = " + workspaceId + ", workspaceResourceId = " + workspaceResourceId + "}";
    }

}

