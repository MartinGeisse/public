/**
 * This file was generated from the database schema.
 */
package name.martingeisse.apidemo.phorum;

import java.io.Serializable;

/**
 * This class represents rows from table 'phorum_forums'.
 */
public class PhorumForums implements Serializable {

    /**
     * Constructor.
     */
    public PhorumForums() {
    }

    /**
     * the active
     */
    private Boolean active;

    /**
     * the allowAttachmentTypes
     */
    private String allowAttachmentTypes;

    /**
     * the allowEmailNotify
     */
    private Boolean allowEmailNotify;

    /**
     * the cacheVersion
     */
    private Integer cacheVersion;

    /**
     * the checkDuplicate
     */
    private Boolean checkDuplicate;

    /**
     * the countViews
     */
    private Boolean countViews;

    /**
     * the countViewsPerThread
     */
    private Boolean countViewsPerThread;

    /**
     * the description
     */
    private String description;

    /**
     * the displayFixed
     */
    private Boolean displayFixed;

    /**
     * the displayIpAddress
     */
    private Boolean displayIpAddress;

    /**
     * the displayOrder
     */
    private Integer displayOrder;

    /**
     * the editPost
     */
    private Boolean editPost;

    /**
     * the emailModerators
     */
    private Boolean emailModerators;

    /**
     * the floatToTop
     */
    private Boolean floatToTop;

    /**
     * the folderFlag
     */
    private Boolean folderFlag;

    /**
     * the forumId
     */
    private Integer forumId;

    /**
     * the forumPath
     */
    private String forumPath;

    /**
     * the inheritId
     */
    private Integer inheritId;

    /**
     * the language
     */
    private String language;

    /**
     * the lastPostTime
     */
    private Integer lastPostTime;

    /**
     * the listLengthFlat
     */
    private Integer listLengthFlat;

    /**
     * the listLengthThreaded
     */
    private Integer listLengthThreaded;

    /**
     * the maxAttachmentSize
     */
    private Integer maxAttachmentSize;

    /**
     * the maxAttachments
     */
    private Integer maxAttachments;

    /**
     * the maxTotalattachmentSize
     */
    private Integer maxTotalattachmentSize;

    /**
     * the messageCount
     */
    private Integer messageCount;

    /**
     * the moderation
     */
    private Integer moderation;

    /**
     * the name
     */
    private String name;

    /**
     * the parentId
     */
    private Integer parentId;

    /**
     * the pubPerms
     */
    private Integer pubPerms;

    /**
     * the readLength
     */
    private Integer readLength;

    /**
     * the regPerms
     */
    private Integer regPerms;

    /**
     * the reverseThreading
     */
    private Boolean reverseThreading;

    /**
     * the stickyCount
     */
    private Integer stickyCount;

    /**
     * the template
     */
    private String template;

    /**
     * the templateSettings
     */
    private String templateSettings;

    /**
     * the threadCount
     */
    private Integer threadCount;

    /**
     * the threadedList
     */
    private Boolean threadedList;

    /**
     * the threadedRead
     */
    private Boolean threadedRead;

    /**
     * the vroot
     */
    private Integer vroot;

    /**
     * Getter method for the active.
     * @return the active
     */
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
     * Getter method for the allowAttachmentTypes.
     * @return the allowAttachmentTypes
     */
    public String getAllowAttachmentTypes() {
        return allowAttachmentTypes;
    }

    /**
     * Setter method for the allowAttachmentTypes.
     * @param allowAttachmentTypes the allowAttachmentTypes to set
     */
    public void setAllowAttachmentTypes(String allowAttachmentTypes) {
        this.allowAttachmentTypes = allowAttachmentTypes;
    }

    /**
     * Getter method for the allowEmailNotify.
     * @return the allowEmailNotify
     */
    public Boolean getAllowEmailNotify() {
        return allowEmailNotify;
    }

    /**
     * Setter method for the allowEmailNotify.
     * @param allowEmailNotify the allowEmailNotify to set
     */
    public void setAllowEmailNotify(Boolean allowEmailNotify) {
        this.allowEmailNotify = allowEmailNotify;
    }

