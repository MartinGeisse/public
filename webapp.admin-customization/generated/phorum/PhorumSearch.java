/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_search'.
 */
@GeneratedFromTable("phorum_search")
public class PhorumSearch extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumSearch.class);

    /**
     * Constructor.
     */
    public PhorumSearch() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the dateTest
     */
    private org.joda.time.LocalDate dateTest;

    /**
     * the datetimeTest
     */
    private org.joda.time.DateTime datetimeTest;

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
    @GeneratedFromColumn("date_test")
    public org.joda.time.LocalDate getDateTest() {
        return dateTest;
    }

    /**
     * Setter method for the dateTest.
     * @param dateTest the dateTest to set
     */
    public void setDateTest(org.joda.time.LocalDate dateTest) {
        this.dateTest = dateTest;
    }

    /**
     * Getter method for the datetimeTest.
     * @return the datetimeTest
     */
    @GeneratedFromColumn("datetime_test")
    public org.joda.time.DateTime getDatetimeTest() {
        return datetimeTest;
    }

    /**
     * Setter method for the datetimeTest.
     * @param datetimeTest the datetimeTest to set
     */
    public void setDatetimeTest(org.joda.time.DateTime datetimeTest) {
        this.datetimeTest = datetimeTest;
    }

    /**
     * Getter method for the forumId.
     * @return the forumId
     */
    @GeneratedFromColumn("forum_id")
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
    @GeneratedFromColumn("message_id")
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
    @GeneratedFromColumn("search_text")
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
        return "{PhorumSearch. dateTest = " + dateTest + ", datetimeTest = " + datetimeTest + ", forumId = " + forumId + ", messageId = " + messageId + ", searchText = " + searchText + "}";
    }

}

