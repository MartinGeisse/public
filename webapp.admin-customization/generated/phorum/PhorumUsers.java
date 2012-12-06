/**
 * This file was generated from the database schema.
 */
package phorum;

import name.martingeisse.admin.entity.instance.SpecificEntityInstanceMeta;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromTable;
import name.martingeisse.admin.entity.instance.AbstractSpecificEntityInstance;
import name.martingeisse.admin.entity.schema.orm.GeneratedFromColumn;

/**
 * This class represents rows from table 'phorum_users'.
 */
@GeneratedFromTable("phorum_users")
public class PhorumUsers extends AbstractSpecificEntityInstance {

    /**
     * Meta-data about this class for the admin framework
     */
    public static final SpecificEntityInstanceMeta GENERATED_CLASS_META_DATA = new SpecificEntityInstanceMeta(PhorumUsers.class);

    /**
     * Constructor.
     */
    public PhorumUsers() {
        super(GENERATED_CLASS_META_DATA);
    }

    /**
     * the active
     */
    private Boolean active;

    /**
     * the admin
     */
    private Boolean admin;

    /**
     * the dateAdded
     */
    private Integer dateAdded;

    /**
     * the dateLastActive
     */
    private Integer dateLastActive;

    /**
     * the displayName
     */
    private String displayName;

    /**
     * the email
     */
    private String email;

    /**
     * the emailNotify
     */
    private Boolean emailNotify;

    /**
     * the emailTemp
     */
    private String emailTemp;

    /**
     * the hideActivity
     */
    private Boolean hideActivity;

    /**
     * the hideEmail
     */
    private Boolean hideEmail;

    /**
     * the isDst
     */
    private Boolean isDst;

    /**
     * the lastActiveForum
     */
    private Integer lastActiveForum;

    /**
     * the moderationEmail
     */
    private Boolean moderationEmail;

    /**
     * the moderatorData
     */
    private String moderatorData;

    /**
     * the password
     */
    private String password;

    /**
     * the passwordTemp
     */
    private String passwordTemp;

    /**
     * the pmEmailNotify
     */
    private Boolean pmEmailNotify;

    /**
     * the posts
     */
    private Integer posts;

    /**
     * the realName
     */
    private String realName;

    /**
     * the sessidLt
     */
    private String sessidLt;

    /**
     * the sessidSt
     */
    private String sessidSt;

    /**
     * the sessidStTimeout
     */
    private Integer sessidStTimeout;

    /**
     * the settingsData
     */
    private String settingsData;

    /**
     * the showSignature
     */
    private Boolean showSignature;

    /**
     * the signature
     */
    private String signature;

    /**
     * the threadedList
     */
    private Boolean threadedList;

    /**
     * the threadedRead
     */
    private Boolean threadedRead;

    /**
     * the tzOffset
     */
    private Float tzOffset;

    /**
     * the userId
     */
    private Integer userId;

    /**
     * the userLanguage
     */
    private String userLanguage;

    /**
     * the userTemplate
     */
    private String userTemplate;

    /**
     * the username
     */
    private String username;

    /**
     * Getter method for the active.
     * @return the active
     */
    @GeneratedFromColumn("active")
    public Boolean getActive() {
        return active;
    }

    /**
     * Setter method for the active.
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * Getter method for the admin.
     * @return the admin
     */
    @GeneratedFromColumn("admin")
    public Boolean getAdmin() {
        return admin;
    }

    /**
     * Setter method for the admin.
     * @param admin the admin to set
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     * Getter method for the dateAdded.
     * @return the dateAdded
     */
    @GeneratedFromColumn("date_added")
    public Integer getDateAdded() {
        return dateAdded;
    }

    /**
     * Setter method for the dateAdded.
     * @param dateAdded the dateAdded to set
     */
    public void setDateAdded(Integer dateAdded) {
        this.dateAdded = dateAdded;
    }

    /**
     * Getter method for the dateLastActive.
     * @return the dateLastActive
     */
    @GeneratedFromColumn("date_last_active")
    public Integer getDateLastActive() {
        return dateLastActive;
    }

    /**
     * Setter method for the dateLastActive.
     * @param dateLastActive the dateLastActive to set
     */
    public void setDateLastActive(Integer dateLastActive) {
        this.dateLastActive = dateLastActive;
    }

    /**
     * Getter method for the displayName.
     * @return the displayName
     */
    @GeneratedFromColumn("display_name")
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter method for the displayName.
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter method for the email.
     * @return the email
     */
    @GeneratedFromColumn("email")
    public String getEmail() {
        return email;
    }

    /**
     * Setter method for the email.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Getter method for the emailNotify.
     * @return the emailNotify
     */
    @GeneratedFromColumn("email_notify")
    public Boolean getEmailNotify() {
        return emailNotify;
    }

    /**
     * Setter method for the emailNotify.
     * @param emailNotify the emailNotify to set
     */
    public void setEmailNotify(Boolean emailNotify) {
        this.emailNotify = emailNotify;
    }