    /**
     * Getter method for the cacheVersion.
     * @return the cacheVersion
     */
    public Integer getCacheVersion() {
        return cacheVersion;
    }

    /**
     * Setter method for the cacheVersion.
     * @param cacheVersion the cacheVersion to set
     */
    public void setCacheVersion(Integer cacheVersion) {
        this.cacheVersion = cacheVersion;
    }

    /**
     * Getter method for the checkDuplicate.
     * @return the checkDuplicate
     */
    public Boolean getCheckDuplicate() {
        return checkDuplicate;
    }

    /**
     * Setter method for the checkDuplicate.
     * @param checkDuplicate the checkDuplicate to set
     */
    public void setCheckDuplicate(Boolean checkDuplicate) {
        this.checkDuplicate = checkDuplicate;
    }

    /**
     * Getter method for the countViews.
     * @return the countViews
     */
    public Boolean getCountViews() {
        return countViews;
    }

    /**
     * Setter method for the countViews.
     * @param countViews the countViews to set
     */
    public void setCountViews(Boolean countViews) {
        this.countViews = countViews;
    }

    /**
     * Getter method for the countViewsPerThread.
     * @return the countViewsPerThread
     */
    public Boolean getCountViewsPerThread() {
        return countViewsPerThread;
    }

    /**
     * Setter method for the countViewsPerThread.
     * @param countViewsPerThread the countViewsPerThread to set
     */
    public void setCountViewsPerThread(Boolean countViewsPerThread) {
        this.countViewsPerThread = countViewsPerThread;
    }

    /**
     * Getter method for the description.
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for the description.
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter method for the displayFixed.
     * @return the displayFixed
     */
    public Boolean getDisplayFixed() {
        return displayFixed;
    }

    /**
     * Setter method for the displayFixed.
     * @param displayFixed the displayFixed to set
     */
    public void setDisplayFixed(Boolean displayFixed) {
        this.displayFixed = displayFixed;
    }

    /**
     * Getter method for the displayIpAddress.
     * @return the displayIpAddress
     */
    public Boolean getDisplayIpAddress() {
        return displayIpAddress;
    }

    /**
     * Setter method for the displayIpAddress.
     * @param displayIpAddress the displayIpAddress to set
     */
    public void setDisplayIpAddress(Boolean displayIpAddress) {
        this.displayIpAddress = displayIpAddress;
    }

    /**
     * Getter method for the displayOrder.
     * @return the displayOrder
     */
    public Integer getDisplayOrder() {
        return displayOrder;
    }

    /**
     * Setter method for the displayOrder.
     * @param displayOrder the displayOrder to set
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * Getter method for the editPost.
     * @return the editPost
     */
    public Boolean getEditPost() {
        return editPost;
    }

    /**
     * Setter method for the editPost.
     * @param editPost the editPost to set
     */
    public void setEditPost(Boolean editPost) {
        this.editPost = editPost;
    }

    /**
     * Getter method for the emailModerators.
     * @return the emailModerators
     */
    public Boolean getEmailModerators() {
        return emailModerators;
    }

    /**
     * Setter method for the emailModerators.
     * @param emailModerators the emailModerators to set
     */
    public void setEmailModerators(Boolean emailModerators) {
        this.emailModerators = emailModerators;
    }

    /**
     * Getter method for the floatToTop.
     * @return the floatToTop
     */
    public Boolean getFloatToTop() {
        return floatToTop;
    }

    /**
     * Setter method for the floatToTop.
     * @param floatToTop the floatToTop to set
     */
    public void setFloatToTop(Boolean floatToTop) {
        this.floatToTop = floatToTop;
    }

    /**
     * Getter method for the folderFlag.
     * @return the folderFlag
     */
    public Boolean getFolderFlag() {
        return folderFlag;
    }

