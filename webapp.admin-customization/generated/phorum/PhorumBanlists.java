/**
 * This file was generated from the database schema.
 */
package phorum;


/**
 * This class represents rows from table 'phorum_banlists'.
 */
public class PhorumBanlists {

    /**
     * Constructor.
     */
    public PhorumBanlists() {
    }

    /**
     * the comments
     */
    private String comments;

    /**
     * the forumId
     */
    private Integer forumId;

    /**
     * the id
     */
    private Integer id;

    /**
     * the pcre
     */
    private Boolean pcre;

    /**
     * the string
     */
    private String string;

    /**
     * the type
     */
    private Byte type;

    /**
     * Getter method for the comments.
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * Setter method for the comments.
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Getter method for the forumId.
     * @return the forumId
     */
    public Integer getForumId() {
        return forumId;
    }

    /**
     * Setter method for the forumId.
     * @param forumId the forumId to set
     */
    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    /**
     * Getter method for the id.
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Setter method for the id.
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Getter method for the pcre.
     * @return the pcre
     */
    public Boolean getPcre() {
        return pcre;
    }

    /**
     * Setter method for the pcre.
     * @param pcre the pcre to set
     */
    public void setPcre(Boolean pcre) {
        this.pcre = pcre;
    }

    /**
     * Getter method for the string.
     * @return the string
     */
    public String getString() {
        return string;
    }

    /**
     * Setter method for the string.
     * @param string the string to set
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * Getter method for the type.
     * @return the type
     */
    public Byte getType() {
        return type;
    }

    /**
     * Setter method for the type.
     * @param type the type to set
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "comments = " + comments + ", forumId = " + forumId + ", id = " + id + ", pcre = " + pcre + ", string = " + string + ", type = " + type;
    }

}