    /**
     * Getter method for the emailTemp.
     * @return the emailTemp
     */
    @GeneratedFromColumn("email_temp")
    public String getEmailTemp() {
        return emailTemp;
    }

    /**
     * Setter method for the emailTemp.
     * @param emailTemp the emailTemp to set
     */
    public void setEmailTemp(String emailTemp) {
        this.emailTemp = emailTemp;
    }

    /**
     * Getter method for the hideActivity.
     * @return the hideActivity
     */
    @GeneratedFromColumn("hide_activity")
    public Boolean getHideActivity() {
        return hideActivity;
    }

    /**
     * Setter method for the hideActivity.
     * @param hideActivity the hideActivity to set
     */
    public void setHideActivity(Boolean hideActivity) {
        this.hideActivity = hideActivity;
    }

    /**
     * Getter method for the hideEmail.
     * @return the hideEmail
     */
    @GeneratedFromColumn("hide_email")
    public Boolean getHideEmail() {
        return hideEmail;
    }

    /**
     * Setter method for the hideEmail.
     * @param hideEmail the hideEmail to set
     */
    public void setHideEmail(Boolean hideEmail) {
        this.hideEmail = hideEmail;
    }

    /**
     * Getter method for the isDst.
     * @return the isDst
     */
    @GeneratedFromColumn("is_dst")
    public Boolean getIsDst() {
        return isDst;
    }

    /**
     * Setter method for the isDst.
     * @param isDst the isDst to set
     */
    public void setIsDst(Boolean isDst) {
        this.isDst = isDst;
    }

    /**
     * Getter method for the lastActiveForum.
     * @return the lastActiveForum
     */
    @GeneratedFromColumn("last_active_forum")
    public Integer getLastActiveForum() {
        return lastActiveForum;
    }

    /**
     * Setter method for the lastActiveForum.
     * @param lastActiveForum the lastActiveForum to set
     */
    public void setLastActiveForum(Integer lastActiveForum) {
        this.lastActiveForum = lastActiveForum;
    }

    /**
     * Getter method for the moderationEmail.
     * @return the moderationEmail
     */
    @GeneratedFromColumn("moderation_email")
    public Boolean getModerationEmail() {
        return moderationEmail;
    }

    /**
     * Setter method for the moderationEmail.
     * @param moderationEmail the moderationEmail to set
     */
    public void setModerationEmail(Boolean moderationEmail) {
        this.moderationEmail = moderationEmail;
    }

    /**
     * Getter method for the moderatorData.
     * @return the moderatorData
     */
    @GeneratedFromColumn("moderator_data")
    public String getModeratorData() {
        return moderatorData;
    }

    /**
     * Setter method for the moderatorData.
     * @param moderatorData the moderatorData to set
     */
    public void setModeratorData(String moderatorData) {
        this.moderatorData = moderatorData;
    }

    /**
     * Getter method for the password.
     * @return the password
     */
    @GeneratedFromColumn("password")
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for the password.
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Getter method for the passwordTemp.
     * @return the passwordTemp
     */
    @GeneratedFromColumn("password_temp")
    public String getPasswordTemp() {
        return passwordTemp;
    }

    /**
     * Setter method for the passwordTemp.
     * @param passwordTemp the passwordTemp to set
     */
    public void setPasswordTemp(String passwordTemp) {
        this.passwordTemp = passwordTemp;
    }

    /**
     * Getter method for the pmEmailNotify.
     * @return the pmEmailNotify
     */
    @GeneratedFromColumn("pm_email_notify")
    public Boolean getPmEmailNotify() {
        return pmEmailNotify;
    }

    /**
     * Setter method for the pmEmailNotify.
     * @param pmEmailNotify the pmEmailNotify to set
     */
    public void setPmEmailNotify(Boolean pmEmailNotify) {
        this.pmEmailNotify = pmEmailNotify;
    }

    /**
     * Getter method for the posts.
     * @return the posts
     */
    @GeneratedFromColumn("posts")
    public Integer getPosts() {
        return posts;
    }

    /**
     * Setter method for the posts.
     * @param posts the posts to set
     */
    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    /**
     * Getter method for the realName.
     * @return the realName
     */
    @GeneratedFromColumn("real_name")
    public String getRealName() {
        return realName;
    }

    /**
     * Setter method for the realName.
     * @param realName the realName to set
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * Getter method for the sessidLt.
     * @return the sessidLt
     */
    @GeneratedFromColumn("sessid_lt")
    public String getSessidLt() {
        return sessidLt;
    }

    /**
     * Setter method for the sessidLt.
     * @param sessidLt the sessidLt to set
     */
    public void setSessidLt(String sessidLt) {
        this.sessidLt = sessidLt;
    }

    /**
     * Getter method for the sessidSt.
     * @return the sessidSt
     */
    @GeneratedFromColumn("sessid_st")
    public String getSessidSt() {
        return sessidSt;
    }

    /**
     * Setter method for the sessidSt.
     * @param sessidSt the sessidSt to set
     */
    public void setSessidSt(String sessidSt) {
        this.sessidSt = sessidSt;
    }