    /**
     * Setter method for the folderFlag.
     * @param folderFlag the folderFlag to set
     */
    public void setFolderFlag(Boolean folderFlag) {
        this.folderFlag = folderFlag;
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
     * Getter method for the forumPath.
     * @return the forumPath
     */
    public String getForumPath() {
        return forumPath;
    }

    /**
     * Setter method for the forumPath.
     * @param forumPath the forumPath to set
     */
    public void setForumPath(String forumPath) {
        this.forumPath = forumPath;
    }

    /**
     * Getter method for the inheritId.
     * @return the inheritId
     */
    public Integer getInheritId() {
        return inheritId;
    }

    /**
     * Setter method for the inheritId.
     * @param inheritId the inheritId to set
     */
    public void setInheritId(Integer inheritId) {
        this.inheritId = inheritId;
    }

    /**
     * Getter method for the language.
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Setter method for the language.
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Getter method for the lastPostTime.
     * @return the lastPostTime
     */
    public Integer getLastPostTime() {
        return lastPostTime;
    }

    /**
     * Setter method for the lastPostTime.
     * @param lastPostTime the lastPostTime to set
     */
    public void setLastPostTime(Integer lastPostTime) {
        this.lastPostTime = lastPostTime;
    }

    /**
     * Getter method for the listLengthFlat.
     * @return the listLengthFlat
     */
    public Integer getListLengthFlat() {
        return listLengthFlat;
    }

    /**
     * Setter method for the listLengthFlat.
     * @param listLengthFlat the listLengthFlat to set
     */
    public void setListLengthFlat(Integer listLengthFlat) {
        this.listLengthFlat = listLengthFlat;
    }

    /**
     * Getter method for the listLengthThreaded.
     * @return the listLengthThreaded
     */
    public Integer getListLengthThreaded() {
        return listLengthThreaded;
    }

    /**
     * Setter method for the listLengthThreaded.
     * @param listLengthThreaded the listLengthThreaded to set
     */
    public void setListLengthThreaded(Integer listLengthThreaded) {
        this.listLengthThreaded = listLengthThreaded;
    }

    /**
     * Getter method for the maxAttachmentSize.
     * @return the maxAttachmentSize
     */
    public Integer getMaxAttachmentSize() {
        return maxAttachmentSize;
    }

    /**
     * Setter method for the maxAttachmentSize.
     * @param maxAttachmentSize the maxAttachmentSize to set
     */
    public void setMaxAttachmentSize(Integer maxAttachmentSize) {
        this.maxAttachmentSize = maxAttachmentSize;
    }

    /**
     * Getter method for the maxAttachments.
     * @return the maxAttachments
     */
    public Integer getMaxAttachments() {
        return maxAttachments;
    }

    /**
     * Setter method for the maxAttachments.
     * @param maxAttachments the maxAttachments to set
     */
    public void setMaxAttachments(Integer maxAttachments) {
        this.maxAttachments = maxAttachments;
    }

    /**
     * Getter method for the maxTotalattachmentSize.
     * @return the maxTotalattachmentSize
     */
    public Integer getMaxTotalattachmentSize() {
        return maxTotalattachmentSize;
    }

    /**
     * Setter method for the maxTotalattachmentSize.
     * @param maxTotalattachmentSize the maxTotalattachmentSize to set
     */
    public void setMaxTotalattachmentSize(Integer maxTotalattachmentSize) {
        this.maxTotalattachmentSize = maxTotalattachmentSize;
    }

    /**
     * Getter method for the messageCount.
     * @return the messageCount
     */
    public Integer getMessageCount() {
        return messageCount;
    }

    /**
     * Setter method for the messageCount.
     * @param messageCount the messageCount to set
     */
    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * Getter method for the moderation.
     * @return the moderation
     */
    public Integer getModeration() {
        return moderation;
    }

    /**
     * Setter method for the moderation.
     * @param moderation the moderation to set
     */
    public void setModeration(Integer moderation) {
        this.moderation = moderation;
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
    public Integer getParentId() {
        return parentId;
    }

    /**
     * Setter method for the parentId.
     * @param parentId the parentId to set
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * Getter method for the pubPerms.
     * @return the pubPerms
     */
    public Integer getPubPerms() {
        return pubPerms;
    }

    /**
     * Setter method for the pubPerms.
     * @param pubPerms the pubPerms to set
     */
    public void setPubPerms(Integer pubPerms) {
        this.pubPerms = pubPerms;
    }

    /**
     * Getter method for the readLength.
     * @return the readLength
     */
    public Integer getReadLength() {
        return readLength;
    }

    /**
     * Setter method for the readLength.
     * @param readLength the readLength to set
     */
    public void setReadLength(Integer readLength) {
        this.readLength = readLength;
    }

    /**
     * Getter method for the regPerms.
     * @return the regPerms
     */
    public Integer getRegPerms() {
        return regPerms;
    }

    /**
     * Setter method for the regPerms.
     * @param regPerms the regPerms to set
     */
    public void setRegPerms(Integer regPerms) {
        this.regPerms = regPerms;
    }

    /**
     * Getter method for the reverseThreading.
     * @return the reverseThreading
     */
    public Boolean getReverseThreading() {
        return reverseThreading;
    }

    /**
     * Setter method for the reverseThreading.
     * @param reverseThreading the reverseThreading to set
     */
    public void setReverseThreading(Boolean reverseThreading) {
        this.reverseThreading = reverseThreading;
    }

    /**
     * Getter method for the stickyCount.
     * @return the stickyCount
     */
    public Integer getStickyCount() {
        return stickyCount;
    }

    /**
     * Setter method for the stickyCount.
     * @param stickyCount the stickyCount to set
     */
    public void setStickyCount(Integer stickyCount) {
        this.stickyCount = stickyCount;
    }

    /**
     * Getter method for the template.
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Setter method for the template.
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Getter method for the templateSettings.
     * @return the templateSettings
     */
    public String getTemplateSettings() {
        return templateSettings;
    }

    /**
     * Setter method for the templateSettings.
     * @param templateSettings the templateSettings to set
     */
    public void setTemplateSettings(String templateSettings) {
        this.templateSettings = templateSettings;
    }

    /**
     * Getter method for the threadCount.
     * @return the threadCount
     */
    public Integer getThreadCount() {
        return threadCount;
    }

    /**
     * Setter method for the threadCount.
     * @param threadCount the threadCount to set
     */
    public void setThreadCount(Integer threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * Getter method for the threadedList.
     * @return the threadedList
     */
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
     * Getter method for the vroot.
     * @return the vroot
     */
    public Integer getVroot() {
        return vroot;
    }

    /**
     * Setter method for the vroot.
     * @param vroot the vroot to set
     */
    public void setVroot(Integer vroot) {
        this.vroot = vroot;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "{PhorumForums. active = " + active + ", allowAttachmentTypes = " + allowAttachmentTypes + ", allowEmailNotify = " + allowEmailNotify + ", cacheVersion = " + cacheVersion + ", checkDuplicate = " + checkDuplicate + ", countViews = " + countViews + ", countViewsPerThread = " + countViewsPerThread + ", description = " + description + ", displayFixed = " + displayFixed + ", displayIpAddress = " + displayIpAddress + ", displayOrder = " + displayOrder + ", editPost = " + editPost + ", emailModerators = " + emailModerators + ", floatToTop = " + floatToTop + ", folderFlag = " + folderFlag + ", forumId = " + forumId + ", forumPath = " + forumPath + ", inheritId = " + inheritId + ", language = " + language + ", lastPostTime = " + lastPostTime + ", listLengthFlat = " + listLengthFlat + ", listLengthThreaded = " + listLengthThreaded + ", maxAttachmentSize = " + maxAttachmentSize + ", maxAttachments = " + maxAttachments + ", maxTotalattachmentSize = " + maxTotalattachmentSize + ", messageCount = " + messageCount + ", moderation = " + moderation + ", name = " + name + ", parentId = " + parentId + ", pubPerms = " + pubPerms + ", readLength = " + readLength + ", regPerms = " + regPerms + ", reverseThreading = " + reverseThreading + ", stickyCount = " + stickyCount + ", template = " + template + ", templateSettings = " + templateSettings + ", threadCount = " + threadCount + ", threadedList = " + threadedList + ", threadedRead = " + threadedRead + ", vroot = " + vroot + "}";
    }

}

