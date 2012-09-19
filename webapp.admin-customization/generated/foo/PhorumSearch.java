package foo;


/**
 * PhorumSearch is a Querydsl bean type
 */
public class PhorumSearch {

    private java.sql.Date dateTest;

    private java.sql.Timestamp datetimeTest;

    private Integer forumId;

    private Integer messageId;

    private String searchText;

    public java.sql.Date getDateTest() {
        return dateTest;
    }

    public void setDateTest(java.sql.Date dateTest) {
        this.dateTest = dateTest;
    }

    public java.sql.Timestamp getDatetimeTest() {
        return datetimeTest;
    }

    public void setDatetimeTest(java.sql.Timestamp datetimeTest) {
        this.datetimeTest = datetimeTest;
    }

    public Integer getForumId() {
        return forumId;
    }

    public void setForumId(Integer forumId) {
        this.forumId = forumId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

}

