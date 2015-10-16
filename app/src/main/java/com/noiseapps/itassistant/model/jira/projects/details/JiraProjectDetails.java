package com.noiseapps.itassistant.model.jira.projects.details;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.noiseapps.itassistant.model.jira.issues.common.IssueType;
import com.noiseapps.itassistant.model.jira.projects.AvatarUrls;

public class JiraProjectDetails {

    @SerializedName("expand")
    @Expose
    private String expand;
    @SerializedName("self")
    @Expose
    private String self;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("lead")
    @Expose
    private Lead lead;
    @SerializedName("components")
    @Expose
    private List<Object> components = new ArrayList<Object>();
    @SerializedName("issueTypes")
    @Expose
    private List<IssueType> issueTypes = new ArrayList<IssueType>();
    @SerializedName("assigneeType")
    @Expose
    private String assigneeType;
    @SerializedName("versions")
    @Expose
    private List<Version> versions = new ArrayList<Version>();
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("roles")
    @Expose
    private Roles roles;
    @SerializedName("avatarUrls")
    @Expose
    private AvatarUrls avatarUrls;

    /**
     * 
     * @return
     *     The expand
     */
    public String getExpand() {
        return expand;
    }

    /**
     * 
     * @param expand
     *     The expand
     */
    public void setExpand(String expand) {
        this.expand = expand;
    }

    /**
     * 
     * @return
     *     The self
     */
    public String getSelf() {
        return self;
    }

    /**
     * 
     * @param self
     *     The self
     */
    public void setSelf(String self) {
        this.self = self;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The key
     */
    public String getKey() {
        return key;
    }

    /**
     * 
     * @param key
     *     The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The lead
     */
    public Lead getLead() {
        return lead;
    }

    /**
     * 
     * @param lead
     *     The lead
     */
    public void setLead(Lead lead) {
        this.lead = lead;
    }

    /**
     * 
     * @return
     *     The components
     */
    public List<Object> getComponents() {
        return components;
    }

    /**
     * 
     * @param components
     *     The components
     */
    public void setComponents(List<Object> components) {
        this.components = components;
    }

    /**
     * 
     * @return
     *     The issueTypes
     */
    public List<IssueType> getIssueTypes() {
        return issueTypes;
    }

    /**
     * 
     * @param issueTypes
     *     The issueTypes
     */
    public void setIssueTypes(List<IssueType> issueTypes) {
        this.issueTypes = issueTypes;
    }

    /**
     * 
     * @return
     *     The assigneeType
     */
    public String getAssigneeType() {
        return assigneeType;
    }

    /**
     * 
     * @param assigneeType
     *     The assigneeType
     */
    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    /**
     * 
     * @return
     *     The versions
     */
    public List<Version> getVersions() {
        return versions;
    }

    /**
     * 
     * @param versions
     *     The versions
     */
    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The roles
     */
    public Roles getRoles() {
        return roles;
    }

    /**
     * 
     * @param roles
     *     The roles
     */
    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    /**
     * 
     * @return
     *     The avatarUrls
     */
    public AvatarUrls getAvatarUrls() {
        return avatarUrls;
    }

    /**
     * 
     * @param avatarUrls
     *     The avatarUrls
     */
    public void setAvatarUrls(AvatarUrls avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    @Override
    public String toString() {
        return "JiraProjectDetails{" +
                "expand='" + expand + '\'' +
                ", self='" + self + '\'' +
                ", id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", description='" + description + '\'' +
                ", lead=" + lead +
                ", components=" + components +
                ", issueTypes=" + issueTypes +
                ", assigneeType='" + assigneeType + '\'' +
                ", versions=" + versions +
                ", name='" + name + '\'' +
                ", roles=" + roles +
                ", avatarUrls=" + avatarUrls +
                '}';
    }
}
