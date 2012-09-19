package foo;


/**
 * PhorumPmFolders is a Querydsl bean type
 */
public class PhorumPmFolders {

    private String foldername;

    private Integer pmFolderId;

    private Integer userId;

    public String getFoldername() {
        return foldername;
    }

    public void setFoldername(String foldername) {
        this.foldername = foldername;
    }

    public Integer getPmFolderId() {
        return pmFolderId;
    }

    public void setPmFolderId(Integer pmFolderId) {
        this.pmFolderId = pmFolderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

