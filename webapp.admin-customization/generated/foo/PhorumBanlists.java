package foo;


/**
 * PhorumBanlists is a Querydsl bean type
 */
public class PhorumBanlists {

    private String comments;

    private Integer forumId;

    private Integer id;

    private Boolean pcre;

    private String string;

    private Byte type;

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getForumId() {
        return forumId;
    }

    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getPcre() {
        return pcre;
    }

    public void setPcre(Boolean pcre) {
        this.pcre = pcre;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

}

