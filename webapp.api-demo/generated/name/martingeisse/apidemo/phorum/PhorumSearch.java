/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.io.Serializable;

import org.joda.time.DateTime;

/**
 * This class represents rows from table 'phorum_search'.
 */
public class PhorumSearch implements Serializable {

    /**
     * Constructor.
     */
    public PhorumSearch() {
    }

    /**
     * the dateTest
     */
    private java.sql.Date dateTest;

    /**
     * the datetimeTest
     */
    private DateTime datetimeTest;

    /**
     * the forumId
     */
    private Integer forumId;

    /**
     * the messageId
     */
    private Integer messageId;

    /**
     * the searchText
     */
    private String searchText;

    /**
     * Getter method for the dateTest.
     * @return the dateTest
     */
    public java.sql.Date getDateTest() {
        return dateTest;
    }

    /**
     * Setter method for the dateTest.
     * @param dateTest the dateTest to set
     */
    public void setDateTest(java.sql.Date dateTest) {
        this.dateTest = dateTest;
    }

    /**
     * Getter method for the datetimeTest.
     * @return the datetimeTest
     */
    public DateTime getDatetimeTest() {
        return datetimeTest;
    }

    /**
     * Setter method for the datetimeTest.
     * @param datetimeTest the datetimeTest to set
     */
    public void setDatetimeTest(DateTime datetimeTest) {
        this.datetimeTest = datetimeTest;
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
     * Getter method for the messageId.
     * @return the messageId
     */
    public Integer getMessageId() {
        return messageId;
    }

    /**
     * Setter method for the messageId.
     * @param messageId the messageId to set
     */
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    /**
     * Getter method for the searchText.
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * Setter method for the searchText.
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "dateTest = " + dateTest + ", datetimeTest = " + datetimeTest + ", forumId = " + forumId + ", messageId = " + messageId + ", searchText = " + searchText;
    }

}

