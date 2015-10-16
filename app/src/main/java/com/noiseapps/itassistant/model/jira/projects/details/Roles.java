
package com.noiseapps.itassistant.model.jira.projects.details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Roles {

    @SerializedName("Users")
    @Expose
    private String Users;
    @SerializedName("Service Desk Team")
    @Expose
    private String ServiceDeskTeam;
    @SerializedName("Administrators")
    @Expose
    private String Administrators;
    @SerializedName("Reviewers")
    @Expose
    private String Reviewers;
    @SerializedName("Tempo Project Managers")
    @Expose
    private String TempoProjectManagers;
    @SerializedName("Developers")
    @Expose
    private String Developers;
    @SerializedName("Service Desk Customers")
    @Expose
    private String ServiceDeskCustomers;

    /**
     * 
     * @return
     *     The Users
     */
    public String getUsers() {
        return Users;
    }

    /**
     * 
     * @param Users
     *     The Users
     */
    public void setUsers(String Users) {
        this.Users = Users;
    }

    /**
     * 
     * @return
     *     The ServiceDeskTeam
     */
    public String getServiceDeskTeam() {
        return ServiceDeskTeam;
    }

    /**
     * 
     * @param ServiceDeskTeam
     *     The Service Desk Team
     */
    public void setServiceDeskTeam(String ServiceDeskTeam) {
        this.ServiceDeskTeam = ServiceDeskTeam;
    }

    /**
     * 
     * @return
     *     The Administrators
     */
    public String getAdministrators() {
        return Administrators;
    }

    /**
     * 
     * @param Administrators
     *     The Administrators
     */
    public void setAdministrators(String Administrators) {
        this.Administrators = Administrators;
    }

    /**
     * 
     * @return
     *     The Reviewers
     */
    public String getReviewers() {
        return Reviewers;
    }

    /**
     * 
     * @param Reviewers
     *     The Reviewers
     */
    public void setReviewers(String Reviewers) {
        this.Reviewers = Reviewers;
    }

    /**
     * 
     * @return
     *     The TempoProjectManagers
     */
    public String getTempoProjectManagers() {
        return TempoProjectManagers;
    }

    /**
     * 
     * @param TempoProjectManagers
     *     The Tempo Project Managers
     */
    public void setTempoProjectManagers(String TempoProjectManagers) {
        this.TempoProjectManagers = TempoProjectManagers;
    }

    /**
     * 
     * @return
     *     The Developers
     */
    public String getDevelopers() {
        return Developers;
    }

    /**
     * 
     * @param Developers
     *     The Developers
     */
    public void setDevelopers(String Developers) {
        this.Developers = Developers;
    }

    /**
     * 
     * @return
     *     The ServiceDeskCustomers
     */
    public String getServiceDeskCustomers() {
        return ServiceDeskCustomers;
    }

    /**
     * 
     * @param ServiceDeskCustomers
     *     The Service Desk Customers
     */
    public void setServiceDeskCustomers(String ServiceDeskCustomers) {
        this.ServiceDeskCustomers = ServiceDeskCustomers;
    }

}