    /**
     * Getter method for the sessidStTimeout.
     * @return the sessidStTimeout
     */
    @GeneratedFromColumn("sessid_st_timeout")
    public Integer getSessidStTimeout() {
        return sessidStTimeout;
    }

    /**
     * Setter method for the sessidStTimeout.
     * @param sessidStTimeout the sessidStTimeout to set
     */
    public void setSessidStTimeout(Integer sessidStTimeout) {
        this.sessidStTimeout = sessidStTimeout;
    }

    /**
     * Getter method for the settingsData.
     * @return the settingsData
     */
    @GeneratedFromColumn("settings_data")
    public String getSettingsData() {
        return settingsData;
    }

    /**
     * Setter method for the settingsData.
     * @param settingsData the settingsData to set
     */
    public void setSettingsData(String settingsData) {
        this.settingsData = settingsData;
    }

    /**
     * Getter method for the showSignature.
     * @return the showSignature
     */
    @GeneratedFromColumn("show_signature")
    public Boolean getShowSignature() {
        return showSignature;
    }

    /**
     * Setter method for the showSignature.
     * @param showSignature the showSignature to set
     */
    public void setShowSignature(Boolean showSignature) {
        this.showSignature = showSignature;
    }

    /**
     * Getter method for the signature.
     * @return the signature
     */
    @GeneratedFromColumn("signature")
    public String getSignature() {
        return signature;
    }

    /**
     * Setter method for the signature.
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

    /**
     * Getter method for the threadedList.
     * @return the threadedList
     */
    @GeneratedFromColumn("threaded_list")
    public Boolean getThreadedList() {
        return threadedList;
    }

    /**
     * Setter method for the threadedList.
     * @param threadedList the threadedList to set
     */
    public void setThreadedList(Boolean threadedList) {
        this.threadedList = threadedList;
    }

    /**
     * Getter method for the threadedRead.
     * @return the threadedRead
     */
    @GeneratedFromColumn("threaded_read")
    public Boolean getThreadedRead() {
        return threadedRead;
    }

    /**
     * Setter method for the threadedRead.
     * @param threadedRead the threadedRead to set
     */
    public void setThreadedRead(Boolean threadedRead) {
        this.threadedRead = threadedRead;
    }

    /**
     * Getter method for the tzOffset.
     * @return the tzOffset
     */
    @GeneratedFromColumn("tz_offset")
    public Float getTzOffset() {
        return tzOffset;
    }

    /**
     * Setter method for the tzOffset.
     * @param tzOffset the tzOffset to set
     */
    public void setTzOffset(Float tzOffset) {
        this.tzOffset = tzOffset;
    }

    /**
     * Getter method for the userId.
     * @return the userId
     */
    @GeneratedFromColumn("user_id")
    public Integer getUserId() {
        return userId;
    }

    /**
     * Setter method for the userId.
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Getter method for the userLanguage.
     * @return the userLanguage
     */
    @GeneratedFromColumn("user_language")
    public String getUserLanguage() {
        return userLanguage;
    }

    /**
     * Setter method for the userLanguage.
     * @param userLanguage the userLanguage to set
     */
    public void setUserLanguage(String userLanguage) {
        this.userLanguage = userLanguage;
    }

    /**
     * Getter method for the userTemplate.
     * @return the userTemplate
     */
    @GeneratedFromColumn("user_template")
    public String getUserTemplate() {
        return userTemplate;
    }

    /**
     * Setter method for the userTemplate.
     * @param userTemplate the userTemplate to set
     */
    public void setUserTemplate(String userTemplate) {
        this.userTemplate = userTemplate;
    }

    /**
     * Getter method for the username.
     * @return the username
     */
    @GeneratedFromColumn("username")
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for the username.
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PhorumUsers. active = " + active + ", admin = " + admin + ", dateAdded = " + dateAdded + ", dateLastActive = " + dateLastActive + ", displayName = " + displayName + ", email = " + email + ", emailNotify = " + emailNotify + ", emailTemp = " + emailTemp + ", hideActivity = " + hideActivity + ", hideEmail = " + hideEmail + ", isDst = " + isDst + ", lastActiveForum = " + lastActiveForum + ", moderationEmail = " + moderationEmail + ", moderatorData = " + moderatorData + ", password = " + password + ", passwordTemp = " + passwordTemp + ", pmEmailNotify = " + pmEmailNotify + ", posts = " + posts + ", realName = " + realName + ", sessidLt = " + sessidLt + ", sessidSt = " + sessidSt + ", sessidStTimeout = " + sessidStTimeout + ", settingsData = " + settingsData + ", showSignature = " + showSignature + ", signature = " + signature + ", threadedList = " + threadedList + ", threadedRead = " + threadedRead + ", tzOffset = " + tzOffset + ", userId = " + userId + ", userLanguage = " + userLanguage + ", userTemplate = " + userTemplate + ", username = " + username + "}";
    }

}

