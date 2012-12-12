/**
 * This file was generated from the database schema.
 */
package name.martingeisse.webide.entity;

import java.io.Serializable;

/**
 * This class represents rows from table 'files'.
 */
public class Files implements Serializable {

    /**
     * Constructor.
     */
    public Files() {
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{Files. contents = " + contents + ", id = " + id + ", name = " + name + "}";
    }

}

